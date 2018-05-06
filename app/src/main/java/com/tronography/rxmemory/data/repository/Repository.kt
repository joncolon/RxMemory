package com.tronography.rxmemory.data.repository


import DEBUG
import ERROR
import android.arch.lifecycle.LiveData
import com.tronography.rxmemory.data.local.CardDao
import com.tronography.rxmemory.data.local.MutableCard
import com.tronography.rxmemory.data.local.MutablePokemon
import com.tronography.rxmemory.data.local.PokemonDao
import com.tronography.rxmemory.data.model.cards.Card
import com.tronography.rxmemory.data.model.common.NamedApiResource
import com.tronography.rxmemory.data.model.common.NamedApiResourceList
import com.tronography.rxmemory.data.model.pokemon.Pokemon
import com.tronography.rxmemory.data.remote.PokeClient
import com.tronography.rxmemory.data.state.GameState
import com.tronography.rxmemory.data.state.GameStateLiveData
import com.tronography.rxmemory.data.state.NetworkState
import com.tronography.rxmemory.data.state.NetworkStateLiveData
import com.tronography.rxmemory.utilities.GlideUtils
import com.tronography.rxmemory.utilities.NetworkConnectivityHelper
import executeInThread
import io.reactivex.Observable
import io.reactivex.Observable.fromCallable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository
@Inject constructor(

        private val liveGameState : GameStateLiveData,

        private val liveNetworkState : NetworkStateLiveData,

        private val cardDao: CardDao,

        private val pokemonDao: PokemonDao,

        private val client: PokeClient,

        private val glideUtils: GlideUtils

) {

    fun insertCard(card: Card) {
        executeInThread { cardDao.insert(card.toMutable()) }
    }

    fun deleteCardTable() {
        executeInThread { cardDao.deleteTable() }
    }

    fun getCards(): LiveData<List<Card>> {
        return cardDao.getAllCards()
    }

    fun updatePokemonAsCaught(id : String, isCaught: Boolean){
        executeInThread { pokemonDao.updateCaught(id, isCaught) }
    }

    fun updatePokemonAsEncountered(id : String, isEncountered: Boolean){
        executeInThread { pokemonDao.updateEncountered(id, isEncountered) }
    }

    fun getLiveGameState(): GameStateLiveData {
        return liveGameState
    }

    fun createNewPokemonDeck() {
        liveGameState.setGameStateLiveData(GameState.LOADING)
        getAllPokemonFromDB()
                .subscribeOn(Schedulers.io())
                .concatMap { pokemonCount ->
                    if (pokemonCount < FULL_DATABASE) {
                        return@concatMap getPokemonFromApi(ONE_FIFTY_ONE, NO_OFFSET)
                    } else
                        return@concatMap getEightPokemonFromDB()
                }
                .concatMap {
                    fromCallable { preloadImages(it) }.observeOn(AndroidSchedulers.mainThread())
                    Observable.just(updateCardDatabase(it))
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Unit>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: Unit) {
                        liveGameState.setGameStateLiveData(GameState.LOAD_COMPLETE)
                    }

                    override fun onError(e: Throwable) {
                        liveGameState.setGameStateLiveData(GameState.ERROR)
                        ERROR("Unable to create a new deck : \n${e.cause} \n${e.message}")
                    }
                })
    }

    fun getPokemonFromApi(limit: Int, offset: Int): Observable<List<Pokemon>> {
        return client.getPokemonList(limit, offset)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { DEBUG("Starting at offset $offset on thread ${Thread.currentThread().name}") }
                .flatMap { namedApiResourceList: NamedApiResourceList ->
                    Observable.fromIterable(namedApiResourceList.results)
                            .subscribeOn(Schedulers.io())
                            .doOnSubscribe {
                                DEBUG("Subscribed to ${namedApiResourceList} on ${Thread.currentThread().getName()}")
                            }
                }
                .flatMap({ namedApiResource: NamedApiResource ->
                    client.getPokemon(namedApiResource.name)
                            .subscribeOn(Schedulers.io())
                }, MAX_CONCURRENCY)
                .doOnNext {
                    DEBUG("Received on ${it.name} ${it.id} on ${Thread.currentThread().getName()}")
                }
                .map { it.toMutable() }
                .toList()
                .toObservable()
                .concatMap { pokemon: List<MutablePokemon> ->
                    fromCallable { updatePokemonDatabase(pokemon) }
                }
                .switchMap { getEightPokemonFromDB() }
                .retry(5)
    }

    private fun getEightPokemonFromDB(): Observable<List<Pokemon>> {
        return pokemonDao.getEightRandomPokemon()
                .toObservable()
                .doOnNext { DEBUG("Dispatching ${it.size} Pokemon from DB...") }
    }

    private fun getAllPokemonFromDB(): Observable<Int> {
        return pokemonDao.getPokemonCount()
                .toObservable()
                .doOnNext { DEBUG("Pokemon DB Count = $it ") }
    }

    private fun preloadImages(cards: List<Pokemon>) {
        cards.forEach { card -> card.sprites.frontDefault?.let { glideUtils.preloadImages(it) } }
    }

    private fun updateCardDatabase(pokemon: List<Pokemon>) {
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

    private fun updatePokemonDatabase(pokemon: List<MutablePokemon>) {
        pokemonDao.insertAll(pokemon)
        DEBUG("Inserted ${pokemon.size} Pokemon into DB")
    }

    companion object {
        private const val ONE_FIFTY_ONE = 151
        private const val NO_OFFSET = 0
        private const val FULL_DATABASE = 151
        private const val MAX_CONCURRENCY = 100
    }
}
