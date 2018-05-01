package com.tronography.rxmemory.data.species

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlavorTextEntries(

        @field:SerializedName("language")
        val language: Language,

        @field:SerializedName("flavor_text")
        val flavorText: String
) : Parcelable