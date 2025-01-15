package com.nemodream.bangkkujaengi.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.customer.ui.fragment.HomeFragment
import com.nemodream.bangkkujaengi.customer.ui.fragment.SocialFragment
import com.nemodream.bangkkujaengi.databinding.FragmentCustomerContainerBinding
import com.nemodream.bangkkujaengi.utils.navigateToChildFragment
import com.nemodream.bangkkujaengi.utils.showSnackBar

// ParentFragment.kt
class CustomerContainerFragment : Fragment() {
    private var _binding: FragmentCustomerContainerBinding? = null
    private val binding get() = _binding!!

    private var backPressedTime: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomerContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomNavigation()

        // 초기 프래그먼트 설정
        if (savedInstanceState == null) {
            navigateToChildFragment(HomeFragment())
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            handleBackPressed()
        }
    }

    private fun handleBackPressed() {
        val currentFragment = childFragmentManager.findFragmentById(R.id.customer_container)
        when (currentFragment) {
            is HomeFragment -> handleHomeFragmentBackPressed()
            else -> navigateToHomeFragment()
        }
    }

    private fun handleHomeFragmentBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime <= DELAY_TIME) {
            // 2초 이내에 다시 백버튼을 누르면 앱 종료
            requireActivity().finish()
        } else {
            // 첫 번째 백버튼 클릭 시 메시지 표시
            backPressedTime = currentTime
            showSnackBar(BACK_SNACK_NAME)
        }
    }

    private fun navigateToHomeFragment() {
        // 다른 프래그먼트일 경우 HomeFragment로 이동
        navigateToChildFragment(HomeFragment())
    }

    private fun setupBottomNavigation() {
        binding.customerBottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navigateToChildFragment(HomeFragment())
                    true
                }

                R.id.navigation_social -> {
                    navigateToChildFragment(SocialFragment())
                    true
                }

                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val DELAY_TIME = 2000L
        const val BACK_SNACK_NAME = "뒤로가기 버튼을 한 번 더 누르면 종료됩니다"
    }
}
