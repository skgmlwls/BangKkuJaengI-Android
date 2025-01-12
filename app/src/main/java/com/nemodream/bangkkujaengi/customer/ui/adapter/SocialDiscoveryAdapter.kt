package com.nemodream.bangkkujaengi.customer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.customer.data.model.Post
import com.nemodream.bangkkujaengi.databinding.ItemSocialPostBinding
import com.nemodream.bangkkujaengi.utils.loadImage

class SocialDiscoveryAdapter: RecyclerView.Adapter<SocialDiscoveryAdapter.SocialDiscoveryViewHolder>() {
    private val items = mutableListOf<Post>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocialDiscoveryViewHolder {
        val binding = ItemSocialPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SocialDiscoveryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SocialDiscoveryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class SocialDiscoveryViewHolder(private val binding: ItemSocialPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post,) {
            binding.ivSocialPostItemThumbnail.loadImage(post.imageList[0])
            binding.tvSocialPostItemTitle.text = post.title
            // binding.ivSocialPostItemProfilePicture.loadImage(작성자 프사)
            binding.tvSocialPostItemNickname.text = post.nickname
            binding.tvSocialPostItemSavedCount.text = post.savedCount.toString()
        }
    }
}