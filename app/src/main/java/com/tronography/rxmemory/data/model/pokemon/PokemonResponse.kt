package com.tronography.rxmemory.data.model.pokemon

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class PokemonResponse(

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

) : Parcelable

