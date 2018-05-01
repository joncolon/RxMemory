package com.tronography.rxmemory.data.species

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Color(

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("url")
        val url: String? = null
) : Parcelable