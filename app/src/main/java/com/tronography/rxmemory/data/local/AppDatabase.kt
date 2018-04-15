package com.tronography.rxmemory.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters

@Database(
        entities = arrayOf(MutableCard::class, MutableSprite::class),
        version = 1,
        exportSchema = false
)
@TypeConverters(value = arrayOf(Converters::class))
abstract class AppDatabase : RoomDatabase() {

    abstract fun cardDao(): CardDao
    abstract fun spriteDao(): SpriteDao

    companion object {
        const val FILENAME: String = "AppDatabase.db"
        const val CARD_TABLE = "cards"
        const val SPRITE_TABLE = "sprites"
    }

}