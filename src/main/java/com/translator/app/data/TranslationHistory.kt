package com.translator.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translation_history")
data class TranslationHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val originalText: String,
    val translatedText: String,
    val sourceLang: String,
    val targetLang: String,
    val transcription: String?,
    val example: String?,
    val timestamp: Long = System.currentTimeMillis(),
    var isFavorite: Boolean = false
)


