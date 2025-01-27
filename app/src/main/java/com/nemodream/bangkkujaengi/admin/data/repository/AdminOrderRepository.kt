package com.nemodream.bangkkujaengi.admin.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.nemodream.bangkkujaengi.admin.data.model.Order
import com.nemodream.bangkkujaengi.admin.data.model.OrderState
import com.nemodream.bangkkujaengi.customer.data.model.Purchase
import kotlinx.coroutines.tasks.await

class AdminOrderRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // 파이어스토어에서 데이터 가져오기
    suspend fun fetchOrdersByState(state: String): List<Order> {
        return try {
            val snapshot = firestore.collection("Purchase")
                .whereEqualTo("purchaseState", state)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject<Purchase>()?.let { purchase ->
                    mapPurchaseToOrder(purchase)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // 실패 시 빈 리스트 반환
        }
    }

    // Purchase 데이터를 Order로 매핑
    private fun mapPurchaseToOrder(purchase: Purchase): Order {
        return Order(
            orderDate = purchase.purchaseDateTime.ifEmpty { purchase.purchaseDate.toDate().toString() },
            productName = purchase.productTitle,
            customerId = purchase.memberId,
            orderNumber = purchase.purchaseInvoiceNumber.toString(),
            deliveryStatus = purchase.purchaseState,
            invoiceNumber = purchase.purchaseInvoiceNumber.toString(),
            state = mapPurchaseStateToOrderState(purchase.purchaseState)
        )
    }

    private fun mapPurchaseStateToOrderState(purchaseState: String): OrderState {
        return when (purchaseState) {
            "결제 완료" -> OrderState.PAYMENT_COMPLETED
            "상품 준비" -> OrderState.PRODUCT_READY
            "배송 중" -> OrderState.SHIPPING
            "구매 확정" -> OrderState.PURCHASE_CONFIRMED
            "취소됨" -> OrderState.CANCELED
            else -> OrderState.PAYMENT_COMPLETED // 기본 상태
        }
    }

    suspend fun updateOrderState(orderNumber: String, newState: String) {
        try {
            val query = firestore.collection("Purchase")
                .whereEqualTo("purchaseInvoiceNumber", orderNumber.toLong())
                .get()
                .await()

            if (query.documents.isNotEmpty()) {
                val document = query.documents.first()
                firestore.collection("Purchase").document(document.id)
                    .update("purchaseState", newState)
                    .await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("주문 상태를 업데이트하는 데 실패했습니다.")
        }
    }

}