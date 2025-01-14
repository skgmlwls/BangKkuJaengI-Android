package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.nemodream.bangkkujaengi.customer.data.model.SocialCategoryType
import com.nemodream.bangkkujaengi.databinding.FragmentSocialBinding
import com.nemodream.bangkkujaengi.utils.popBackStack
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SocialFragment: Fragment() {
    private var _binding: FragmentSocialBinding? = null
    private val binding get() = _binding!!

    private var socialCategoryType: SocialCategoryType? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 전달받은 socialCategoryType을 초기화
        socialCategoryType = SocialCategoryType.valueOf(
            arguments?.getString(KEY_SOCIAL_CATEGORY_TYPE) ?: SocialCategoryType.DISCOVERY.name
        )
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSocialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupTabs()
        setupViewPager()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    * 리스너 모음 함수
    */
    private fun setupListeners() {
        with(binding) {
            toolbarSocial.setNavigationOnClickListener {
                popBackStack()
            }
        }
    }

    /*
    * 탭 설정
    */
    private fun setupTabs() {
        // SocialCategoryType의 항목을 탭으로 추가
        SocialCategoryType.entries.forEach { type ->
            binding.tabSocialCategory.addTab(binding.tabSocialCategory.newTab().setText(type.getSocialTabTitle()))
        }

        val initialPosition = socialCategoryType?.ordinal ?: 0

        viewLifecycleOwner.lifecycleScope.launch {
            delay(SocialFragment.Companion.DELAY_TIME) // 초기 탭 선택 시 자연스러운 애니메이션을 위해 딜레이를 준다.
            binding.tabSocialCategory.selectTab(binding.tabSocialCategory.getTabAt(initialPosition), true)
        }
    }

    /*
    * 뷰 페이저 설정
    */
    private fun setupViewPager() {
        val adapter = SocialPagerAdapter(this)
        binding.viewPagerSocialCategory.adapter = adapter

        // TabLayout과 ViewPager2 연결
        TabLayoutMediator(binding.tabSocialCategory, binding.viewPagerSocialCategory) { tab, position ->
            tab.text = SocialCategoryType.values()[position].getSocialTabTitle()
        }.attach()
    }


    companion object {
        private const val KEY_SOCIAL_CATEGORY_TYPE = "social_category_type"
        private const val DELAY_TIME = 100L

        fun newInstance(type: SocialCategoryType): SocialFragment {
            return SocialFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_SOCIAL_CATEGORY_TYPE, type.name)
                }
            }
        }
    }
}

// ViewPager2의 어댑터
class SocialPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = SocialCategoryType.values().size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            SocialCategoryType.DISCOVERY.ordinal -> SocialDiscoveryFragment.newInstance()
            SocialCategoryType.RANK.ordinal -> SocialRankFragment.newInstance()
            SocialCategoryType.FOLLOWING.ordinal -> SocialFollowingFragment.newInstance()
            else -> SocialDiscoveryFragment.newInstance() // 임시로 동일한 fragment를 리턴
        }
    }
}