package com.tronography.rxmemory.data.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
data class Pokemon(

        @field:SerializedName("resource_uri")
        val resourceUri: String,

        @field:SerializedName("name")
        val name: String

) : Parcelable