package com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.local.db.dao.CharacterDao
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.local.db.entity.Character

@Database(
    entities = [Character::class],
    version = RickAndMortyDB.DB_VERSION
)
abstract class RickAndMortyDB : RoomDatabase(){

    abstract val characterDao : CharacterDao

    companion object{
        const val DB_NAME = "RickAndMorty"
        const val DB_VERSION = 1
    }
}