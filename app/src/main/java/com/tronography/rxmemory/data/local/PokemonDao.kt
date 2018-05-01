package com.tronography.rxmemory.data.local

import android.arch.persistence.room.*
import com.tronography.rxmemory.data.model.pokemon.Pokemon
import io.reactivex.Observable
import io.reactivex.Single


@Dao
abstract class PokemonDao {

    @Query("SELECT * FROM ${AppDatabase.POKEMON_TABLE}")
    abstract fun getAllPokemon(): Single<List<Pokemon>>

    @Query("SELECT COUNT(*) FROM ${AppDatabase.POKEMON_TABLE}")
    abstract fun getPokemonCount(): Single<Int>

    @Query("SELECT * FROM ${AppDatabase.POKEMON_TABLE} ORDER BY RANDOM() LIMIT 8")
    abstract fun getEightRandomPokemon(): Single<List<Pokemon>>

    @Query("SELECT * FROM ${AppDatabase.POKEMON_TABLE} WHERE id = :id")
    abstract fun getPokemonById(id: String): Single<Pokemon>

    @Query("DELETE FROM ${AppDatabase.POKEMON_TABLE}")
    abstract fun deleteTable()

    @Delete
    abstract fun delete(pokemon: MutablePokemon)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(pokemon: MutablePokemon)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertAll(pokemon: List<MutablePokemon>)

}