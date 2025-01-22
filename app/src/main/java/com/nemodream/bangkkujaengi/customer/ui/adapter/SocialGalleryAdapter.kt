package com.nemodream.bangkkujaengi.customer.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nemodream.bangkkujaengi.databinding.ItemSocialGalleryPhotoBinding

class SocialGalleryAdapter(private val photoList: List<Uri>) :
    RecyclerView.Adapter<SocialGalleryAdapter.SocialGalleryViewHolder>() {

    inner class SocialGalleryViewHolder(private val binding: ItemSocialGalleryPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photoUri: Uri) {
            Glide.with(binding.root.context)
                .load(photoUri)
                .into(binding.ivPhoto)

            // 사진 클릭 이벤트
            binding.root.setOnClickListener {
                // 선택 동작 처리 (예: 선택된 사진 강조 표시)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocialGalleryViewHolder {
        val binding = ItemSocialGalleryPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SocialGalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SocialGalleryViewHolder, position: Int) {
        holder.bind(photoList[position])
    }

    override fun getItemCount(): Int = photoList.size
}
