package com.example.assignment4.data

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow

class FunFactRepository private constructor(
    private val dao: FunFactDao,
    private val client: HttpClient
) {
    fun getAllFacts(): Flow<List<FunFact>> = dao.getAllFacts()

    suspend fun fetchAndSaveFunFact() {
        try {
            val response: FunFactNetwork = client
                .get("https://uselessfacts.jsph.pl//api/v2/facts/random")
                .body()
            val entity = FunFact(text = response.text, source_url = response.source_url)
            dao.insert(entity)
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching FunFact", e)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: FunFactRepository? = null

        fun getInstance(dao: FunFactDao, client: HttpClient) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FunFactRepository(dao, client).also { INSTANCE = it }
            }
    }
}
