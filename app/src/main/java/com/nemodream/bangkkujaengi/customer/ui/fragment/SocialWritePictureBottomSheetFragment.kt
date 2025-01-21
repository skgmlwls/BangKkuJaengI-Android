package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.databinding.FragmentSocialWritePictureBinding
import com.nemodream.bangkkujaengi.databinding.FragmentSocialWritePictureBottomSheetBinding

class SocialWritePictureBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentSocialWritePictureBottomSheetBinding? = null
    private val binding get() = _binding!!

    // 프래그먼트의 뷰를 생성하는 메서드
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSocialWritePictureBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    // 뷰가 생성된 후 초기화 작업을 수행하는 메서드
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    // 프래그먼트가 파괴될 때 Binding 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}