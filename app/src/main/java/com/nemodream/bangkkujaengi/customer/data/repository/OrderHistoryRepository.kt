package com.nemodream.bangkkujaengi.customer.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.nemodream.bangkkujaengi.customer.data.model.Purchase
import kotlinx.coroutines.tasks.await

class OrderHistoryRepository {

    companion object {

        // 주문 내역 가져오기
        suspend fun getting_order_history_list(user_id: String): List<Purchase> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("Purchase")

            return try {
                // Firestore에서 memberId로 검색하고 purchaseDate로 정렬
                val querySnapshot = collectionReference
                    .whereEqualTo("memberId", user_id) // memberId로 필터링
                    .orderBy("purchaseDate", Query.Direction.DESCENDING) // purchaseDate 기준 정렬
                    .get()
                    .await()

                // 결과를 Purchase 객체 리스트로 변환
                val orderHistoryList = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(Purchase::class.java)
                }

                orderHistoryList.forEach {
                    Log.d("OrderHistory", "Fetched order history: $it")
                }

                // Log.d("OrderHistory", "Fetched order history: $orderHistoryList")
                orderHistoryList
            } catch (e: Exception) {
                Log.e("FirestoreError", "Error fetching order history: ${e.message}", e)
                emptyList()
            }
        }

    }

}