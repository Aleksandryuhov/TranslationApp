package com.translator.app.repository

import com.translator.app.data.TranslationResponse
import com.translator.app.network.TranslationService

class TranslationRepository {
    
    private val translationService = TranslationService()
    
    suspend fun translate(
        text: String,
        sourceLang: String,
        targetLang: String
    ): Result<TranslationResponse> {
        return translationService.translate(text, sourceLang, targetLang)
    }
    
    fun getLanguageCode(language: String): String {
        return when (language.lowercase()) {
            "русский", "russian" -> "ru"
            "английский", "english" -> "en"
            else -> "en"
        }
    }
    
    fun getLanguageName(code: String): String {
        return when (code) {
            "ru" -> "Русский"
            "en" -> "Английский"
            else -> "Английский"
        }
    }
}

