package com.tronography.rxmemory.data.model.common

import com.google.gson.annotations.SerializedName

data class NamedApiResourceList(

        @field:SerializedName("next")
        val next: String? = null,

        @field:SerializedName("previous")
        val previous: String? = null,

        @field:SerializedName("count")
        val count: Int,

        @field:SerializedName("results")
        val results: List<NamedApiResource>
)