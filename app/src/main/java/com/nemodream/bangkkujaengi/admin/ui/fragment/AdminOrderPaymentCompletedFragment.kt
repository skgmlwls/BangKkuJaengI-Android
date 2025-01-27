package com.nemodream.bangkkujaengi.admin.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.nemodream.bangkkujaengi.admin.data.model.Order
import com.nemodream.bangkkujaengi.admin.data.model.OrderState
import com.nemodream.bangkkujaengi.admin.ui.adapter.OrderViewType
import com.nemodream.bangkkujaengi.admin.ui.custom.CustomCanceledDialog
import com.nemodream.bangkkujaengi.customer.ui.custom.CustomDialog
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
        val context = requireContext()
        CustomDialog(
            context = context,
            message = "결제 완료된 상품을 준비 상태로 변경하시겠습니까?",
            confirmText = "확인",
            cancelText = "취소",
            onConfirm = {
                viewModel.updateOrderState(order, OrderState.PRODUCT_READY)
            },
            onCancel = {}
        ).show()
    }

    override fun handleCancel(order: Order) {
        val reasons = listOf("재고 부족", "가격 오류", "주문 정보 오류", "결제 오류", "기타")

        CustomCanceledDialog(
            context = requireContext(),
            message = "결제 완료된 상품의 주문을 취소하시겠습니까?",
            reasons = reasons,
            onConfirm = {
                viewModel.updateOrderState(order, OrderState.CANCELED)
            },
            onCancel = { }
        ).show()
    }

}

