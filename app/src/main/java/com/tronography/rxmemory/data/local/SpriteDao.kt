package com.tronography.rxmemory.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.IGNORE
import android.arch.persistence.room.Query
import com.tronography.rxmemory.data.model.Sprite
import io.reactivex.Completable
import io.reactivex.Single


@Dao
interface SpriteDao {

    @Query("SELECT * FROM ${AppDatabase.SPRITE_TABLE}")
    fun getAllSprites(): Single<List<Sprite>>

    @Query("SELECT * FROM ${AppDatabase.SPRITE_TABLE} ORDER BY RANDOM() LIMIT 8")
    fun getEightRandomSprites(): Single<List<Sprite>>

    @Query("SELECT * FROM ${AppDatabase.SPRITE_TABLE} WHERE id = :id")
    fun getSpriteById(id: String): Single<Sprite>

    @Query("DELETE FROM ${AppDatabase.SPRITE_TABLE}")
    fun deleteTable()

    @Delete
    fun delete(sprite: MutableSprite)

    @Insert(onConflict = IGNORE)
    fun insert(sprite: MutableSprite)

}