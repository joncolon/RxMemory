package com.tronography.rxmemory.data.model

import com.google.gson.annotations.SerializedName

data class SpriteResponse(

        @field:SerializedName("meta")
        val meta: Meta,

        @field:SerializedName("objects")
        val sprites: List<Sprite>

)