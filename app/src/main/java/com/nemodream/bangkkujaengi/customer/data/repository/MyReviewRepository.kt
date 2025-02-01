package com.nemodream.bangkkujaengi.customer.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nemodream.bangkkujaengi.customer.data.model.PurchaseItem
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MyReviewRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    suspend fun fetchMemberIdAndPurchases(documentId: String): List<PurchaseItem> {
        val memberId = firestore.collection("Member")
            .document(documentId)
            .get()
            .await()
            .getString("memberId") ?: return emptyList()

        val documents = firestore.collection("Purchase")
            .whereEqualTo("memberId", memberId)
            .whereEqualTo("purchaseState", "PURCHASE_CONFIRMED")
            .get()
            .await()
            .documents

        return documents.map { doc ->
            val productId = doc.getString("productId") ?: ""
            val imagePath = doc.getString("images") ?: ""
            val imageUrl = if (imagePath.isNotEmpty()) getImageUrl(imagePath) else ""
            PurchaseItem(
                productId = productId,
                productTitle = doc.getString("productTitle") ?: "상품명 없음",
                purchaseConfirmedDate = doc.getString("purchaseConfirmedDate") ?: "날짜 없음",
                imageUrl = imageUrl
            )
        }
    }

    // Firebase Storage에서 이미지 URL 가져오기
    private suspend fun getImageUrl(imagePath: String): String {
        return storage.reference
            .child(imagePath)
            .downloadUrl
            .await()
            .toString()
    }
}
