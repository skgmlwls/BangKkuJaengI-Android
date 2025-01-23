package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.customer.ui.adapter.SocialCarouselAdapter
import com.nemodream.bangkkujaengi.databinding.FragmentSocialWritePictureBinding

class SocialWritePictureFragment : Fragment() {

    private var _binding: FragmentSocialWritePictureBinding? = null
    private val binding get() = _binding!!

    private val selectedPhotos = mutableListOf<Uri>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSocialWritePictureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupCarousel()

        // BottomSheet에서 전달된 선택된 사진 데이터를 수신
        childFragmentManager.setFragmentResultListener("selectedPhotos", this) { _, bundle ->
            val photos = bundle.getParcelableArrayList<Uri>("photos")
            if (!photos.isNullOrEmpty()) {
                updateSelectedPhotos(photos)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    // 사진 선택 바텀시트 올리기
    private fun openWritePictureBottomSheet() {
        val bottomSheetFragment = SocialWritePictureBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList("selectedPhotos", ArrayList(selectedPhotos))
            }
        }
        bottomSheetFragment.show(childFragmentManager, "SocialWritePictureBottomSheetFragment")
    }

    // 태그 선택 바텀시트 올리기
    private fun openWriteTagBottomSheet() {}

    // 선택된 사진 리스트를 업데이트
    private fun updateSelectedPhotos(photos: List<Uri>) {
        selectedPhotos.clear() // 이거 없애면 캐러셀에 사진 추가 할 수 있음
        selectedPhotos.addAll(photos)

        // Placeholder 숨기기 및 Carousel 표시
        binding.viewSocialWritePicturePlaceholder.visibility = View.GONE
        binding.tvSocialWritePicturePlaceholder.visibility = View.GONE
        binding.rvSocialWritePictureSelectedPhotos.visibility = View.VISIBLE

        // 버튼 상태 업데이트
        binding.btnAddPicture.visibility = View.GONE
        binding.btnModifyItem.visibility = View.VISIBLE
        binding.btnWritePictureNext.visibility = View.VISIBLE

        binding.rvSocialWritePictureSelectedPhotos.adapter?.notifyDataSetChanged()
    }

    private fun setupCarousel() {
        binding.rvSocialWritePictureSelectedPhotos.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = SocialCarouselAdapter(selectedPhotos)
        }
    }
}
