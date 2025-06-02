package com.mohammed.rickandmorty.fueature.rick_and_morty.di

import android.app.Application
import androidx.room.Room
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.local.db.RickAndMortyDB
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.remote.rick_and_morty_api.RickAndMortyAPI
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.repository.RickAndMortyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RickAndMortyModule {

    @Provides
    @Singleton
    fun provideRickAndMortyAPI() : RickAndMortyAPI{
        return Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                    ).build()
            )
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideRickAndMortyDB(app : Application) :RickAndMortyDB{
        return Room.databaseBuilder(
            app,
            RickAndMortyDB::class.java,
            RickAndMortyDB.DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideRickAndMortyRepository(db : RickAndMortyDB , api : RickAndMortyAPI) : RickAndMortyRepository{
        return RickAndMortyRepository(db , api)
    }
}