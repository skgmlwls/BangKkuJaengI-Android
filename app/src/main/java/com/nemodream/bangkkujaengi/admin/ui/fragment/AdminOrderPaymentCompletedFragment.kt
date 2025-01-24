package com.nemodream.bangkkujaengi.admin.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.nemodream.bangkkujaengi.admin.data.model.Order
import com.nemodream.bangkkujaengi.admin.data.model.OrderState
import com.nemodream.bangkkujaengi.admin.ui.adapter.OrderViewType
import com.nemodream.bangkkujaengi.databinding.FragmentAdminOrderPaymentCompletedBinding

// 결제완료
class AdminOrderPaymentCompletedFragment : BaseAdminOrderFragment() {
    private var _binding: FragmentAdminOrderPaymentCompletedBinding? = null
    private val binding get() = _binding!!

    override val viewType = OrderViewType.PAYMENT_COMPLETED
    override val orderState = OrderState.PAYMENT_COMPLETED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminOrderPaymentCompletedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeaderText(
            orderDateHeader = "주문일시"
        )
        setupRecyclerView(binding.recyclerViewPaymentCompleted)
        setupHeaderCheckbox(binding.layoutHeader.cbOrderPcHeader, binding.recyclerViewPaymentCompleted)
        setupScrollSync(headerScrollView = binding.layoutHeader.scrOrderPcHeader, recyclerScrollView = binding.scrollableRecyclerView)
        setupHeaderTextAndActions(
            cancelSelectionTextView = binding.layoutHeader.tvOrderPcCancelSelection,
            prepareSelectionTextView = binding.layoutHeader.tvOrderPcPrepareSelection,
            headerCheckbox = binding.layoutHeader.cbOrderPcHeader
        )
        viewModel.loadOrders(orderState)
    }

    override fun handleNextState(order: Order){
        Toast.makeText(requireContext(), "${order.productName} 상태가 '상품 준비중'으로 변경되었습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun handleCancel(order: Order) {
        Toast.makeText(requireContext(), "${order.productName} 주문이 취소되었습니다.", Toast.LENGTH_SHORT).show()

    }

}

