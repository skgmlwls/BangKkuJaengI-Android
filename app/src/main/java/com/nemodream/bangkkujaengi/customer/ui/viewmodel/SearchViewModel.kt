package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemodream.bangkkujaengi.customer.data.model.SearchHistory
import com.nemodream.bangkkujaengi.customer.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
): ViewModel() {
    // 검색 기록 프로퍼티
    private var _searchHistory = MutableLiveData<List<SearchHistory>>()
    val searchHistory: LiveData<List<SearchHistory>> get() = _searchHistory

    init {
        loadSearchHistory()
    }

    // 검색 기록 불러오기
    fun loadSearchHistory() = viewModelScope.launch {
        runCatching {
            repository.getAllSearchHistory()
        }.onSuccess {
            _searchHistory.value = it
        }.onFailure {
            it.printStackTrace()
        }
    }

    // 검색어 추가
    fun addSearch(query: String) = viewModelScope.launch {
        runCatching {
            repository.addSearch(query)
        }.onSuccess {
            loadSearchHistory()
        }.onFailure {
            it.printStackTrace()
        }
    }

    // 검색어 삭제
    fun deleteSearch(search: SearchHistory) = viewModelScope.launch {
        runCatching {
            repository.deleteSearch(search)
        }.onSuccess {
            loadSearchHistory()
        }.onFailure {
            it.printStackTrace()
        }
    }

    // 검색어 전체 삭제
    fun clearAllSearches() = viewModelScope.launch {
        runCatching {
            repository.deleteAllSearches()
        }.onSuccess {
            loadSearchHistory()
        }.onFailure {
            it.printStackTrace()
        }
    }

}