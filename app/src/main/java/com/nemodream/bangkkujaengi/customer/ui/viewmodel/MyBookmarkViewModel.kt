package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemodream.bangkkujaengi.customer.data.model.Product
import com.nemodream.bangkkujaengi.customer.data.repository.MyBookmarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyBookmarkViewModel @Inject constructor(
    private val repository: MyBookmarkRepository,
): ViewModel() {

    // 상품 리스트
    private val _productList = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>> = _productList

    // 로딩 상태
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading


    fun loadProductList(userId: String) = viewModelScope.launch {
        runCatching {
            _isLoading.value = true
            repository.loadMyBookmarkList(userId)
        }.onSuccess {
            _productList.value = it
            _isLoading.value = false
        }.onFailure {
            _isLoading.value = false
            it.printStackTrace()
        }
    }

}