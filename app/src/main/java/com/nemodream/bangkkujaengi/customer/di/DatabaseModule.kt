package com.nemodream.bangkkujaengi.customer.di

import android.content.Context
import androidx.room.Room
import com.nemodream.bangkkujaengi.customer.data.local.dao.SearchHistoryDao
import com.nemodream.bangkkujaengi.customer.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideSearchHistoryDao(database: AppDatabase): SearchHistoryDao {
        return database.searchHistoryDao()
    }
}