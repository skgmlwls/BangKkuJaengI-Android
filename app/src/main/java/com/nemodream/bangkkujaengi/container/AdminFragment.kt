package com.nemodream.bangkkujaengi.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.admin.ui.fragment.AdminHomeFragment
import com.nemodream.bangkkujaengi.databinding.FragmentAdminBinding

class AdminFragment: Fragment() {
    private var _binding: FragmentAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // AdminHomeFragment를 초기 화면으로 설정
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .add(R.id.admin_container, AdminHomeFragment())
                .commit()
        }

        // 백 네비게이션 처리
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (childFragmentManager.backStackEntryCount > 0) {
                childFragmentManager.popBackStack()
            } else {
                requireActivity().finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}