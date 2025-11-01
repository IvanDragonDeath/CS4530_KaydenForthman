package com.example.assignment4.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Your FunFact entity
@Database(entities = [FunFact::class], version = 2, exportSchema = false)
abstract class FunFactDatabase : RoomDatabase() {
    abstract fun funFactDao(): FunFactDao

    companion object {
        @Volatile
        private var INSTANCE: FunFactDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE funfacts ADD COLUMN source_url TEXT")
            }
        }

        fun getDatabase(context: Context): FunFactDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FunFactDatabase::class.java,
                    "funfact_db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
