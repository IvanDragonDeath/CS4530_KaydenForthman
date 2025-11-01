package com.example.assignment4.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "funfacts")
data class FunFact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val source_url: String? = null
)

@Serializable
data class FunFactNetwork(
    val text: String,
    val source_url: String? = null
)
