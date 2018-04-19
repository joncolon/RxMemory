package com.tronography.rxmemory.data.model

import com.google.gson.annotations.SerializedName


data class Meta(

        @field:SerializedName("next")
        val next: String,

        @field:SerializedName("offset")
        val offset: Int,

        @field:SerializedName("previous")
        val previous: Any,

        @field:SerializedName("total_count")
        val totalCount: Int,

        @field:SerializedName("limit")
        val limit: Int
)