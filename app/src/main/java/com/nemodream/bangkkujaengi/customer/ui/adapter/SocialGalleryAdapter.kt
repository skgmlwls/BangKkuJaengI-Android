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

        // photoUri : 표시할 이미지의 URI
        fun bind(photoUri: Uri) {
            // Glide 라이브러리 : 이미지를 ImageView에 비동기적으로 로드
            Glide.with(binding.root.context)
                // photoUri를 통해 이미지를 로드
                .load(photoUri)
                // 이미지를 ivPhoto에 표시
                .into(binding.ivPhoto)

            // 사진 클릭 이벤트
            binding.root.setOnClickListener {
                val isSelected = binding.selectionCircle.isSelected
                binding.selectionCircle.isSelected = !isSelected // 선택 상태 토글
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocialGalleryViewHolder {
        val binding = ItemSocialGalleryPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            // RecyclerView의 부모 뷰
            parent,
            // 루트 뷰를 즉시 부모 뷰에 추가하지 않는다
            false
        )
        return SocialGalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SocialGalleryViewHolder, position: Int) {
        holder.bind(photoList[position])
    }

    override fun getItemCount(): Int = photoList.size
}
