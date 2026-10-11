package com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.local.db.entity.Character

@Dao
interface CharacterDao {

    @Query("Select * from Character Where name LIKE '%'||:name||'%' AND page = :page")
    suspend fun getCharactersByName(name : String, page: Int) : List<Character>

    @Query("Select * from Character Where id = :id")
    suspend fun getCharacterById(id: Int) : Character?

    @Upsert
    suspend fun upsertCharacter(character : Character )
}
