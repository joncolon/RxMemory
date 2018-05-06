package com.tronography.rxmemory.data.model.pokemon

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tronography.rxmemory.data.local.MutablePokemon
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Pokemon(

        @field:SerializedName("types")
        val types: List<TypesItem>,

        @field:SerializedName("weight")
        val weight: Int,

        @field:SerializedName("sprites")
        val sprites: Sprites,

        @field:SerializedName("species")
        val species: Species,

        @field:SerializedName("name")
        val name: String,

        @field:SerializedName("id")
        val id: Int,

        @field:SerializedName("height")
        val height: Int,

        val caught: Boolean = false,

        val encountered: Boolean = false

) : Parcelable {
    fun toMutable(): MutablePokemon = MutablePokemon(
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

