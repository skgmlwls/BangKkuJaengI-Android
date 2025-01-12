package com.nemodream.bangkkujaengi.customer.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nemodream.bangkkujaengi.customer.data.model.Banner
import com.nemodream.bangkkujaengi.customer.data.model.Product
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
) {
    /*
    * 배너 데이터 가져오기
    * */
    suspend fun getBanners(): List<Banner> =
        firestore.collection("Banner")
            .get()
            .await()
            .documents
            .map { doc ->
                doc.toBanner().copy(thumbnailImageRef = getImageUrl(doc.getString("imageUrl") ?: ""))
            }
    /*
    * Firebase Storage에서 상대 경로에 맞는 이미지 가져오기
    * */
    private suspend fun getImageUrl(imagePath: String) = storage.reference
        .child(imagePath)
        .downloadUrl
        .await()
        .toString()

    /*
    * productId를 통해 Firebase의 Product Collection에서 해당 상품 데이터 가져오기
    * */
    suspend fun getProducts(productId: String): Product? =
        firestore.collection("Product")
            .document(productId)
            .get()
            .await()
            .toProduct()

    /*
    * Firebase Firestore에서 배너 데이터 가져오기
    * */
    private fun DocumentSnapshot.toBanner(): Banner =
        Banner(
            id = id,
            productId = getString("productId") ?: "",
            thumbnailImageRef = getString("imageUrl") ?: ""
        )

    /*
    * Firebase Firestore에서 상품 데이터 가져오기
    * */
    private suspend fun DocumentSnapshot.toProduct(): Product {
        val imageRefs = get("images") as? List<String> ?: emptyList()
        val imageUrls = imageRefs.map { imagePath -> getImageUrl(imagePath) }

        return Product(
            productId = id,
            productName = getString("productName") ?: "",
            description = getString("description") ?: "",
            images = imageUrls,
            isBest = getBoolean("isBest") ?: false,
            price = getLong("price")?.toInt() ?: 0,
            productCount = getLong("productCount")?.toInt() ?: 0,
            saledPrice = getLong("saledPrice")?.toInt() ?: 0,
        )
    }
}