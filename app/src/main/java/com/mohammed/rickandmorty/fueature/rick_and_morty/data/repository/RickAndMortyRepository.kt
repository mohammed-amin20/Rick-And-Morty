package com.mohammed.rickandmorty.fueature.rick_and_morty.data.repository

import com.google.gson.JsonParseException
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.local.db.RickAndMortyDB
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.local.db.entity.Character
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.data_source.remote.rick_and_morty_api.RickAndMortyAPI
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.mappers.toEntity
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.util.Error
import com.mohammed.rickandmorty.fueature.rick_and_morty.data.util.Result
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RickAndMortyRepository @Inject constructor(
    private val db: RickAndMortyDB,
    private val api: RickAndMortyAPI
) {
    suspend fun getCharactersByName(
        page: Int,
        name: String,
    ): Result<List<Character>, Error> {
        return when (val response = safeApiCall { api.getCharactersByName(name, page) }) {
            is Result.Failure -> {
                val cached = db.characterDao.getCharactersByName(name, page)
                if (cached.isNotEmpty()) Result.Success(cached) else Result.Failure(response.error)
            }
            is Result.Success -> {
                val charactersApi = response.data
                if (charactersApi.error != null) {
                    val cached = db.characterDao.getCharactersByName(name, page)
                    if (cached.isNotEmpty()) {
                        Result.Success(cached)
                    } else {
                        Result.Failure(Error.Remote.NotFound)
                    }
                } else {
                    val entities = charactersApi.results.map { it.toEntity(page) }
                    entities.forEach { db.characterDao.upsertCharacter(it) }
                    Result.Success(entities)
                }
            }
        }
    }

    suspend fun getCharacter(id: Int): Result<Character, Error> {
        db.characterDao.getCharacterById(id)?.let { return Result.Success(it) }

        return when (val response = safeApiCall { api.getCharacter(id) }) {
            is Result.Failure -> Result.Failure(response.error)
            is Result.Success -> {
                val entity = response.data.toEntity(page = 0)
                db.characterDao.upsertCharacter(entity)
                Result.Success(entity)
            }
        }
    }

    private suspend fun <T> safeApiCall(block: suspend () -> T): Result<T, Error> {
        return try {
            Result.Success(block())
        } catch (_: IOException) {
            Result.Failure(Error.Remote.NoInternet)
        } catch (e: HttpException) {
            Result.Failure(
                when (e.code()) {
                    400 -> Error.Remote.BadRequest
                    401 -> Error.Remote.Unauthorized
                    404 -> Error.Remote.NotFound
                    408 -> Error.Remote.Timeout
                    429 -> Error.Remote.TooManyRequests
                    in 500..599 -> Error.Remote.ServerError
                    else -> Error.Remote.Unknown(e.localizedMessage ?: "something went wrong")
                }
            )
        } catch (_: JsonParseException) {
            Result.Failure(Error.Remote.JsonParseError)
        } catch (e: Exception) {
            Result.Failure(Error.Remote.Unknown(e.localizedMessage ?: "something went wrong"))
        }
    }
}
