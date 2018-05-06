package com.tronography.rxmemory.data.model.pokemon.forms

import android.annotation.SuppressLint
import android.arch.persistence.room.Entity
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tronography.rxmemory.data.local.AppDatabase
import com.tronography.rxmemory.data.local.MutablePokemonForm
import com.tronography.rxmemory.data.model.pokemon.Sprites
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class PokemonForm(

        @field:SerializedName("id")
        val id: Int,

        @field:SerializedName("sprites")
        val sprites: Sprites,

        @field:SerializedName("name")
        val name: String,

        val caught: Boolean = false,

        val encountered: Boolean = false
) : Parcelable {

    fun toMutable(): MutablePokemonForm {
        return MutablePokemonForm(
                id,
                sprites,
                name,
                caught,
                encountered
        )
    }
}