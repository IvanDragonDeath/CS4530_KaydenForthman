package com.example.assignment4.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FunFactDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(fact: FunFact)

    @Query("SELECT * FROM funfacts ORDER BY id DESC")
    fun getAllFacts(): Flow<List<FunFact>>
}