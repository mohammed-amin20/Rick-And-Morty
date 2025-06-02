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
    private val api: RickAndMortyAPI,
) {
    suspend fun getCharactersByName(
        page: Int,
        name: String,
    ): Result<List<Character>, Error> {
        val charactersDB = db.characterDao.getCharactersByName(name, page)
        return if (charactersDB.isEmpty()) {
            try {
                val charactersApi = api.getCharactersByName(name, page)
                if (charactersApi.error != null) {
                    Result.Failure(Error.Remote.Unknown(charactersApi.error))
                } else {
                    charactersApi.results.forEach {
                        db.characterDao.upsertCharacter(it.toEntity(page))
                    }
                    Result.Success((charactersApi.results.map { it.toEntity(page) }))
                }
            } catch (_: IOException) {
                Result.Failure(Error.Remote.NoInternet)
            } catch (e: HttpException) {
                val code = e.code()
                when (code) {
                    400 -> Result.Failure(Error.Remote.BadRequest)
                    401 -> Result.Failure(Error.Remote.Unauthorized)
                    404 -> Result.Failure(Error.Remote.NotFound)
                    408 -> Result.Failure(Error.Remote.Timeout)
                    429 -> Result.Failure(Error.Remote.TooManyRequests)
                    in 500..599 -> Result.Failure(Error.Remote.ServerError)
                    else -> Result.Failure(
                        Error.Remote.Unknown(
                            e.localizedMessage ?: "something went wrong"
                        )
                    )
                }
            } catch (_: JsonParseException) {
                Result.Failure(Error.Remote.JsonParseError)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Failure(Error.Remote.Unknown(e.localizedMessage ?: "something went wrong"))
            }
        } else {
            Result.Success((charactersDB))
        }
    }
}