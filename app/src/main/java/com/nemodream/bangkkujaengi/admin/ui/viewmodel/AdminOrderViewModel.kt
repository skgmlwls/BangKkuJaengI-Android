package com.nemodream.bangkkujaengi.admin.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nemodream.bangkkujaengi.admin.data.model.Order
import com.nemodream.bangkkujaengi.admin.data.model.OrderState

class AdminOrderViewModel : ViewModel() {

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> get() = _orders

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

        // 데이터 갱신
        _orders.value = sampleOrders
    }
}
