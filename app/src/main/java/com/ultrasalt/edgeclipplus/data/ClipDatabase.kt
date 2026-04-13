package com.ultrasalt.edgeclipplus.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ClipEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ClipDatabase : RoomDatabase() {

    abstract fun clipDao(): ClipDao

    companion object {
        @Volatile private var INSTANCE: ClipDatabase? = null

        fun init(context: Context) {
            if (INSTANCE != null) return

            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                ClipDatabase::class.java,
                "edgeclip_db"
            ).build()
        }

        fun get(): ClipDatabase {
            return INSTANCE ?: throw IllegalStateException("ClipDatabase not initialized")
        }
    }
}
