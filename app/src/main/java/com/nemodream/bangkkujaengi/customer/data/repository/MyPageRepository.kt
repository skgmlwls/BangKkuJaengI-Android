package com.nemodream.bangkkujaengi.customer.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nemodream.bangkkujaengi.customer.data.model.Member
import com.nemodream.bangkkujaengi.customer.data.model.Product
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPageRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
) {

    // 현재 로그인된 사용자 정보를 받아온다.
    suspend fun getMemberInfo(memberDocId: String): Member {
        // 현재 로그인된 사용자 정보를 받아온다.
        return firestore.collection("Member")
            .document(memberDocId)
            .get()
            .await()
            .toObject(Member::class.java)!!
    }

    suspend fun loadRecentProduct(userId: String): List<Product> {
        return try {
            val snapshot = firestore.collection("RecentProduct")
                .document(userId)
                .get()
                .await()

            val productIds = snapshot.get("productIds") as? List<String> ?: return emptyList()
            val products = productIds.mapNotNull { productId ->
                firestore.collection("Product")
                    .document(productId)
                    .get()
                    .await()
                    .toObject(Product::class.java)
            }

            products
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getImageUrl(imagePath: String) = storage.reference
        .child(imagePath)
        .downloadUrl
        .await()
        .toString()
}