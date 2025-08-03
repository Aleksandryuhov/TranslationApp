package com.translator.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.translator.app.R
import com.translator.app.data.TranslationHistory
import com.translator.app.databinding.ItemTranslationHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

class TranslationHistoryAdapter(
    private val onFavoriteClick: (TranslationHistory) -> Unit,
    private val onDeleteClick: (TranslationHistory) -> Unit,
    private val onItemClick: (TranslationHistory) -> Unit
) : ListAdapter<TranslationHistory, TranslationHistoryAdapter.TranslationViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslationViewHolder {
        val binding = ItemTranslationHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TranslationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TranslationViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    inner class TranslationViewHolder(private val binding: ItemTranslationHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(translation: TranslationHistory) {
            binding.apply {
                tvOriginalText.text = translation.originalText
                tvTranslatedText.text = translation.translatedText
                tvTimestamp.text = formatTimestamp(translation.timestamp)

                // Set favorite icon
                btnFavoriteItem.setImageResource(
                    if (translation.isFavorite) R.drawable.ic_favorite
                    else R.drawable.ic_favorite_border
                )

                // Set click listeners
                btnFavoriteItem.setOnClickListener {
                    onFavoriteClick(translation)
                }

                btnDeleteItem.setOnClickListener {
                    onDeleteClick(translation)
                }

                root.setOnClickListener {
                    onItemClick(translation)
                }
            }
        }

        private fun formatTimestamp(timestamp: Long): String {
            val now = System.currentTimeMillis()
            val diff = now - timestamp

            return when {
                diff < 60_000 -> "Только что"
                diff < 3600_000 -> "${diff / 60_000} мин назад"
                diff < 86400_000 -> "${diff / 3600_000} ч назад"
                diff < 604800_000 -> "${diff / 86400_000} дн назад"
                else -> {
                    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    sdf.format(Date(timestamp))
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<TranslationHistory>() {
            override fun areItemsTheSame(oldItem: TranslationHistory, newItem: TranslationHistory): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TranslationHistory, newItem: TranslationHistory): Boolean {
                return oldItem == newItem
            }
        }
    }
}

