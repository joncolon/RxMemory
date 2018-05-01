package com.tronography.rxmemory.data.local

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tronography.rxmemory.data.model.pokemon.Pokemon
import com.tronography.rxmemory.data.model.pokemon.Species
import com.tronography.rxmemory.data.model.pokemon.Sprites
import com.tronography.rxmemory.data.model.pokemon.TypesItem
import java.lang.reflect.Type


class Converters {

    @TypeConverter
    fun fromPokemonToString(pokemon: Pokemon): String {
        val gson = Gson()
        return gson.toJson(pokemon)
    }

    @TypeConverter
    fun fromStringToPokemon(value: String): Pokemon {
        val type: Type = object : TypeToken<Pokemon>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromTypesToString(types: List<TypesItem>): String {
        val gson = Gson()
        return gson.toJson(types)
    }

    @TypeConverter
    fun fromStringToTypes(value: String): List<TypesItem> {
        val type: Type = object : TypeToken<ArrayList<TypesItem>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromSpeciesToString(species: Species): String {
        val gson = Gson()
        return gson.toJson(species)
    }

    @TypeConverter
    fun fromStringToSpecies(value: String): Species {
        val type: Type = object : TypeToken<Species>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromSpritesToString(sprites: Sprites): String {
        val gson = Gson()
        return gson.toJson(sprites)
    }

    @TypeConverter
    fun fromStringToSprites(value: String): Sprites {
        val type: Type = object : TypeToken<Sprites>() {}.type
        return Gson().fromJson(value, type)
    }



}