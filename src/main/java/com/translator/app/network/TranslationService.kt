package com.translator.app.network

import com.translator.app.data.TranslationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class TranslationService {

    companion object {
        private const val BASE_URL = "https://api.mymemory.translated.net/get"
    }

    suspend fun translate(
        text: String,
        sourceLang: String,
        targetLang: String
    ): Result<TranslationResponse> = withContext(Dispatchers.IO) {
        try {
            val encodedText = URLEncoder.encode(text, "UTF-8")
            val langPair = "${sourceLang}|${targetLang}"
            val urlString = "$BASE_URL?q=$encodedText&langpair=$langPair"
            
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = reader.readText()
                reader.close()
                
                val jsonResponse = JSONObject(response)
                val responseData = jsonResponse.getJSONObject("responseData")
                val translatedText = responseData.getString("translatedText")
                
                // Generate example sentence
                val example = generateExample(text, translatedText, sourceLang, targetLang)
                
                // Generate transcription for English words
                val transcription = if (targetLang == "en" || sourceLang == "en") {
                    generateTranscription(if (targetLang == "en") translatedText else text)
                } else null
                
                Result.success(
                    TranslationResponse(
                        translatedText = translatedText,
                        transcription = transcription,
                        example = example,
                        originalText = text,
                        sourceLang = sourceLang,
                        targetLang = targetLang
                    )
                )
            } else {
                Result.failure(Exception("HTTP Error: $responseCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun generateExample(
        originalText: String,
        translatedText: String,
        sourceLang: String,
        targetLang: String
    ): String {
        return when {
            sourceLang == "ru" && targetLang == "en" -> {
                "Пример: \"$originalText\" - \"$translatedText\""
            }
            sourceLang == "en" && targetLang == "ru" -> {
                "Example: \"$originalText\" - \"$translatedText\""
            }
            else -> "\"$originalText\" → \"$translatedText\""
        }
    }
    
    private fun generateTranscription(englishText: String): String? {
        // Simplified transcription for common words
        val transcriptions = mapOf(
            "hello" to "[həˈloʊ]",
            "world" to "[wɜːrld]",
            "good" to "[ɡʊd]",
            "morning" to "[ˈmɔːrnɪŋ]",
            "evening" to "[ˈiːvnɪŋ]",
            "night" to "[naɪt]",
            "day" to "[deɪ]",
            "time" to "[taɪm]",
            "water" to "[ˈwɔːtər]",
            "food" to "[fuːd]",
            "house" to "[haʊs]",
            "car" to "[kɑːr]",
            "book" to "[bʊk]",
            "computer" to "[kəmˈpjuːtər]",
            "phone" to "[foʊn]",
            "work" to "[wɜːrk]",
            "home" to "[hoʊm]",
            "family" to "[ˈfæməli]",
            "friend" to "[frend]",
            "love" to "[lʌv]"
        )
        
        val word = englishText.lowercase().trim()
        return transcriptions[word] ?: "[${word}]"
    }
}

