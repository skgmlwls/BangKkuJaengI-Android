package com.nemodream.bangkkujaengi.customer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.customer.data.model.Banner
import com.nemodream.bangkkujaengi.databinding.ItemHomeBannerBinding
import com.nemodream.bangkkujaengi.utils.loadImage

class HomeBannerAdapter: RecyclerView.Adapter<HomeBannerAdapter.HomeBannerViewHolder>() {
    private val items = mutableListOf<Banner>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeBannerViewHolder {
        val binding = ItemHomeBannerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HomeBannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeBannerViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun submitList(bannerItems: List<Banner>) {
        items.clear()
        items.addAll(bannerItems)
        notifyDataSetChanged()
    }

    class HomeBannerViewHolder(private val binding: ItemHomeBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(banner: Banner) {
            binding.ivHomeBannerImage.loadImage(banner.thumbnailImageRef)
        }
    }
}