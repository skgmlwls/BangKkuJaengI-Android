package com.nemodream.bangkkujaengi.customer.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nemodream.bangkkujaengi.databinding.ItemSocialCarouselPhotoBinding
import com.nemodream.bangkkujaengi.R

class SocialCarouselAdapter(
    private val photos: List<Uri>,
    private val onPhotoClick: (Int, Float, Float) -> Unit // 클릭 이벤트 콜백 추가
) : RecyclerView.Adapter<SocialCarouselAdapter.CarouselViewHolder>() {

    inner class CarouselViewHolder(private val binding: ItemSocialCarouselPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photoUri: Uri) {
            // 이미지 로드
            Glide.with(binding.root.context)
                .load(photoUri)
                .into(binding.ivCarouselPhoto)

            // 터치 이벤트로 태그 위치 처리
            binding.ivCarouselPhoto.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    // 클릭된 위치에서 태그 핀 추가
                    addTagPin(event.x, event.y)
                    // 클릭된 위치 정보 콜백 호출
                    onPhotoClick(adapterPosition, event.x, event.y)
                }
                true
            }
        }

        // 태그 핀 추가하는 함수
        private fun addTagPin(x: Float, y: Float) {
            // 태그 핀을 추가하는 로직
            val tagPin = ImageView(binding.root.context).apply {
                setImageResource(R.drawable.ic_tag_pin)  // 태그 핀 아이콘
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    leftMargin = (x - 20).toInt()  // 태그 위치 조정
                    topMargin = (y - 20).toInt()   // 태그 위치 조정
                }
            }
            (binding.root as FrameLayout).addView(tagPin)  // 태그 핀을 FrameLayout에 추가
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = ItemSocialCarouselPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    override fun getItemCount(): Int = photos.size
}
