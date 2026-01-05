package com.raffimaulana259xixixix.keuangan.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room Database class untuk aplikasi Money Manager
 * Singleton pattern untuk memastikan hanya ada satu instance database
 */
@Database(entities = [Transaction::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Mendapatkan instance TransactionDao
     */
    abstract fun transactionDao(): TransactionDao

    companion object {
        // Singleton instance
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Mendapatkan instance database (singleton)
         * Thread-safe menggunakan synchronized block
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "money_manager_database"
                )
                    .fallbackToDestructiveMigration() // Menghapus data lama jika ada perubahan struktur
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

