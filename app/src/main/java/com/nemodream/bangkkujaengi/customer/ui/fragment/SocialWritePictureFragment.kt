package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.get
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
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

    private lateinit var appContext: Context

    private val carouselAdapter: SocialCarouselAdapter by lazy {
        SocialCarouselAdapter(selectedPhotos) { position, x, y ->
            openWriteTagBottomSheet(position, x, y)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext = context
    }

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
                addTagPin(position, xCoord, yCoord, product)

                // Tag 데이터 생성 및 리스트에 추가
                val tag = Tag(tagX = xCoord, tagY = yCoord, tagProductInfo = product)
                productTagPinList.add(tag)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 태그 핀 추가 함수
    private fun addTagPin(position: Int, x: Float, y: Float, product: Product) {
        val container = (binding.vpSocialWritePictureCarousel[0] as RecyclerView)
            .findViewHolderForAdapterPosition(position)?.itemView as? FrameLayout

        val tagPin = ImageView(requireContext()).apply {
            setImageResource(R.drawable.ic_tag_pin)
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                leftMargin = (x - 20).toInt()
                topMargin = (y - 20).toInt()
            }
        }

        container?.addView(tagPin)

        tagPin.setOnClickListener {
            // 태그 정보 표시 로직
            tag?.let {
                Toast.makeText(
                    binding.root.context,
                    "상품명: ${product.productName}, 가격: ${product.price}",
                    Toast.LENGTH_SHORT
                ).show()
            }
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
        binding.vpSocialWritePictureCarousel.adapter = carouselAdapter
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