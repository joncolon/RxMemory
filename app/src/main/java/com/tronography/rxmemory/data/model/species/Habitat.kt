package com.tronography.rxmemory.data.model.species

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Habitat(

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("url")
        val url: String? = null
) : Parcelable