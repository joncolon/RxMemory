package com.tronography.rxmemory.data.local

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.tronography.rxmemory.data.model.pokemon.Pokemon
import com.tronography.rxmemory.data.model.pokemon.Species
import com.tronography.rxmemory.data.model.pokemon.Sprites
import com.tronography.rxmemory.data.model.pokemon.TypesItem

@Entity(tableName = AppDatabase.POKEMON_TABLE)
class MutablePokemon(

        var types: List<TypesItem>,

        var weight: Int,

        var sprites: Sprites,

        var species: Species,

        var name: String,

        @PrimaryKey
        var id: Int,

        var height: Int,

        var caught: Boolean,

        var encountered: Boolean
) {

    fun toImmutable(): Pokemon {
        return Pokemon(
                types,
                weight,
                sprites,
                species,
                name,
                id,
                height,
                caught,
                encountered
        )
    }

}