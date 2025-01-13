package com.nemodream.bangkkujaengi.customer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.databinding.ItemProductDetailImageListBinding
import com.nemodream.bangkkujaengi.utils.loadImage

class ProductDetailImageAdapter: RecyclerView.Adapter<ProductDetailImageAdapter.ProductDetailImageViewHolder>() {
    private val items = mutableListOf<String>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductDetailImageViewHolder {
        return ProductDetailImageViewHolder(
            ItemProductDetailImageListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductDetailImageViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(images: List<String>) {
        items.clear()
        items.addAll(images)
        notifyDataSetChanged()
    }

    class ProductDetailImageViewHolder(
        private val binding: ItemProductDetailImageListBinding,
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(image: String) {
            binding.ivProductDetailContentImage.loadImage(image)
        }

    }
}