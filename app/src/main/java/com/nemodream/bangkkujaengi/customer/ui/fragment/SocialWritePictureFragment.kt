package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.databinding.FragmentSocialFollowingAllBinding
import com.nemodream.bangkkujaengi.databinding.FragmentSocialWritePictureBinding
// 상수 선언
private const val REQUEST_CODE_PERMISSION = 100

class SocialWritePictureFragment : Fragment() {

    private var _binding: FragmentSocialWritePictureBinding? = null
    private val binding get() = _binding!!


    // 프래그먼트의 뷰를 생성하는 메서드
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSocialWritePictureBinding.inflate(inflater, container, false)
        return binding.root
    }

    // 뷰가 생성된 후 초기화 작업을 수행하는 메서드
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    // 프래그먼트가 파괴될 때 Binding 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 리스너 설정
    private fun setupListeners() {
        with(binding) {
            toolbarSocial.setNavigationOnClickListener {
                findNavController().popBackStack(R.id.navigation_social, false)
            }
            btnAddPicture.setOnClickListener {
                val socialWritePictureBottomSheetFragment = SocialWritePictureBottomSheetFragment()
                socialWritePictureBottomSheetFragment.show(childFragmentManager, "SocialWritePictureBottomSheetFragment")
            }

            btnAddPicture.setOnClickListener {
                checkAndRequestPermission() // 버튼 클릭 시 권한 요청
            }
        }
    }

    // 권한 요청 함수
    private fun checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 이상
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_IMAGES), REQUEST_CODE_PERMISSION)
            } else {
                loadGalleryPhotos() // 권한이 이미 허용된 경우
            }
        } else {
            // Android 6 ~ 12
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION)
            } else {
                loadGalleryPhotos() // 권한이 이미 허용된 경우
            }
        }
    }

    // 권한 결과 처리 함수
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadGalleryPhotos() // 권한이 허용된 경우
            } else {
                Toast.makeText(context, "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 갤러리 불러오기 작업 (예시)
    private fun loadGalleryPhotos() {
        // 사진 불러오는 로직 (fetchGalleryPhotos 호출 등)
        val photoList = fetchGalleryPhotos()
        val adapter = GalleryAdapter(photoList)
        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerView.adapter = adapter
    }
}