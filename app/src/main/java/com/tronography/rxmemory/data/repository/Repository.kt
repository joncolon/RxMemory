package com.tronography.rxmemory.data.repository


import DEBUG
import ERROR
import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import com.tronography.rxmemory.data.local.CardDao
import com.tronography.rxmemory.data.local.MutableCard
import com.tronography.rxmemory.data.local.MutablePokemonData
import com.tronography.rxmemory.data.local.PokemonDao
import com.tronography.rxmemory.data.model.cards.Card
import com.tronography.rxmemory.data.model.common.NamedApiResource
import com.tronography.rxmemory.data.model.common.NamedApiResourceList
import com.tronography.rxmemory.data.model.pokemon.PokemonData
import com.tronography.rxmemory.data.model.pokemon.PokemonResponse
import com.tronography.rxmemory.data.model.species.SpeciesResponse
import com.tronography.rxmemory.data.remote.PokeClient
import com.tronography.rxmemory.data.state.GameState
import com.tronography.rxmemory.data.state.GameStateLiveData
import com.tronography.rxmemory.data.state.NetworkState
import com.tronography.rxmemory.data.state.NetworkStateLiveData
import com.tronography.rxmemory.utilities.GlideUtils
import executeInThread
import io.reactivex.Observable
import io.reactivex.Observable.fromCallable
import io.reactivex.Observable.zip
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository
@Inject constructor(

        private val liveGameState: GameStateLiveData,

        private val liveNetworkState: NetworkStateLiveData,

        private val cardDao: CardDao,

        private val pokemonDao: PokemonDao,

        private val client: PokeClient,

        private val glideUtils: GlideUtils

) {

    var downloadInProgress: Boolean = false

    fun insertCard(card: Card) {
        executeInThread { cardDao.insert(card.toMutable()) }
    }

    fun deleteCardTable() {
        executeInThread { cardDao.deleteTable() }
    }

    fun getCards(): LiveData<List<Card>> {
        return cardDao.getAllCards()
    }

    fun updatePokemonAsCaught(id: Int, isCaught: Boolean) {
        executeInThread { pokemonDao.updateCaught(id, isCaught) }
    }

    fun getCaughtPokemon(): LiveData<List<PokemonData>> {
        return pokemonDao.getAllPokemon()
    }

    fun getLiveGameState(): GameStateLiveData {
        return liveGameState
    }

    fun getLiveNetworkError(): NetworkStateLiveData {
        return liveNetworkState
    }

    @SuppressLint("CheckResult")
    fun downloadPokemon() {
        downloadInProgress = true
        getPokemonDatabaseCount()
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableSingleObserver<Int>() {

                    override fun onSuccess(pokemonCount: Int) {
                        DEBUG("Pokemon Count = $pokemonCount")
                        var offset = 0
                        var limit = FULL_DATABASE

                        if (pokemonCount > 0) {
                            offset = FULL_DATABASE - pokemonCount
                            limit = (FULL_DATABASE - offset) + 1
                        }
                        DEBUG("limit = $limit / offset = $offset")

                        if (pokemonCount < FULL_DATABASE) {
                            getPokemonFromApi(limit, offset)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeWith(object : DisposableObserver<Unit>() {
                                        override fun onComplete() {
                                            downloadInProgress = false
                                            DEBUG("getPokemonFromApi Complete")
                                        }

                                        override fun onNext(t: Unit) {
                                            downloadInProgress = false
                                            DEBUG("getPokemonFromApi Success : $t")
                                        }

                                        override fun onError(e: Throwable) {
                                            downloadInProgress = false
                                            liveNetworkState.setNetworkState(NetworkState.ERROR)
                                            DEBUG("getPokemonFromApi Success : $e")
                                        }
                                    })
                        }
                    }

                    override fun onError(e: Throwable) {
                        ERROR("Unable to get Pokemon DB Count : $e")
                    }
                })
    }

    @SuppressLint("CheckResult")
    fun createNewPokemonDeck() {
        if (!downloadInProgress) {
            downloadPokemon()
        }
        liveGameState.setGameStateLiveData(GameState.LOADING)
        getEightPokemonFromDB()
                .subscribeOn(Schedulers.io())
                .concatMap {
                    fromCallable { preloadImages(it) }.observeOn(AndroidSchedulers.mainThread())
                    Observable.just(updateCardDatabase(it))
                }
                .observeOn(AndroidSchedulers.mainThread())
                .retry(10)
                .subscribeWith(object : DisposableObserver<Unit>() {
                    override fun onComplete() {
                        liveGameState.setGameStateLiveData(GameState.LOAD_COMPLETE)
                    }

                    override fun onNext(t: Unit) {

                    }

                    override fun onError(e: Throwable) {
                        liveGameState.setGameStateLiveData(GameState.ERROR)
                        ERROR("Unable to create a new deck : \n${e.cause} \n${e.message}")
                    }
                })
    }

    private fun getPokemonFromApi(limit: Int, offset: Int): Observable<Unit> {
        return client.getPokemonList(limit, offset)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { DEBUG("Starting at offset $offset on thread ${Thread.currentThread().name}") }
                .flatMap { namedApiResourceList: NamedApiResourceList ->
                    Observable.fromIterable(namedApiResourceList.results)
                            .subscribeOn(Schedulers.io())
                            .doOnSubscribe {
                                DEBUG("Subscribed to $namedApiResourceList on ${Thread.currentThread().name}")
                            }
                }
                .delay(2000, TimeUnit.MILLISECONDS)
                .doOnNext { DEBUG("Retrieving ${it.name} PokemonResponse and Species Data") }
                .flatMap ({ namedApiResource: NamedApiResource ->
                    zip(client.getPokemon(namedApiResource.name), client.getSpecies(namedApiResource.name),
                            BiFunction<PokemonResponse, SpeciesResponse, PokemonData> { t1, t2 ->
                                return@BiFunction PokemonData(
                                        t1.types,
                                        t1.weight,
                                        t1.sprites,
                                        t2.habitat.name,
                                        t2.flavorTextEntries.filter { it.language.name == "en" }[0].flavorText,
                                        t1.name,
                                        t1.id,
                                        t1.height)
                            }).subscribeOn(Schedulers.io())
                            .doOnNext {
                                DEBUG("Received on ${it.name} ${it.id} on ${Thread.currentThread().name}")
                            }
                            .concatMap { fromCallable { updatePokemonDatabase(it.toMutable()) } }
                }, MAX_CONCURRENCY)
                .retry(5)
    }

    private fun getEightPokemonFromDB(): Observable<List<PokemonData>> {
        return pokemonDao.getEightRandomPokemon()
                .toObservable()
                .doOnNext { DEBUG("Dispatching ${it.size} Pokemon from DB...") }
    }

    private fun getPokemonDatabaseCount(): Single<Int> {
        return pokemonDao.getPokemonCount()
    }

    fun getPokemonDatabaseCountLiveData(): LiveData<Int> {
        return pokemonDao.getLivePokemonCount()
    }

    private fun preloadImages(cards: List<PokemonData>) {
        cards.forEach { card -> card.sprites.frontDefault?.let { glideUtils.preloadImages(it) } }
    }

    private fun updateCardDatabase(pokemon: List<PokemonData>) {
        DEBUG("Saving ${pokemon.size} Cards to DB")
        val cards = mutableListOf<MutableCard>()
        pokemon.forEach {

            val photoUrl = it.sprites.frontDefault!!
            val card = Card(it.id, photoUrl, it.name)
            val duplicateCard = Card(it.id, photoUrl, it.name)

            cards.add(card.toMutable())
            cards.add(duplicateCard.toMutable())

        }

        cardDao.repopulateTable(cards)
    }

    private fun updatePokemonDatabase(pokemon: MutablePokemonData) {
        pokemonDao.insert(pokemon)
        DEBUG("Inserted ${pokemon.name} Pokemon into DB")
    }

    companion object {
        private const val ONE_FIFTY_ONE = 151
        private const val NO_OFFSET = 0
        private const val FULL_DATABASE = 151
        private const val MAX_CONCURRENCY = 3
    }
}
