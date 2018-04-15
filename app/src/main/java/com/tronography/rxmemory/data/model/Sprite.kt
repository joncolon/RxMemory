package com.tronography.rxmemory.data.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tronography.rxmemory.data.local.MutableSprite
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@SuppressLint("ParcelCreator")
@Parcelize
data class Sprite(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("pokemon")
	val pokemon: Pokemon,

	@field:SerializedName("created")
	val created: String,

	@field:SerializedName("resource_uri")
	val resourceUri: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("modified")
	val modified: String,

	@field:SerializedName("id")
	val id: Int

) : Parcelable {

    fun toMutable(): MutableSprite {
        return MutableSprite(image, pokemon, created, resourceUri, name, modified, id)
    }
}