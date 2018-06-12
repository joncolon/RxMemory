package com.tronography.rxmemory.data.local

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tronography.rxmemory.data.model.pokemon.Sprites
import com.tronography.rxmemory.data.model.pokemon.TypesItem
import java.lang.reflect.Type


class Converters {

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