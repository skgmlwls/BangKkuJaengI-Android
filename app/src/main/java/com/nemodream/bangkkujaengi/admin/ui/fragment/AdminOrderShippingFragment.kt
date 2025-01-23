package com.nemodream.bangkkujaengi.admin.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nemodream.bangkkujaengi.admin.data.model.Order
import com.nemodream.bangkkujaengi.admin.data.model.OrderState
import com.nemodream.bangkkujaengi.admin.ui.adapter.AdminOrderAdapter
import com.nemodream.bangkkujaengi.admin.ui.adapter.OrderViewType
import com.nemodream.bangkkujaengi.admin.ui.viewmodel.AdminOrderViewModel
import com.nemodream.bangkkujaengi.databinding.FragmentAdminOrderShippingBinding

// 배송
class AdminOrderShippingFragment : Fragment() {

    private lateinit var binding: FragmentAdminOrderShippingBinding
    private lateinit var orderAdapter: AdminOrderAdapter
    private val viewModel: AdminOrderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminOrderShippingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeaderVisibility(
            orderDateHeader = "배송시작일",
            isDeliveryStatusVisible = true,
            isInvoiceNumberVisible = true,
            isDeliveryDateVisible = true
        )
        setupRecyclerView()
        setupScrollSync()
        observeOrders()
        viewModel.loadOrders(OrderState.SHIPPING)
    }

    private fun setupRecyclerView() {

        orderAdapter = AdminOrderAdapter(
            orders = emptyList(),
            viewType = OrderViewType.SHIPPING,
            onActionClick = {}, // 배송 탭에는 작업 버튼이 없음
            onCancelClick = {}
        )

        binding.recyclerViewShipping.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }
    }

    private fun observeOrders() {
        viewModel.orders.observe(viewLifecycleOwner) { orders ->
            orderAdapter.updateOrders(orders) // 어댑터에 데이터 갱신
        }
    }

    private fun setupScrollSync() {
        binding.layoutHeader.scrOrderPcHeader.setOnScrollChangeListener { _, scrollX, _, _, _ ->
            binding.scrollableRecyclerView.scrollTo(scrollX, 0)
        }

        binding.scrollableRecyclerView.setOnScrollChangeListener { _, scrollX, _, _, _ ->
            binding.layoutHeader.scrOrderPcHeader.scrollTo(scrollX, 0)
        }
    }

    private fun setupHeaderVisibility(
        orderDateHeader : String = "",
        isDeliveryStatusVisible: Boolean = false,
        isInvoiceNumberVisible: Boolean = false,
        isDeliveryDateVisible: Boolean = false
    ) {
        with(binding.layoutHeader) {
            tvOrderPcOrderDateHeader.text = orderDateHeader
            tvOrderPcOrderDeliveryStatus.visibility =
                if (isDeliveryStatusVisible) View.VISIBLE else View.GONE
            tvOrderPcOrderInvoiceNumber.visibility =
                if (isInvoiceNumberVisible) View.VISIBLE else View.GONE
            tvOrderPcOrderDeliveryDate.visibility =
                if (isDeliveryDateVisible) View.VISIBLE else View.GONE
        }
    }

}
