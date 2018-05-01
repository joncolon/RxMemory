package com.tronography.rxmemory.data.model.pokemon

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Type(

        @field:SerializedName("name")
        val name: String,

        @field:SerializedName("url")
        val url: String
) : Parcelable