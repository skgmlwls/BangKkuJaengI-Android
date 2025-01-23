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
import com.nemodream.bangkkujaengi.databinding.FragmentAdminOrderProductReadyBinding

// 상품 준비
class AdminOrderProductReadyFragment : Fragment() {

    private lateinit var binding: FragmentAdminOrderProductReadyBinding
    private lateinit var orderAdapter: AdminOrderAdapter
    private val viewModel: AdminOrderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminOrderProductReadyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupScrollSync()
        observeOrders()
        viewModel.loadOrders(OrderState.PRODUCT_READY)
    }

    private fun setupRecyclerView() {

        orderAdapter = AdminOrderAdapter(
            orders = emptyList(),
            viewType = OrderViewType.PRODUCT_READY,
            onActionClick = { order -> handleNextState(order) },
            onCancelClick = { order -> {} } // 상품 준비 탭에는 취소 버튼이 없음
        )

        binding.recyclerViewProductReady.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }
    }

    private fun observeOrders() {
        viewModel.orders.observe(viewLifecycleOwner) { orders ->
            orderAdapter.updateOrders(orders) // 어댑터에 데이터 갱신
        }
    }

    private fun handleNextState(order: Order) {
        Toast.makeText(requireContext(), "${order.productName} 배송으로 이동", Toast.LENGTH_SHORT).show()
    }

    private fun setupScrollSync() {
        binding.layoutHeader.scrOrderPcHeader.setOnScrollChangeListener { _, scrollX, _, _, _ ->
            binding.scrollableRecyclerView.scrollTo(scrollX, 0)
        }

        binding.scrollableRecyclerView.setOnScrollChangeListener { _, scrollX, _, _, _ ->
            binding.layoutHeader.scrOrderPcHeader.scrollTo(scrollX, 0)
        }
    }
}

