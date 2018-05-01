package com.tronography.rxmemory.data.model.common

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NamedApiResource(

        @field:SerializedName("name")
        val name: String,

        @field:SerializedName("url")
        val url: String
) : Parcelable