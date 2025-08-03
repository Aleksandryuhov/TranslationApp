package com.translator.app

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.translator.app.databinding.ActivityMainBinding
import com.translator.app.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setupObservers()
        setupListeners()
        setupBottomNavigation()
    }

    private fun setupObservers() {
        viewModel.translationResult.observe(this) { result ->
            result?.let {
                binding.cardTranslationResult.visibility = View.VISIBLE
                binding.tvTranslationResult.text = it.translatedText

                if (!it.transcription.isNullOrBlank()) {
                    binding.layoutPronunciation.visibility = View.VISIBLE
                    binding.tvPronunciation.text = it.transcription
                } else {
                    binding.layoutPronunciation.visibility = View.GONE
                }

                if (!it.example.isNullOrBlank()) {
                    binding.layoutExample.visibility = View.VISIBLE
                    binding.tvExample.text = it.example
                } else {
                    binding.layoutExample.visibility = View.GONE
                }

                // Update favorite icon based on whether it's in favorites (dummy for now)
                binding.btnFavorite.setImageResource(R.drawable.ic_favorite_border)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.btnTranslate.isEnabled = !isLoading
            binding.tilInputText.isEnabled = !isLoading
            // Show/hide progress indicator if you have one
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }

        viewModel.sourceLang.observe(this) { langCode ->
            binding.tvSourceLanguage.text = viewModel.getSourceLanguageName()
        }

        viewModel.targetLang.observe(this) { langCode ->
            binding.tvTargetLanguage.text = viewModel.getTargetLanguageName()
        }
    }

    private fun setupListeners() {
        binding.btnTranslate.setOnClickListener {
            val textToTranslate = binding.etInputText.text.toString()
            viewModel.translate(textToTranslate)
        }

        binding.btnSwapLanguages.setOnClickListener {
            viewModel.swapLanguages()
            // Clear input and output when languages are swapped
            binding.etInputText.text?.clear()
            binding.cardTranslationResult.visibility = View.GONE
        }

        binding.btnCopyTranslation.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Translated Text", binding.tvTranslationResult.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, R.string.copied, Toast.LENGTH_SHORT).show()
        }

        binding.btnFavorite.setOnClickListener {
            // TODO: Implement add/remove from favorites logic
            Toast.makeText(this, "Favorite functionality coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_translate -> {
                    // Already on translate screen, do nothing or reset
                    true
                }
                R.id.nav_history -> {
                    // TODO: Navigate to History Fragment
                    Toast.makeText(this, "History clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_favorites -> {
                    // TODO: Navigate to Favorites Fragment
                    Toast.makeText(this, "Favorites clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}

