package com.tronography.rxmemory.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.tronography.rxmemory.data.model.pokemon.PokemonData
import io.reactivex.Single


@Dao
abstract class PokemonDao {

    @Query("SELECT * FROM ${AppDatabase.POKEMON_TABLE}")
    abstract fun getAllPokemon(): LiveData<List<PokemonData>>

    @Query("SELECT * FROM ${AppDatabase.POKEMON_TABLE} WHERE caught = :caught")
    abstract fun getCaughtPokemon(caught: Boolean): LiveData<List<PokemonData>>

    @Query("SELECT COUNT(*) FROM ${AppDatabase.POKEMON_TABLE}")
    abstract fun getPokemonCount(): Single<Int>

    @Query("SELECT COUNT(*) FROM ${AppDatabase.POKEMON_TABLE}")
    abstract fun getLivePokemonCount(): LiveData<Int>

    @Query("SELECT * FROM ${AppDatabase.POKEMON_TABLE} ORDER BY RANDOM() LIMIT 8")
    abstract fun getEightRandomPokemon(): Single<List<PokemonData>>

    @Query("SELECT * FROM ${AppDatabase.POKEMON_TABLE} WHERE id = :id")
    abstract fun getPokemonById(id: Int): Single<PokemonData>

    @Query("DELETE FROM ${AppDatabase.POKEMON_TABLE}")
    abstract fun deleteTable()

    @Query("UPDATE ${AppDatabase.POKEMON_TABLE} SET caught = :caught  WHERE id = :id")
    abstract fun updateCaught(id: Int, caught: Boolean)

    @Delete
    abstract fun delete(pokemon: MutablePokemonData)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(pokemon: MutablePokemonData)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertAll(pokemon: List<MutablePokemonData>)

}