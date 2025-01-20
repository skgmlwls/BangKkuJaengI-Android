package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nemodream.bangkkujaengi.customer.data.repository.OrderHistoryRepository
import com.nemodream.bangkkujaengi.customer.ui.adapter.OrderHistoryAdapter
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.OrderHistoryViewModel
import com.nemodream.bangkkujaengi.databinding.FragmentOrderHistoryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class OrderHistoryFragment : Fragment() {

    lateinit var fragmentOrderHistoryBinding: FragmentOrderHistoryBinding

    val orderHistoryViewModel : OrderHistoryViewModel by viewModels()

    // 유저 id
    val user_id = "testuser2"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentOrderHistoryBinding = FragmentOrderHistoryBinding.inflate(inflater, container, false)

        setting_order_history_recyclerview()

        return fragmentOrderHistoryBinding.root
    }

    // 날짜별로 묶는 recyclerview ////////////////////////////////////////////////////////////////////

    fun setting_order_history_recyclerview() {

        viewLifecycleOwner.lifecycleScope.launch {
            val work1 = async(Dispatchers.IO) {
                OrderHistoryRepository.getting_order_history_list(user_id)
            }
            orderHistoryViewModel.order_history_product_list.value = work1.await()


            // purchaseDate에서 날짜(년-월-일)만 추출하여 중복 제거 및 정렬
            val dateTimeFormatter = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
            orderHistoryViewModel.order_history_date_list.value = orderHistoryViewModel.order_history_product_list.value!!
                .mapNotNull { it.purchaseDate?.toDate() } // Timestamp를 Date로 변환
                .map { dateTimeFormatter.format(it).toLong() } // 날짜 형식으로 변환 (yyyy-MM-dd)
                .distinct() // 중복 제거
                .sortedDescending() // 내림차순 정렬

            orderHistoryViewModel.order_history_date_list.value!!.forEach {
                Log.d("uniqueTimes", "uniqueTimes: ${it}")
            }

            fragmentOrderHistoryBinding.apply {
                rvOrderHistoryList.adapter = OrderHistoryAdapter(
                    orderHistoryViewModel
                )
                rvOrderHistoryList.layoutManager = LinearLayoutManager(context)
            }
        }
    }

}