package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.customer.data.model.Product
import com.nemodream.bangkkujaengi.customer.data.model.Tag
import com.nemodream.bangkkujaengi.customer.ui.adapter.SocialCarouselAdapter
import com.nemodream.bangkkujaengi.databinding.FragmentSocialWritePictureBinding

class SocialWritePictureFragment : Fragment() {

    private var _binding: FragmentSocialWritePictureBinding? = null
    private val binding get() = _binding!!
    private val selectedPhotos = mutableListOf<Uri>()
    private val productTagPinList = mutableListOf<Tag>() // 태그 리스트


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSocialWritePictureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupViewPager()

        // PictureBottomSheet에서 전달된 선택된 사진 데이터를 수신
        childFragmentManager.setFragmentResultListener("selectedPhotos", this) { _, bundle ->
            val photos = bundle.getParcelableArrayList<Uri>("photos")
            if (!photos.isNullOrEmpty()) {
                updateSelectedPhotos(photos)
            }
        }

        // TagBottomSheet에서 전달된 상품 데이터 수신
        childFragmentManager.setFragmentResultListener("productWithTagData", this) { _, bundle ->
            val product = bundle.getParcelable<Product>("selectedProduct")
            val position = bundle.getInt("photoPosition", -1)
            val xCoord = bundle.getFloat("xCoord", 0f)
            val yCoord = bundle.getFloat("yCoord", 0f)

            // 전달받은 데이터를 로그로 확인 (필요에 따라 추가 처리)
            Log.d("SocialWritePictureFragment", "Product: $product, Position: $position, X: $xCoord, Y: $yCoord")

            if (product != null && position != -1) {
                // Tag 데이터 생성
                val tag = Tag(tagX = xCoord, tagY = yCoord, tagProductInfo = product)

                // 태그를 리스트에 추가
                productTagPinList.add(tag)

                // 태그 핀을 화면에 표시
                addTagPinToImage(position, tag)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 태그 핀 추가 함수
    private fun addTagPinToImage(position: Int, tag: Tag) {
        val imageView = ImageView(requireContext()).apply {
            setImageResource(R.drawable.ic_tag_pin)
            layoutParams = ViewGroup.LayoutParams(50, 50) // 핀 크기 조정
            x = tag.tagX
            y = tag.tagY
        }

        tag.pinView = imageView

        // 특정 이미지에 태그 핀 추가
        val imageViewContainer = binding.vpSocialWritePictureCarousel.findViewWithTag<ViewGroup>("imageContainer_$position")

        if (imageViewContainer != null) {
            imageViewContainer.addView(imageView)
        } else {
            Log.e("SocialWritePictureFragment", "Container not found for position $position")
        }
    }

    // 리스너 모음
    private fun setupListeners() {
        with(binding) {
            // 툴바의 뒤로가기 아이콘
            toolbarSocial.setNavigationOnClickListener {
                findNavController().popBackStack(R.id.navigation_social, false)
            }

            // "사진 추가" 버튼
            btnAddPicture.setOnClickListener {
                openWritePictureBottomSheet()
            }

            // "항목 수정" 버튼
            btnModifyItem.setOnClickListener {
                openWritePictureBottomSheet()
            }

            // 게시된 사진 클릭 리스너
            // 클릭하면..
            // 사진에서 클릭된 위치값 저장 함수 호출
            // openWriteTagBottomSheet() 바텀시트 올리기 함수 호출

            // 사진 추가 화면에서 "다음" 버튼
            btnWritePictureNext.setOnClickListener {
                binding.btnWritePictureNext.visibility = View.GONE
                binding.btnModifyItem.visibility = View.GONE
                binding.btnWriteTagNext.visibility = View.VISIBLE
            }

            // 태그 추가 화면에서 "다음" 버튼
            btnWriteTagNext.setOnClickListener {
                binding.btnWriteTagNext.visibility = View.GONE
                binding.tfWriteTitle.visibility = View.VISIBLE
                binding.tfWriteContent.visibility = View.VISIBLE
                binding.btnPost.visibility = View.VISIBLE
            }
        }
    }

    private fun setupViewPager() {
        binding.vpSocialWritePictureCarousel.apply {
            adapter = SocialCarouselAdapter(selectedPhotos) { position, x, y ->
                // 사진 클릭 시 태그 추가 BottomSheet 열기
                openWriteTagBottomSheet(position, x, y)
            }
        }
    }

    // 선택된 사진 리스트를 업데이트
    private fun updateSelectedPhotos(photos: List<Uri>) {
        selectedPhotos.clear()
        selectedPhotos.addAll(photos)

        // Placeholder 숨기기 및 Carousel 표시
        binding.viewSocialWritePicturePlaceholder.visibility = View.GONE
        binding.tvSocialWritePicturePlaceholder.visibility = View.GONE
        binding.flSocialWritePictureContainer.visibility = View.VISIBLE

        // 버튼 상태 업데이트
        binding.btnAddPicture.visibility = View.GONE
        binding.btnModifyItem.visibility = View.VISIBLE
        binding.btnWritePictureNext.visibility = View.VISIBLE

        binding.vpSocialWritePictureCarousel.adapter?.notifyDataSetChanged()
    }

    // 사진 선택 바텀시트 올리기
    private fun openWritePictureBottomSheet() {
        val bottomSheetFragment = SocialWritePictureBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList("selectedPhotos", ArrayList(selectedPhotos))
            }
        }
        bottomSheetFragment.show(childFragmentManager, "SocialWritePictureBottomSheetFragment")
    }

    // 태그 추가 바텀시트 올리기
    private fun openWriteTagBottomSheet(position: Int, x: Float, y: Float) {
        val bottomSheetFragment = SocialWriteTagBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putInt("photoPosition", position)
                putFloat("xCoord", x)
                putFloat("yCoord", y)
            }
        }
        bottomSheetFragment.show(childFragmentManager, "SocialWriteTagBottomSheetFragment")
    }
}
