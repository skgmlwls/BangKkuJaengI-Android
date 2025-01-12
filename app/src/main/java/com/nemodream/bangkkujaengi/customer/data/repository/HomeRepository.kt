package com.nemodream.bangkkujaengi.customer.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nemodream.bangkkujaengi.customer.data.model.Banner
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
            .map { doc -> // 상대경로를 통해 storage에서 이미지 불러오기
                doc.toBanner().copy(thumbnailImageRef = getImageUrl(doc))
            }

    /*
    * Firebase Storage에서 상대 경로에 맞는 이미지 가져오기
    * */
    private suspend fun getImageUrl(snapshot: DocumentSnapshot) = storage.reference
        .child(snapshot.getString("imageUrl") ?: "")
        .downloadUrl
        .await()
        .toString()

    /*
    * Firebase Firestore에서 배너 데이터 가져오기
    * */
    private fun DocumentSnapshot.toBanner(): Banner =
        Banner(
            id = id,
            productId = getString("productId") ?: "",
            thumbnailImageRef = getString("imageUrl") ?: ""
        )
}