package com.nemodream.bangkkujaengi.customer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.databinding.RowReviewWriteBinding

class ReviewWriteListAdapter(private val onClick: (String) -> Unit) :
    ListAdapter<String, ReviewWriteListAdapter.ReviewViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = RowReviewWriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val reviewItem = getItem(position)
        holder.bind(reviewItem)
    }

    inner class ReviewViewHolder(private val binding: RowReviewWriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reviewItem: String) {
            binding.tvReviewWriteProductName.text = reviewItem
            binding.btnWriteReview.setOnClickListener {
                onClick(reviewItem)  // 리뷰 작성 화면으로 이동하는 콜백 호출
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
        }
    }
}
