package com.translator.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.translator.app.data.TranslationResponse
import com.translator.app.repository.TranslationRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    
    private val repository = TranslationRepository()
    
    private val _translationResult = MutableLiveData<TranslationResponse?>()
    val translationResult: LiveData<TranslationResponse?> = _translationResult
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    private val _sourceLang = MutableLiveData<String>()
    val sourceLang: LiveData<String> = _sourceLang
    
    private val _targetLang = MutableLiveData<String>()
    val targetLang: LiveData<String> = _targetLang
    
    init {
        _sourceLang.value = "ru"
        _targetLang.value = "en"
    }
    
    fun translate(text: String) {
        if (text.isBlank()) {
            _error.value = "Введите текст для перевода"
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            val result = repository.translate(
                text = text.trim(),
                sourceLang = _sourceLang.value ?: "ru",
                targetLang = _targetLang.value ?: "en"
            )
            
            result.fold(
                onSuccess = { response ->
                    _translationResult.value = response
                },
                onFailure = { exception ->
                    _error.value = "Ошибка перевода: ${exception.message}"
                }
            )
            
            _isLoading.value = false
        }
    }
    
    fun swapLanguages() {
        val currentSource = _sourceLang.value
        val currentTarget = _targetLang.value
        
        _sourceLang.value = currentTarget
        _targetLang.value = currentSource
        
        // Clear previous translation result
        _translationResult.value = null
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun getSourceLanguageName(): String {
        return repository.getLanguageName(_sourceLang.value ?: "ru")
    }
    
    fun getTargetLanguageName(): String {
        return repository.getLanguageName(_targetLang.value ?: "en")
    }
}

