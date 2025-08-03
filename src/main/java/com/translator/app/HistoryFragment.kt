package com.translator.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.translator.app.adapter.TranslationHistoryAdapter
import com.translator.app.data.TranslationHistory
import com.translator.app.databinding.FragmentHistoryBinding
import com.translator.app.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var historyAdapter: TranslationHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        historyAdapter = TranslationHistoryAdapter(
            onFavoriteClick = { translation ->
                historyViewModel.toggleFavorite(translation)
            },
            onDeleteClick = { translation ->
                historyViewModel.deleteTranslation(translation)
            },
            onItemClick = { translation ->
                // TODO: Handle item click, e.g., show details or re-translate
            }
        )
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }
    }

    private fun setupObservers() {
        historyViewModel.allHistory.observe(viewLifecycleOwner) { historyList ->
            historyList?.let {
                historyAdapter.submitList(it)
                binding.tvNoHistory.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

