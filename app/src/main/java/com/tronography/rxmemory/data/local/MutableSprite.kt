package com.tronography.rxmemory.data.local

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.tronography.rxmemory.data.model.Pokemon
import com.tronography.rxmemory.data.model.Sprite

@Entity(tableName = AppDatabase.SPRITE_TABLE)
data class MutableSprite(

        var image: String,

        var pokemon: Pokemon,

        var created: String,

        var resourceUri: String,

        var name: String,

        var modified: String,

        @PrimaryKey
        var id: Int
) {

    fun toImmutable(): Sprite {
        return Sprite(image, pokemon, created, resourceUri, name, modified, id)
    }
}