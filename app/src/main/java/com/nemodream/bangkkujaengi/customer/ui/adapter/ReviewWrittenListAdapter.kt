package com.nemodream.bangkkujaengi.customer.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.databinding.RowReviewWrittenBinding

data class WrittenReview(
    val productName: String,
    val rating: Int,
    val writtenDate: String,
    val content: String
)

class ReviewWrittenListAdapter :
    ListAdapter<WrittenReview, ReviewWrittenListAdapter.ReviewViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = RowReviewWrittenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val reviewItem = getItem(position)
        holder.bind(reviewItem)
    }

    inner class ReviewViewHolder(private val binding: RowReviewWrittenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reviewItem: WrittenReview) {
            binding.tvReviewWrittenProductName.text = reviewItem.productName
            binding.tvReviewWrittenDate.text = reviewItem.writtenDate
            binding.tvReviewWrittenContent.text = reviewItem.content

            // 별점 아이콘들 가져오기
            val starViews = listOf(
                binding.layoutReviewWrittenRating.findViewById<ImageView>(R.id.star_1),
                binding.layoutReviewWrittenRating.findViewById<ImageView>(R.id.star_2),
                binding.layoutReviewWrittenRating.findViewById<ImageView>(R.id.star_3),
                binding.layoutReviewWrittenRating.findViewById<ImageView>(R.id.star_4),
                binding.layoutReviewWrittenRating.findViewById<ImageView>(R.id.star_5)
            )

            // 별점 상태 업데이트
            starViews.forEachIndexed { index, starView ->
                if (index < reviewItem.rating) {
                    starView.setImageResource(R.drawable.ic_star_fill)  // 채워진 별
                } else {
                    starView.setImageResource(R.drawable.ic_star_outline)  // 비어 있는 별
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<WrittenReview>() {
            override fun areItemsTheSame(oldItem: WrittenReview, newItem: WrittenReview): Boolean =
                oldItem.productName == newItem.productName

            override fun areContentsTheSame(oldItem: WrittenReview, newItem: WrittenReview): Boolean =
                oldItem == newItem
        }
    }
}
