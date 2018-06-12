package com.tronography.rxmemory.data.local

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.tronography.rxmemory.data.model.pokemon.PokemonData
import com.tronography.rxmemory.data.model.pokemon.Sprites
import com.tronography.rxmemory.data.model.pokemon.TypesItem

@Entity(tableName = AppDatabase.POKEMON_TABLE)
class MutablePokemonData(

        var types: List<TypesItem>,

        var weight: Int,

        var sprites: Sprites,

        var habitat: String,

        var description: String,

        var name: String,

        @PrimaryKey
        var id: Int,

        var height: Int,

        var caught: Boolean) {

    fun toImmutable(): PokemonData {
        return PokemonData(
                types,
                weight,
                sprites,
                habitat,
                description,
                name,
                id,
                height,
                caught
        )
    }

}