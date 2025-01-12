package com.nemodream.bangkkujaengi.customer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.databinding.ItemHomeBannerBinding
import com.nemodream.bangkkujaengi.utils.loadImage

class ProductDetailBannerAdapter : RecyclerView.Adapter<ProductDetailBannerAdapter.ProductDetailBannerViewHolder>() {
    private val items = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDetailBannerViewHolder {
        return ProductDetailBannerViewHolder(
            ItemHomeBannerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductDetailBannerViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun submitList(bannerItems: List<String>) {
        items.clear()
        items.addAll(bannerItems)
        notifyDataSetChanged()
    }

    class ProductDetailBannerViewHolder(
        private val binding: ItemHomeBannerBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: String) {
            binding.ivHomeBannerImage.loadImage(image)
        }
    }
}
