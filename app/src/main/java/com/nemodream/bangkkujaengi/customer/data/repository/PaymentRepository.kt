package com.nemodream.bangkkujaengi.customer.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.nemodream.bangkkujaengi.customer.data.model.Cart
import com.nemodream.bangkkujaengi.customer.data.model.Coupon
import com.nemodream.bangkkujaengi.customer.data.model.Member
import com.nemodream.bangkkujaengi.customer.data.model.PaymentItems
import com.nemodream.bangkkujaengi.customer.data.model.PaymentProduct
import com.nemodream.bangkkujaengi.customer.data.model.Product
import com.nemodream.bangkkujaengi.customer.data.model.Promotion
import com.nemodream.bangkkujaengi.customer.data.model.ShoppingCart
import kotlinx.coroutines.tasks.await
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

class PaymentRepository {

    companion object {

        // 장바구니에서 체크 표시 된 상품 데이터만 가져오는 메소드
        suspend fun getting_payment_product_by_checked(user_id: String): MutableList<Map<String, *>> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("Cart")
            val result = collectionReference.whereEqualTo("userId", user_id).get().await()

            val checked_cart_item_list = mutableListOf<Map<String, *>>()

            result.forEach { document ->
                val cartData = document.toObject(PaymentProduct::class.java)

                // checked가 true인 항목만 필터링
                val filteredItems = cartData.items.filter { it.checked }

                // 필터링된 항목이 있다면 결과 리스트에 추가
                if (filteredItems.isNotEmpty()) {
                    val filteredCartData = PaymentProduct(
                        items = filteredItems,  // 필터링된 항목만 포함
                        userId = cartData.userId
                    )
                    val map = mapOf(
                        "cart_document_id" to document.id,
                        "checked_cart_data" to filteredCartData // 필터링된 데이터만 추가
                    )
                    checked_cart_item_list.add(map)
                }
            }

            // 디버깅용 로그 추가
            checked_cart_item_list.forEach { map ->
                val cartData = map["checked_cart_data"] as? PaymentProduct
                cartData?.items?.forEach { item ->
                    Log.d("test1010", "Checked Product ID: ${item.productId}, Quantity: ${item.quantity}")
                }
            }

            return checked_cart_item_list
        }

        // 상품 문서 id 리스트로 상품 가져오는 메소드
        suspend fun getting_prodcut_by_product_document_id(product_document_id_list: List<String>): MutableList<Map<String, *>> {
            Log.d("test200", "test2: ${product_document_id_list}")
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("Product")

            // 결과를 저장할 리스트
            val product_list = mutableListOf<Map<String, *>>()

            product_document_id_list.forEach {
                val result = collectionReference.document(it).get().await()
                val map = mapOf(
                    "product_document_id" to it,
                    "product_data" to result.toObject(Product::class.java)
                )
                product_list.add(map)
            }

            product_list.forEach {
                Log.d("test200", "getting_prodcut_by_product_document_id: ${it["product_document_id"]}")
                Log.d("test200", "getting_prodcut_by_product_document_id: ${it["product_data"]}")
            }

            return product_list
        }


        // 유저 id를 통해 유저가 보유한 쿠폰 가져오는 메소드
        suspend fun getting_coupon_document_id_by_user_id(user_id: String): List<String> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("Member")

            return try {
                // Firestore에서 userId로 문서 검색
                val result = collectionReference.whereEqualTo("memberId", user_id).get().await()

                // couponDocumentId 필드 추출
                val couponDocumentIds = mutableListOf<String>()

                for (document in result.documents) {
                    // couponDocumentId 필드를 가져옴
                    val couponIds = document.get("couponDocumentId") as? List<*>

                    // String으로 변환 가능한 값만 필터링하여 추가
                    couponIds?.filterIsInstance<String>()?.let {
                        couponDocumentIds.addAll(it)
                    }
                }

                // 결과 반환
                couponDocumentIds
            } catch (e: Exception) {
                // 예외 발생 시 로그 출력 및 빈 리스트 반환
                Log.e("FirestoreError", "Error fetching couponDocumentId: ${e.message}")
                emptyList()
            }
        }


        // 유저가 가지고있는 쿠폰의 정보를 가져오는 메소드
        suspend fun getting_coupon_all(coupon_document_id_list: List<String>): MutableList<Coupon> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("Coupon")
            Log.d("coupon", "coupon_document_id_list: $coupon_document_id_list")
            val couponList = mutableListOf<Coupon>()

            try {
                val result = collectionReference.whereEqualTo("isActivity", true).get().await()
                Log.d("coupon", "Fetched documents: ${result.documents.size}")

                for (document in result.documents) {
                    val couponData = document.toObject(Coupon::class.java)?.copy(documentId = document.id)
                    if (couponData != null && coupon_document_id_list.contains(document.id)) {
                        couponList.add(couponData)
                    }
                }

                Log.d("coupon", "Final couponList: $couponList")
            } catch (e: Exception) {
                Log.e("coupon", "Error fetching coupons: ${e.message}", e)
            }

            return couponList
        }


//        suspend fun getting_coupon_by_select_coupon_document_id(couponDocumentIdList: List<String>): MutableList<Coupon> {
//            val firestore = FirebaseFirestore.getInstance()
//            val collectionReference = firestore.collection("Coupon")
//            Log.d("coupon2", "coupon_document_id_list: $couponDocumentIdList")
//
//            val couponList = mutableListOf<Coupon>()
//
//            if (couponDocumentIdList.isEmpty()) {
//                Log.d("coupon2", "No document IDs provided")
//                return couponList
//            }
//
//            for (documentId in couponDocumentIdList) {
//                val documentSnapshot = collectionReference.document(documentId).get().await()
//
//                if (documentSnapshot.exists()) {
//                    val coupon = documentSnapshot.toObject(Coupon::class.java)?.copy(documentId = documentId)
//                    if (coupon != null) {
//                        couponList.add(coupon)
//                    } else {
//                        Log.d("coupon2", "Document $documentId could not be converted to Coupon object")
//                    }
//                } else {
//                    Log.d("coupon2", "Document $documentId does not exist")
//                }
//            }
//
//            return couponList
//        }

        // 상품 document id로 상품 정보 가져오기
        suspend fun getting_coupon_by_select_coupon_document_id(couponDocumentIdList: List<String>): MutableList<Map<String, *>> {
            Log.d("test200", "test2: ${couponDocumentIdList}")
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("Coupon")

            // 결과를 저장할 리스트
            val coupon_list = mutableListOf<Map<String, *>>()

            couponDocumentIdList.forEach {
                val result = collectionReference.document(it).get().await()
                val map = mapOf(
                    "coupon_document_id" to it,
                    "coupon_data" to result.toObject(Coupon::class.java)
                )
                coupon_list.add(map)
            }

            coupon_list.forEach {
                Log.d("test200", "getting_prodcut_by_product_document_id: ${it["product_document_id"]}")
                Log.d("test200", "getting_prodcut_by_product_document_id: ${it["product_data"]}")
            }

            return coupon_list
        }



    }


}