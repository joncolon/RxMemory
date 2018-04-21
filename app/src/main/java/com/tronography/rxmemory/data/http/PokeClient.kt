package com.tronography.rxmemory.data.http

import com.tronography.rxmemory.data.model.SpriteResponse
import io.reactivex.Single
import retrofit2.http.GET


interface PokeClient {

    @GET(HttpConstants.SPRITES_PATH)
    fun getSprites(): Single<SpriteResponse>

}

