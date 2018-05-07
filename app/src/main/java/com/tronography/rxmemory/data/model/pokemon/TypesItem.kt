package com.tronography.rxmemory.data.model.pokemon

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class TypesItem(

        @field:SerializedName("slot")
        val slot: Int,

        @field:SerializedName("type")
        val type: Type

) : Parcelable