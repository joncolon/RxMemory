package com.tronography.rxmemory.data.model.pokemon

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tronography.rxmemory.data.local.MutablePokemonData
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class PokemonData(

        @field:SerializedName("types")
        val types: List<TypesItem>,

        @field:SerializedName("weight")
        val weight: Int,

        @field:SerializedName("sprites")
        val sprites: Sprites,

        @field:SerializedName("habitat")
        val habitat: String,

        @field:SerializedName("description")
        val description: String,

        @field:SerializedName("name")
        val name: String,

        @field:SerializedName("id")
        val id: Int,

        @field:SerializedName("height")
        val height: Int,

        val caught: Boolean = false

) : Parcelable {
    fun toMutable(): MutablePokemonData = MutablePokemonData(
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

