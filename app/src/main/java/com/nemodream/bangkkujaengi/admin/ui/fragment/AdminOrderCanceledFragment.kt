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
import com.nemodream.bangkkujaengi.databinding.FragmentAdminOrderCanceledBinding

// 취소
class AdminOrderCanceledFragment : Fragment() {

    private lateinit var binding: FragmentAdminOrderCanceledBinding
    private lateinit var orderAdapter: AdminOrderAdapter
    private val viewModel: AdminOrderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminOrderCanceledBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 취소 탭은 배송상태만 취소자 정보로 대체
        setupHeaderVisibility(
            orderDateHeader = "취소일시",
            isDeliveryStatusVisible = true, // 취소자는 배송상태 자리에 표시
            deliveryStatusText = "취소자",
            isInvoiceNumberVisible = true,
            InvoiceNumberText = "취소사유",
            isDeliveryDateVisible = false
        )
        setupRecyclerView()
        setupScrollSync()
        observeOrders()
        viewModel.loadOrders(OrderState.CANCELED)
    }

    private fun setupRecyclerView() {

        orderAdapter = AdminOrderAdapter(
            orders = emptyList(),
            viewType = OrderViewType.CANCELED,
            onActionClick = {}, // 취소 탭에는 작업 버튼이 없음
            onCancelClick = {}
        )

        binding.recyclerViewCanceled.apply {
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
        deliveryStatusText: String = "",
        isInvoiceNumberVisible: Boolean = false,
        InvoiceNumberText :String = "",
        isDeliveryDateVisible: Boolean = false
    ) {
        with(binding.layoutHeader) {
            tvOrderPcOrderDateHeader.text = orderDateHeader

            tvOrderPcOrderDeliveryStatus.visibility =
                if (isDeliveryStatusVisible) View.VISIBLE else View.GONE
            if (isDeliveryStatusVisible) {
                tvOrderPcOrderDeliveryStatus.text = deliveryStatusText
            }

            tvOrderPcOrderInvoiceNumber.visibility =
                if (isInvoiceNumberVisible) View.VISIBLE else View.GONE
            if (isInvoiceNumberVisible) {
                tvOrderPcOrderInvoiceNumber.text = InvoiceNumberText
            }

            tvOrderPcOrderDeliveryDate.visibility =
                if (isDeliveryDateVisible) View.VISIBLE else View.GONE
        }
    }
}
