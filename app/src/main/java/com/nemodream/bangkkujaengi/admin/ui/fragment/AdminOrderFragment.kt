package com.nemodream.bangkkujaengi.admin.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayoutMediator
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.databinding.FragmentAdminOrderBinding
import com.nemodream.bangkkujaengi.utils.showSnackBar
import com.nemodream.bangkkujaengi.utils.showToast

class AdminOrderFragment : Fragment() {

    private lateinit var binding: FragmentAdminOrderBinding
    private lateinit var pagerAdapter: OrderPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPagerAndTabs()
        navigateBack()
        settingToolbarMenuClickListener()
    }

    // 뒤로가기
    private fun navigateBack(){
        binding.toolbarAdminOrder.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun settingToolbarMenuClickListener() {
        // 툴바 메뉴 클릭 리스너 설정
        val toolbar = view?.findViewById<MaterialToolbar>(R.id.toolbar_admin_order)
        toolbar?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_refresh -> {
                    Toast.makeText(requireContext(), "새로고침 버튼 클릭됨", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupViewPagerAndTabs() {
        // Adapter 연결
        pagerAdapter = OrderPagerAdapter(this)
        binding.viewPagerAdminOrder.adapter = pagerAdapter

        // ViewPager2의 사용자 입력 비활성화 -> 좌우 스와이프로 탭 이동 불가
        binding.viewPagerAdminOrder.isUserInputEnabled = false

        // TabLayout과 ViewPager2 연결
        TabLayoutMediator(binding.tabLayoutAdminOrder, binding.viewPagerAdminOrder) { tab, position ->
            tab.text = when (position) {
                0 -> "결제 완료"
                1 -> "상품 준비"
                2 -> "배송"
                3 -> "구매 확정"
                4 -> "취소"
                else -> throw IllegalArgumentException("Invalid position")
            }
        }.attach()
    }

}

class OrderPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 5 // 탭 개수
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AdminOrderPaymentCompletedFragment()
            1 -> AdminOrderProductReadyFragment()
            2 -> AdminOrderShippingFragment()
            3 -> AdminOrderPurchaseConfirmedFragment()
            4 -> AdminOrderCanceledFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
