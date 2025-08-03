package com.translator.app.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TranslationDao {
    
    @Query("SELECT * FROM translation_history ORDER BY timestamp DESC")
    fun getAllHistory(): LiveData<List<TranslationHistory>>
    
    @Query("SELECT * FROM translation_history WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavorites(): LiveData<List<TranslationHistory>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(translation: TranslationHistory): Long
    
    @Update
    suspend fun updateTranslation(translation: TranslationHistory)
    
    @Delete
    suspend fun deleteTranslation(translation: TranslationHistory)
    
    @Query("DELETE FROM translation_history WHERE isFavorite = 0")
    suspend fun clearHistory()
    
    @Query("SELECT * FROM translation_history WHERE originalText = :originalText AND sourceLang = :sourceLang AND targetLang = :targetLang LIMIT 1")
    suspend fun findTranslation(originalText: String, sourceLang: String, targetLang: String): TranslationHistory?
    
    @Query("UPDATE translation_history SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)
    
    @Query("SELECT COUNT(*) FROM translation_history")
    suspend fun getHistoryCount(): Int
    
    @Query("SELECT COUNT(*) FROM translation_history WHERE isFavorite = 1")
    suspend fun getFavoritesCount(): Int
}

