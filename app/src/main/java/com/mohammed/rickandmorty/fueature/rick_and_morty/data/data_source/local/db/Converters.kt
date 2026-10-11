package com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.local.db

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromEpisodeList(value: List<Int>): String =
        value.joinToString(separator = ",")

    @TypeConverter
    fun toEpisodeList(value: String): List<Int> =
        if (value.isBlank()) {
            emptyList()
        } else {
            value.split(",").mapNotNull { it.trim().toIntOrNull() }
        }
}
