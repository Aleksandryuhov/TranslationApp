package com.translator.app

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.translator.app.databinding.FragmentMainBinding
import com.translator.app.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.translationResult.observe(viewLifecycleOwner) { result ->
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

                // Update favorite icon based on whether it\'s in favorites (dummy for now)
                binding.btnFavorite.setImageResource(R.drawable.ic_favorite_border)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.btnTranslate.isEnabled = !isLoading
            binding.tilInputText.isEnabled = !isLoading
            // Show/hide progress indicator if you have one
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }

        viewModel.sourceLang.observe(viewLifecycleOwner) { langCode ->
            binding.tvSourceLanguage.text = viewModel.getSourceLanguageName()
        }

        viewModel.targetLang.observe(viewLifecycleOwner) { langCode ->
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
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Translated Text", binding.tvTranslationResult.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), R.string.copied, Toast.LENGTH_SHORT).show()
        }

        binding.btnFavorite.setOnClickListener {
            // TODO: Implement add/remove from favorites logic
            Toast.makeText(requireContext(), "Favorite functionality coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

