package com.shopverse.core.data.cart.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CartItemEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class ShopVerseDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao

    companion object {
        private const val DB_NAME = "shopverse.db"

        fun build(context: Context): ShopVerseDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                ShopVerseDatabase::class.java,
                DB_NAME,
            ).build()
    }
}
