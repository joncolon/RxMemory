package com.tronography.rxmemory.data.model.species

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Language(

        @field:SerializedName("name")
        val name: String,

        @field:SerializedName("url")
        val url: String
) : Parcelable