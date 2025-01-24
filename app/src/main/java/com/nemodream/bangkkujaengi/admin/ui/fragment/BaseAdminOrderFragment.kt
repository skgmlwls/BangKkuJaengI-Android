package com.nemodream.bangkkujaengi.admin.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.checkbox.MaterialCheckBox
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.admin.data.model.Order
import com.nemodream.bangkkujaengi.admin.data.model.OrderState
import com.nemodream.bangkkujaengi.admin.ui.adapter.AdminOrderAdapter
import com.nemodream.bangkkujaengi.admin.ui.adapter.OrderViewType
import com.nemodream.bangkkujaengi.admin.ui.viewmodel.AdminOrderViewModel

abstract class BaseAdminOrderFragment : Fragment() {

    protected abstract val viewType: OrderViewType
    protected abstract val orderState: OrderState
    protected val viewModel: AdminOrderViewModel by viewModels()
    // private val selectedOrders = mutableSetOf<String>()

    protected lateinit var orderAdapter: AdminOrderAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    protected open fun setupRecyclerView(recyclerView: androidx.recyclerview.widget.RecyclerView) {
        orderAdapter = AdminOrderAdapter(
            orders = emptyList(),
            viewType = viewType,
            onActionClick = { order -> handleNextState(order) },
            onCancelClick = { order -> handleCancel(order) },
            isOrderSelected = { orderNumber -> viewModel.isOrderSelected(orderNumber) },
            onOrderCheckedChange = { orderNumber, isChecked ->
                viewModel.updateOrderSelection(orderNumber, isChecked)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = orderAdapter
    }

    private fun observeViewModel() {
        viewModel.orders.observe(viewLifecycleOwner) { orders ->
            orderAdapter.updateOrders(orders)
        }

        viewModel.headerCheckboxState.observe(viewLifecycleOwner) { state ->
            val headerCheckbox = view?.findViewById<MaterialCheckBox>(R.id.cb_order_pc_header)
            headerCheckbox?.checkedState = state
        }
    }


    protected open fun setupHeaderCheckbox(
        checkbox: MaterialCheckBox,
        recyclerView: androidx.recyclerview.widget.RecyclerView
    ) {
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            // 헤더 체크박스 상태에 따라 ViewModel 업데이트 호출
            viewModel.toggleSelectAllOrders(isChecked)

            // 어댑터 강제 갱신
            orderAdapter.notifyDataSetChanged() // 이 코드가 없으면 뷰가 갱신되지 않을 수 있음
        }
    }

    protected open fun setupScrollSync(
        headerScrollView: HorizontalScrollView,
        recyclerScrollView: HorizontalScrollView
    ) {
        headerScrollView.setOnScrollChangeListener { _, scrollX, _, _, _ ->
            recyclerScrollView.scrollTo(scrollX, 0)
        }

        recyclerScrollView.setOnScrollChangeListener { _, scrollX, _, _, _ ->
            headerScrollView.scrollTo(scrollX, 0)
        }
    }

    protected open fun setupHeaderText(
        orderDateHeader: String,
        deliveryStatusHeader: String? = null,
        invoiceNumberHeader: String? = null,
        deliveryDateHeader: String? = null
    ) {
        // 헤더 텍스트를 동적으로 설정
        view?.findViewById<android.widget.TextView>(R.id.tv_order_pc_order_date_header)?.text =
            orderDateHeader

        view?.findViewById<android.widget.TextView>(R.id.tv_order_pc_order_delivery_status)?.apply {
            text = deliveryStatusHeader
            visibility = if (deliveryStatusHeader != null) View.VISIBLE else View.GONE
        }

        view?.findViewById<android.widget.TextView>(R.id.tv_order_pc_order_invoice_number)?.apply {
            text = invoiceNumberHeader
            visibility = if (invoiceNumberHeader != null) View.VISIBLE else View.GONE
        }

        view?.findViewById<android.widget.TextView>(R.id.tv_order_pc_order_delivery_date)?.apply {
            text = deliveryDateHeader
            visibility = if (deliveryDateHeader != null) View.VISIBLE else View.GONE
        }
    }

    protected open fun setupHeaderTextAndActions(
        cancelSelectionTextView: TextView,
        prepareSelectionTextView: TextView,
        headerCheckbox: MaterialCheckBox
    ) {
        when (viewType) {
            OrderViewType.PAYMENT_COMPLETED -> {
                cancelSelectionTextView.visibility = View.VISIBLE
                prepareSelectionTextView.visibility = View.VISIBLE
                prepareSelectionTextView.text = "선택준비"
                headerCheckbox.isEnabled = true
            }
            OrderViewType.PRODUCT_READY -> {
                cancelSelectionTextView.visibility = View.VISIBLE
                prepareSelectionTextView.visibility = View.VISIBLE
                prepareSelectionTextView.text = "선택배송"
                headerCheckbox.isEnabled = true
            }
            OrderViewType.SHIPPING,
            OrderViewType.PURCHASE_CONFIRMED,
            OrderViewType.CANCELED -> {
                cancelSelectionTextView.visibility = View.GONE
                prepareSelectionTextView.visibility = View.GONE
                headerCheckbox.isEnabled = false
            }
        }
    }


    abstract fun handleNextState(order: Order)

    abstract fun handleCancel(order: Order)
}

