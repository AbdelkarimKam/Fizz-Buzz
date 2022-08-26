package com.example.fizz_buzz.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.fizz_buzz.data.Resultat
import com.example.fizz_buzz.databinding.ResultatViewholderBinding

/**
 * Adapter for an [Resultat] [List].
 */
class ResultatAdapter : PagingDataAdapter<Resultat, ResultatViewHolder>(Resultat_DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultatViewHolder =
        ResultatViewHolder(
            ResultatViewholderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )

    override fun onBindViewHolder(holder: ResultatViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val Resultat_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Resultat>() {
            override fun areItemsTheSame(oldItem: Resultat, newItem: Resultat): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Resultat, newItem: Resultat): Boolean =
                oldItem == newItem
        }
    }
}
