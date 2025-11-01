package com.example.assignment4

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.assignment4.data.FunFact
import com.example.assignment4.data.FunFactRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FunFactViewModel(private val repository: FunFactRepository) : ViewModel() {

    val funFacts: StateFlow<List<FunFact>> = repository.getAllFacts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun fetchFunFact() {
        viewModelScope.launch { repository.fetchAndSaveFunFact() }
    }
}

class FunFactViewModelFactory(private val repository: FunFactRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FunFactViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FunFactViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
