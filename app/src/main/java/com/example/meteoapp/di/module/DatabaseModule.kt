package com.example.meteoapp.di.module

import android.app.Application
import androidx.room.Room
import com.example.meteoapp.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    private lateinit var database: AppDatabase

    @Singleton
    @Provides
    fun providesDatabase(application: Application): AppDatabase {
        database = Room.databaseBuilder(application, AppDatabase::class.java, "meteo-db").build()
        return database
    }

    @Singleton
    @Provides
    fun providesCityDao(database: AppDatabase?) = database?.cityDao()
}
