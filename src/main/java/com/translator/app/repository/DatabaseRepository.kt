package com.translator.app.repository

import androidx.lifecycle.LiveData
import com.translator.app.data.TranslationDao
import com.translator.app.data.TranslationHistory
import com.translator.app.data.TranslationResponse

class DatabaseRepository(private val translationDao: TranslationDao) {
    
    fun getAllHistory(): LiveData<List<TranslationHistory>> {
        return translationDao.getAllHistory()
    }
    
    fun getFavorites(): LiveData<List<TranslationHistory>> {
        return translationDao.getFavorites()
    }
    
    suspend fun saveTranslation(response: TranslationResponse): Long {
        val translation = TranslationHistory(
            originalText = response.originalText,
            translatedText = response.translatedText,
            sourceLang = response.sourceLang,
            targetLang = response.targetLang,
            transcription = response.transcription,
            example = response.example
        )
        
        // Check if translation already exists
        val existing = translationDao.findTranslation(
            response.originalText,
            response.sourceLang,
            response.targetLang
        )
        
        return if (existing != null) {
            // Update timestamp of existing translation
            val updated = existing.copy(timestamp = System.currentTimeMillis())
            translationDao.updateTranslation(updated)
            existing.id.toLong()
        } else {
            translationDao.insertTranslation(translation)
        }
    }
    
    suspend fun toggleFavorite(translation: TranslationHistory) {
        val updated = translation.copy(isFavorite = !translation.isFavorite)
        translationDao.updateTranslation(updated)
    }
    
    suspend fun deleteTranslation(translation: TranslationHistory) {
        translationDao.deleteTranslation(translation)
    }
    
    suspend fun clearHistory() {
        translationDao.clearHistory()
    }
    
    suspend fun getHistoryCount(): Int {
        return translationDao.getHistoryCount()
    }
    
    suspend fun getFavoritesCount(): Int {
        return translationDao.getFavoritesCount()
    }
}

