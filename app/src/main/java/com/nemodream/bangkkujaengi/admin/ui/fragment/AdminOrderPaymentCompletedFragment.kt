package com.nemodream.bangkkujaengi.admin.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nemodream.bangkkujaengi.admin.data.model.Order
import com.nemodream.bangkkujaengi.admin.data.model.OrderState
import com.nemodream.bangkkujaengi.admin.ui.adapter.AdminOrderAdapter
import com.nemodream.bangkkujaengi.admin.ui.adapter.OrderViewType
import com.nemodream.bangkkujaengi.admin.ui.viewmodel.AdminOrderViewModel
import com.nemodream.bangkkujaengi.databinding.FragmentAdminOrderPaymentCompletedBinding

// 결제완료
class AdminOrderPaymentCompletedFragment : Fragment() {

    private lateinit var binding : FragmentAdminOrderPaymentCompletedBinding
    private lateinit var orderAdapter: AdminOrderAdapter
    private val viewModel: AdminOrderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminOrderPaymentCompletedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupScrollSync()
        observeOrders()
        viewModel.loadOrders(OrderState.PAYMENT_COMPLETED)
    }

    private fun setupRecyclerView() {
        // 어댑터 초기화
        orderAdapter = AdminOrderAdapter(
            orders = emptyList(),
            viewType = OrderViewType.PAYMENT_COMPLETED,
            onActionClick = { order -> handleNextState(order) },
            onCancelClick = { order -> handleCancel(order) }
        )

        // RecyclerView 설정
        binding.recyclerViewPaymentCompleted.apply {
            layoutManager = LinearLayoutManager(requireContext()) // 세로 스크롤
            adapter = orderAdapter
        }
    }

    private fun observeOrders() {
        viewModel.orders.observe(viewLifecycleOwner) { orders ->
            orderAdapter.updateOrders(orders) // 어댑터에 데이터 갱신
        }
    }

    private fun handleNextState(order: Order) {
        Toast.makeText(requireContext(), "${order.productName} 상품 준비로 이동", Toast.LENGTH_SHORT).show()
    }

    private fun handleCancel(order: Order) {
        Toast.makeText(requireContext(), "${order.productName} 주문 취소", Toast.LENGTH_SHORT).show()
    }

    private fun setupScrollSync() {
        // Header의 HorizontalScrollView 스크롤 동작 감지
        binding.layoutHeader.scrOrderPcHeader.setOnScrollChangeListener { _, scrollX, _, _, _ ->
            binding.scrollableRecyclerView.scrollTo(scrollX, 0)
        }

        // RecyclerView의 HorizontalScrollView 스크롤 동작 감지
        binding.scrollableRecyclerView.setOnScrollChangeListener { _, scrollX, _, _, _ ->
            binding.layoutHeader.scrOrderPcHeader.scrollTo(scrollX, 0)
        }
    }
}
