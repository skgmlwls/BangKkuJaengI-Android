package com.nemodream.bangkkujaengi.customer.data.repository

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.nemodream.bangkkujaengi.customer.data.model.CategoryType
import com.nemodream.bangkkujaengi.customer.data.model.Product
import com.nemodream.bangkkujaengi.customer.data.model.SortType
import com.nemodream.bangkkujaengi.customer.data.model.SubCategoryType
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
) {

    /*
    * 카테고리별 상품 리스트 가져오기
    * 대분류 카테고리와 소분류 카테고리로 데이터 가져온다.
    * */
    suspend fun getProducts(
        category: CategoryType,
        subCategory: SubCategoryType,
        sortType: SortType,
        userId: String,
    ): List<Product> {
        return try {
            // 기본 쿼리 생성
            var query = when (category) {
                CategoryType.ALL -> {
                    firestore.collection(COLLECTION_PRODUCTS)
                        .whereEqualTo("delete", false)
                }
                else -> {
                    firestore.collection(COLLECTION_PRODUCTS)
                        .whereEqualTo("delete", false)
                        .whereEqualTo("category", category.name)
                        .let { baseQuery ->
                            if (subCategory.title != "전체") {
                                baseQuery.whereEqualTo("subCategory", subCategory.name)
                            } else {
                                baseQuery
                            }
                        }
                }
            }

            // 정렬 조건 추가
            query = when (sortType) {
                SortType.PURCHASE -> query.orderBy("purchaseCount", Query.Direction.DESCENDING)
                SortType.REVIEW -> query.orderBy("reviewCount", Query.Direction.DESCENDING)
                SortType.PRICE_HIGH -> query.orderBy("price", Query.Direction.DESCENDING)
                SortType.PRICE_LOW -> query.orderBy("price", Query.Direction.ASCENDING)
                SortType.VIEWS -> query.orderBy("viewCount", Query.Direction.DESCENDING)
                SortType.LATEST -> query.orderBy("createdAt", Query.Direction.DESCENDING)
                SortType.DISCOUNT -> query.orderBy("saleRate", Query.Direction.DESCENDING)
            }

            val documents = query.get().await().documents
            Log.d("ProductRepository", "Query returned ${documents.size} documents")

            documents.mapNotNull { document ->
                try {
                    document.toProduct(userId)
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Query failed", e)
            emptyList()
        }
    }

    private suspend fun DocumentSnapshot.toProduct(userId: String): Product {
        return toObject<Product>()?.copy(
            productId = id,
            images = (get("images") as? List<String>)?.map { imagePath ->
                getImageUrl(imagePath)
            } ?: emptyList(),
            like = isProductLiked(userId, id)
        ) ?: Product()
    }

    private suspend fun getImageUrl(imagePath: String) = storage.reference
        .child(imagePath)
        .downloadUrl
        .await()
        .toString()

    /* 좋아요 토글 */
    suspend fun toggleProductLikeState(userId: String, productId: String) {
        try {
            firestore.runTransaction { transaction ->
                // 현재 좋아요 상태만 확인
                val likeDoc = transaction.get(
                    firestore.collection("ProductLike").document(userId)
                )

                val currentLikes = if (likeDoc.exists()) {
                    (likeDoc.get("productIds") as? List<String>) ?: emptyList()
                } else {
                    emptyList()
                }

                // 좋아요 상태 토글
                if (productId in currentLikes) {
                    // 좋아요 취소
                    transaction.update(
                        firestore.collection("ProductLike").document(userId),
                        mapOf(
                            "productIds" to currentLikes - productId,
                            "updatedAt" to System.currentTimeMillis()
                        )
                    )
                    transaction.update(
                        firestore.collection("Product").document(productId),
                        "likeCount", FieldValue.increment(-1)
                    )
                } else {
                    // 좋아요 추가
                    transaction.set(
                        firestore.collection("ProductLike").document(userId),
                        mapOf(
                            "userId" to userId,
                            "productIds" to currentLikes + productId,
                            "updatedAt" to System.currentTimeMillis()
                        )
                    )
                    transaction.update(
                        firestore.collection("Product").document(productId),
                        "likeCount", FieldValue.increment(1)
                    )
                }
            }.await()
        } catch (e: Exception) {
            Log.e("HomeRepository", "좋아요 상태변경 실패: ", e)
            throw e
        }
    }

    /* 상품 좋아요 여부 확인 */
    private suspend fun isProductLiked(userId: String, productId: String): Boolean {
        return try {
            val doc = firestore.collection("ProductLike")
                .document(userId)
                .get()
                .await()

            if (doc.exists()) {
                val productIds = doc.get("productIds") as? List<String>
                productIds?.contains(productId) ?: false
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "좋아요 상태변경 실패: ", e)
            false
        }
    }

    companion object {
        private const val COLLECTION_PRODUCTS = "Product"
    }
}