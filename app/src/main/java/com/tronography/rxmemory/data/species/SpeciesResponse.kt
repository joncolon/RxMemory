package com.tronography.rxmemory.data.species

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tronography.rxmemory.data.model.common.NamedApiResource
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SpeciesResponse(

        @field:SerializedName("habitat")
        val habitat: NamedApiResource,

        @field:SerializedName("color")
        val color: Color,

        @field:SerializedName("flavor_text_entries")
        val flavorTextEntries: List<FlavorTextEntries>,

        @field:SerializedName("id")
        val id: Int,

        @field:SerializedName("name")
        val name: String
) : Parcelable