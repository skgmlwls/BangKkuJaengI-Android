package com.nemodream.bangkkujaengi.admin.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nemodream.bangkkujaengi.customer.data.model.Product
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminProductRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
) {

    /**
     * 상품 이미지를 Storage에 업로드하고, 상품 정보를 Firestore에 저장한다.
     */
    fun addProduct(product: Product, imageUris: List<Uri>) {
        val imageUrls = imageUris.mapIndexed { index, uri ->
            uploadImage(uri, index)
        }

        uploadProduct(product.copy(images = imageUrls))
    }

    /**
     * Firebase Firestore에 상품을 업로드한다.
     */
    private fun uploadProduct(product: Product) {
        val docRef = firestore.collection("Product").document()
        val updatedProduct = product.copy(productId = docRef.id)
        docRef.set(updatedProduct)
    }

    /**
     * Firebase Storage에 이미지를 업로드한다.
     */
    private fun uploadImage(uri: Uri, index: Int): String {
        val fileName = "product/${System.currentTimeMillis()}_${index}.jpg"
        val imageRef = storage.reference.child(fileName)
        imageRef.putFile(uri)
        return fileName
    }

    /*
    * Firebase Firestore에서 데이터를 가져혼다.
    * */
    private suspend fun getImageUrl(imagePath: String) = storage.reference
        .child(imagePath)
        .downloadUrl
        .await()
        .toString()

    /**
     * Firebase Firestore에서 상품 데이터를 모두 가져온다.
     */
    suspend fun getProducts(): List<Product> = firestore.collection("Product")
        .get()
        .await()
        .map { document ->
            val product = document.toObject(Product::class.java)
            val imageUrls = product.images.map { path -> getImageUrl(path) }
            product.copy(images = imageUrls)
        }

    // productId를 받아 해당 상품을 삭제한다.
    fun deleteProduct(productId: String) {
        firestore.collection("Product").document(productId).delete()
    }
}