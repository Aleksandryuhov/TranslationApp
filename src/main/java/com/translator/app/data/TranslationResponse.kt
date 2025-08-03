package com.translator.app.data

data class TranslationResponse(
    val translatedText: String,
    val transcription: String? = null,
    val example: String? = null,
    val originalText: String,
    val sourceLang: String,
    val targetLang: String
)

