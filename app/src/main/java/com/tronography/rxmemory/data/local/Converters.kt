package com.tronography.rxmemory.data.local

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tronography.rxmemory.data.model.Pokemon
import java.lang.reflect.Type

/**
 * Created by jonat on 4/13/2018.
 */

class Converters {

    @TypeConverter
    fun fromPokemonToString(pokemon: Pokemon): String {
        val gson = Gson()
        return gson.toJson(pokemon)
    }

    @TypeConverter
    fun fromStringtoPokemon(value: String): Pokemon {
        val type: Type = object : TypeToken<Pokemon>() {}.type
        return Gson().fromJson(value, type)
    }
}