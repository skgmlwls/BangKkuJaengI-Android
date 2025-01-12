package com.nemodream.bangkkujaengi.customer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.customer.data.model.SearchHistory
import com.nemodream.bangkkujaengi.databinding.ItemSearchHistoryListBinding

class SearchHistoryAdapter(
    private val listener: OnItemClickListener
) : ListAdapter<SearchHistory, SearchHistoryAdapter.SearchHistoryViewHolder>(
    SearchHistoryDiffCallBack()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        val binding =
            ItemSearchHistoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchHistoryViewHolder(
            binding,
            onClickListener = { position -> listener.onItemClick(getItem(position)) },
            onDeleteClickListener = { position -> listener.onDeleteClick(getItem(position)) }
        )
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SearchHistoryViewHolder(
        private val binding: ItemSearchHistoryListBinding,
        private val onClickListener: (position: Int) -> Unit,
        private val onDeleteClickListener: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onClickListener(adapterPosition)
            }

            binding.btnSearchCancel.setOnClickListener {
                onDeleteClickListener(adapterPosition)
            }
        }

        fun bind(search: SearchHistory) {
            binding.tvSearchText.text = search.query
        }
    }
}

interface OnItemClickListener {
    fun onItemClick(item: SearchHistory)
    fun onDeleteClick(search: SearchHistory)
}

class SearchHistoryDiffCallBack: DiffUtil.ItemCallback<SearchHistory>() {
    override fun areItemsTheSame(oldItem: SearchHistory, newItem: SearchHistory): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SearchHistory, newItem: SearchHistory): Boolean {
        return oldItem == newItem
    }

}