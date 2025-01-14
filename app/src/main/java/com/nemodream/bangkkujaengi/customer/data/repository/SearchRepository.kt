package com.nemodream.bangkkujaengi.customer.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nemodream.bangkkujaengi.customer.data.local.dao.SearchHistoryDao
import com.nemodream.bangkkujaengi.customer.data.model.CategoryType
import com.nemodream.bangkkujaengi.customer.data.model.Product
import com.nemodream.bangkkujaengi.customer.data.model.SearchHistory
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    ) {
    suspend fun getAllSearchHistory(): List<SearchHistory> =
        searchHistoryDao.getSearchHistoryList()

    suspend fun addSearch(query: String) {
        val existingSearch = searchHistoryDao.findSearchByQuery(query)
        if (existingSearch != null) {
            searchHistoryDao.updateSearch(existingSearch.copy(timestamp = System.currentTimeMillis()))
        } else {
            searchHistoryDao.insertSearch(
                SearchHistory(
                    query = query,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun deleteSearch(search: SearchHistory) {
        searchHistoryDao.deleteSearch(search)
    }

    suspend fun deleteAllSearches() {
        searchHistoryDao.deleteAllSearches()
    }

    /*
    * firebase에서 productName이 포함되는 상품 정보를 가져온다.
    * */
    suspend fun getProductsByKeyword(keyword: String): List<Product> {
        return firestore.collection("Product")
            .whereArrayContains("searchKeywords", keyword)
            .get()
            .await()
            .documents
            .mapNotNull { it.toProduct() }
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
            category = CategoryType.fromString(getString("category") ?: ""),
            price = getLong("price")?.toInt() ?: 0,
            productCount = getLong("productCount")?.toInt() ?: 0,
            saledPrice = getLong("saledPrice")?.toInt() ?: 0,
            saledRate = getLong("saledRate")?.toInt() ?: 0,
        )
    }
}