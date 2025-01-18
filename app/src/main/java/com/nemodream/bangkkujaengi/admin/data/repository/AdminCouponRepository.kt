package com.nemodream.bangkkujaengi.admin.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.nemodream.bangkkujaengi.customer.data.model.Coupon
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AdminCouponRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) {

    // 쿠폰 데이터 Coupon 컬렉션에 저장하기
    suspend fun saveCoupon(coupon: Coupon) {
        firestore.collection("Coupon").add(coupon).await()
    }
}