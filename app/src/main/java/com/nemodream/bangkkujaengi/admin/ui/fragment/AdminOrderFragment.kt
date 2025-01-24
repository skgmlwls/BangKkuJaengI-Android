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
    }

    // 뒤로가기
    private fun navigateBack(){
        binding.toolbarAdminOrder.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_refresh -> {
                // 새로고침 아이템 클릭 시 동작 처리
                context?.showToast("새로고침")
                refreshContent()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refreshContent() {

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
