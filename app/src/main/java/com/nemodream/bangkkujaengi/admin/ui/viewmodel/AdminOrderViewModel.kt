package com.nemodream.bangkkujaengi.admin.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.checkbox.MaterialCheckBox
import com.nemodream.bangkkujaengi.admin.data.model.Order
import com.nemodream.bangkkujaengi.admin.data.model.OrderState

class AdminOrderViewModel : ViewModel() {

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> get() = _orders

    private val selectedOrders = mutableSetOf<String>()

    private val _headerCheckboxState = MutableLiveData<Int>()
    val headerCheckboxState: LiveData<Int> get() = _headerCheckboxState

    // 선택된 항목 수 LiveData
    private val _selectedItemCount = MutableLiveData<Int>()
    val selectedItemCount: LiveData<Int> get() = _selectedItemCount

    init {
        // 초기값 설정
        _selectedItemCount.value = 0
    }

    fun loadOrders(state: OrderState) {
        // 상태별 샘플 데이터 로드
        val sampleOrders = when (state) {
            OrderState.PAYMENT_COMPLETED -> listOf(
                Order("2025-01-01 16:40:23", "상품이름머머", "user1", "abcdefg1234567@e", state = OrderState.PAYMENT_COMPLETED),
                Order("2025-01-02 10:15:12", "상품이름왈라", "user2", "hijklmn8901234@e", state = OrderState.PAYMENT_COMPLETED),
                Order("2025-01-03 08:20:45", "상품이름어쩌고", "user3", "opqrst5678901@e", state = OrderState.PAYMENT_COMPLETED)
            )
            OrderState.PRODUCT_READY -> listOf(
                Order("2025-01-01 16:40:23", "상품이름A", "user1", "ORD12345", state = OrderState.PRODUCT_READY),
                Order("2025-01-02 10:15:12", "상품이름B", "user2", "ORD12346", state = OrderState.PRODUCT_READY)
            )
            OrderState.SHIPPING -> listOf(
                Order("2025-01-03 12:40:00", "상품이름C", "user3", "ORD12347", invoiceNumber = "1234567890", deliveryStatus = "배송중", deliveryStartDate = "2025-01-24 13:34:56", deliveryDate = "-", state = OrderState.SHIPPING),
                Order("2025-01-04 15:20:00", "상품이름D", "user4", "ORD12348", invoiceNumber = "0987654321", deliveryStatus = "배송완료", deliveryStartDate = "2025-01-24 13:34:56", deliveryDate = "2025-01-26 14:54:22", state = OrderState.SHIPPING)
            )
            OrderState.PURCHASE_CONFIRMED -> listOf(
                Order("2025-01-05 09:30:00", "상품이름E", "user5", "ORD12349", confirmationDate = "2025-01-23 11:35:32", state = OrderState.PURCHASE_CONFIRMED),
                Order("2025-01-06 14:00:00", "상품이름F", "user6", "ORD12350", confirmationDate = "2025-01-23 11:35:32", state = OrderState.PURCHASE_CONFIRMED)
            )
            OrderState.CANCELED -> listOf(
                Order("2025-01-07 18:45:00", "상품이름G", "user7", "ORD12351", canceledBy = "관리자", cancellationReason = "재고 부족", cancelDate = "2025-01-23 15:45:12", state = OrderState.CANCELED),
                Order("2025-01-08 12:00:00", "상품이름H", "user8", "ORD12352", canceledBy = "고객", cancellationReason = "변심", cancelDate = "2025-01-24 10:20:15", state = OrderState.CANCELED)
            )
        }

        // 데이터 초기화
        _orders.value = sampleOrders
    }

    fun handleNextState(order: Order) {
        // 다음 상태 처리 로직 (예: API 호출)
        // 필요시 LiveData를 업데이트
    }

    fun handleCancel(order: Order) {
        // 취소 처리 로직 (예: API 호출)
        // 필요시 LiveData를 업데이트
    }

    fun isOrderSelected(orderNumber: String): Boolean {
        return selectedOrders.contains(orderNumber)
    }

    fun updateOrderSelection(orderNumber: String, isChecked: Boolean) {
        if (isChecked) selectedOrders.add(orderNumber) else selectedOrders.remove(orderNumber)
        _selectedItemCount.value = selectedOrders.size
        updateCheckboxState()
    }

    fun toggleSelectAllOrders(selectAll: Boolean) {
        val currentOrders = _orders.value ?: return
        selectedOrders.clear()

        if (selectAll) {
            currentOrders.forEach { selectedOrders.add(it.orderNumber) }
        }
        _selectedItemCount.value = selectedOrders.size
        updateCheckboxState()

        // LiveData로 선택된 항목 업데이트를 트리거
        _orders.value = currentOrders.toList() // 강제로 LiveData 업데이트
    }

    fun getSelectedOrders(): List<Order> {
        return _orders.value?.filter { selectedOrders.contains(it.orderNumber) } ?: emptyList()
    }

    private fun updateCheckboxState() {
        val currentOrders = _orders.value ?: return
        val selectedCount = selectedOrders.size
        val totalCount = currentOrders.size

        _headerCheckboxState.value = when {
            selectedCount == 0 -> MaterialCheckBox.STATE_UNCHECKED
            selectedCount == totalCount -> MaterialCheckBox.STATE_CHECKED
            else -> MaterialCheckBox.STATE_INDETERMINATE
        }
    }
}
