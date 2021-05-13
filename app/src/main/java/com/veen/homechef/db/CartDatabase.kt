package com.veen.homechef.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [AddToCart::class],
    version = 1
)
abstract class CartDatabase : RoomDatabase(){
    abstract fun getNoteDao(): DatabaseDao

    companion object {
        @Volatile private var instance: CartDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context.applicationContext,
            CartDatabase::class.java,
            "myfirstdatabase"
        ).allowMainThreadQueries().build()
    }
}