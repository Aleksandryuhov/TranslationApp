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
import com.translator.app.databinding.FragmentFavoritesBinding
import com.translator.app.viewmodel.FavoritesViewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var favoritesAdapter: TranslationHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesViewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        favoritesAdapter = TranslationHistoryAdapter(
            onFavoriteClick = { translation ->
                favoritesViewModel.removeFromFavorites(translation)
            },
            onDeleteClick = { translation ->
                favoritesViewModel.deleteTranslation(translation)
            },
            onItemClick = { translation ->
                // TODO: Handle item click, e.g., show details or re-translate
            }
        )
        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoritesAdapter
        }
    }

    private fun setupObservers() {
        favoritesViewModel.favorites.observe(viewLifecycleOwner) { favoritesList ->
            favoritesList?.let {
                favoritesAdapter.submitList(it)
                binding.tvNoFavorites.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

