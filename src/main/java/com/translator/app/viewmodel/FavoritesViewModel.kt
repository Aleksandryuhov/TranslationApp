package com.translator.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.translator.app.data.TranslationDatabase
import com.translator.app.data.TranslationHistory
import com.translator.app.repository.DatabaseRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DatabaseRepository
    val favorites: LiveData<List<TranslationHistory>>

    init {
        val translationDao = TranslationDatabase.getDatabase(application).translationDao()
        repository = DatabaseRepository(translationDao)
        favorites = repository.getFavorites()
    }

    fun removeFromFavorites(translation: TranslationHistory) {
        viewModelScope.launch {
            repository.toggleFavorite(translation)
        }
    }

    fun deleteTranslation(translation: TranslationHistory) {
        viewModelScope.launch {
            repository.deleteTranslation(translation)
        }
    }
}

