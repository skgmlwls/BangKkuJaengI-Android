package com.nemodream.bangkkujaengi.customer.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.nemodream.bangkkujaengi.customer.data.model.Coupon
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CouponRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) {

    // Firebase에서 Coupon 정보를 activity가 true인 쿠폰만 가져오는 함수
    suspend fun getCouponList() = try {
        val snapshot = firestore.collection("Coupon").get().await()
        snapshot.documents.map { document ->
            document.toObject(Coupon::class.java)?.copy(
                documentId = document.id
            ) ?: throw Exception("쿠폰 정보 불러오기 실패")
        }
    } catch (e: Exception) {
        throw e
    }
}