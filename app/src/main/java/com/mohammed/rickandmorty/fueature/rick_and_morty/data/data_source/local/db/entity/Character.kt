package com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Character(
    @PrimaryKey
    val id: Int,
    val name: String,
    val status: String,
    val location: String,
    val origin: String,
    val image: String,
    val species: String,
    val page: Int,
    val gender: String = "",
    val type: String = "",
    val originUrl: String = "",
    val locationUrl: String = "",
    val created: String = "",
    val url: String = "",
    val episode: List<Int> = emptyList()
)
