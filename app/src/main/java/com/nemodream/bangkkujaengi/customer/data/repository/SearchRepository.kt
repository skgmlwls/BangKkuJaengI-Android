package com.nemodream.bangkkujaengi.customer.data.repository

import com.nemodream.bangkkujaengi.customer.data.local.dao.SearchHistoryDao
import com.nemodream.bangkkujaengi.customer.data.model.SearchHistory
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao,
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
}