package com.nemodream.bangkkujaengi.customer.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nemodream.bangkkujaengi.customer.data.local.dao.SearchHistoryDao
import com.nemodream.bangkkujaengi.customer.data.model.SearchHistory

@Database(entities = [SearchHistory::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
}