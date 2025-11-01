package com.example.assignment4

import android.app.Application
import com.example.assignment4.data.FunFactDatabase
import com.example.assignment4.data.FunFactRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class Assignment4App : Application() {

    lateinit var repository: FunFactRepository
        private set

    override fun onCreate() {
        super.onCreate()

        val dao = FunFactDatabase.getDatabase(this).funFactDao()

        val client = HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true; isLenient = true })
            }
        }

        repository = FunFactRepository.getInstance(dao, client)
    }
}
