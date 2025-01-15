package com.nemodream.bangkkujaengi.customer.data.repository

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.nemodream.bangkkujaengi.customer.data.model.CategoryType
import com.nemodream.bangkkujaengi.customer.data.model.Product
import com.nemodream.bangkkujaengi.customer.data.model.Promotion
import com.nemodream.bangkkujaengi.customer.data.model.SortType
import com.nemodream.bangkkujaengi.customer.data.model.SubCategoryType
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PromotionRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
) {
    suspend fun getPromotionByTitle(
        title: String,
        sortType: SortType,
        startAfter: DocumentSnapshot? = null
    ): List<Product> {
        try {
            // 1. 프로모션의 productIds 가져오기
            val promotionSnapshot = firestore.collection("Promotion")
                .whereEqualTo("title", title)
                .get()
                .await()
                .documents.first()

            val promotion = promotionSnapshot.toPromotion()
            if (promotion.productIds.isEmpty()) return emptyList()

            // 2. 해당 프로모션의 상품들 가져오기
            var query = firestore.collection("Product")
                .whereIn(FieldPath.documentId(), promotion.productIds)
                .let { baseQuery ->
                    when (sortType) {
                        SortType.PURCHASE -> baseQuery.orderBy(
                            "purchaseCount",
                            Query.Direction.DESCENDING
                        )

                        SortType.REVIEW -> baseQuery.orderBy(
                            "reviewCount",
                            Query.Direction.DESCENDING
                        )

                        SortType.PRICE_HIGH -> baseQuery.orderBy(
                            "price",
                            Query.Direction.DESCENDING
                        )

                        SortType.PRICE_LOW -> baseQuery.orderBy("price", Query.Direction.ASCENDING)
                        SortType.VIEWS -> baseQuery.orderBy("viewCount", Query.Direction.DESCENDING)
                        SortType.DISCOUNT -> baseQuery.orderBy(
                            "saleRate",
                            Query.Direction.DESCENDING
                        )

                        SortType.LATEST -> baseQuery.orderBy(
                            "createdAt",
                            Query.Direction.DESCENDING
                        )
                    }
                }

            if (startAfter != null) {
                query = query.startAfter(startAfter)
            }

            // 3. 프로모션 필터 적용
            val products = query.get().await().documents.map { it.toProduct() }
            val filteredProducts =
                if (promotion.filterField != null && promotion.filterValue != null) {
                    products.filter {
                        when (promotion.filterType) {
                            "GREATER_THAN" -> it.getFieldValue(promotion.filterField) > promotion.filterValue
                            else -> true
                        }
                    }
                } else products

            return filteredProducts.take(10)

        } catch (e: Exception) {
            Log.e("PromotionRepository", "Error fetching promotion data", e)
            throw e
        }
    }

    private fun Product.getFieldValue(fieldName: String): Int {
        return when (fieldName) {
            "purchaseCount" -> purchaseCount
            "reviewCount" -> reviewCount
            "price" -> price
            "viewCount" -> viewCount
            "saleRate" -> saleRate
            else -> 0
        }
    }

    private fun DocumentSnapshot.toPromotion(): Promotion =
        Promotion(
            title = getString("title") ?: "",
            order = getLong("order")?.toInt() ?: 0,
            isActive = getBoolean("isActive") ?: false,
            startDate = getTimestamp("startDate")?.seconds ?: 0L,
            endDate = getTimestamp("endDate")?.seconds ?: 0L,
            productIds = (get("productIds") as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
            filterField = getString("filterField"),
            filterValue = getLong("filterValue")?.toInt(),
            filterType = getString("filterType")
        )

    private suspend fun DocumentSnapshot.toProduct(): Product {
        val imageRefs = get("images") as? List<String> ?: emptyList()
        val imageUrls = imageRefs.map { imagePath -> getImageUrl(imagePath) }

        return Product(
            productId = id,
            productName = getString("productName") ?: "",
            description = getString("description") ?: "",
            images = imageUrls,
            isBest = getBoolean("isBest") ?: false,
            category = CategoryType.fromString(getString("category") ?: ""),
            subCategory = SubCategoryType.fromString(getString("subCategory") ?: ""),
            price = getLong("price")?.toInt() ?: 0,
            productCount = getLong("productCount")?.toInt() ?: 0,
            saledPrice = getLong("saledPrice")?.toInt() ?: 0,
            saleRate = getLong("saleRate")?.toInt() ?: 0,
            purchaseCount = getLong("purchaseCount")?.toInt() ?: 0,
            reviewCount = getLong("reviewCount")?.toInt() ?: 0,
            viewCount = getLong("viewCount")?.toInt() ?: 0,
            createdAt = getTimestamp("createdAt")?.seconds ?: 0L,
        )
    }

    private suspend fun getImageUrl(imagePath: String) = storage.reference
        .child(imagePath)
        .downloadUrl
        .await()
        .toString()
}