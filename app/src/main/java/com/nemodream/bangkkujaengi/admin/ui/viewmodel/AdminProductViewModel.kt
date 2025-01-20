package com.nemodream.bangkkujaengi.admin.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemodream.bangkkujaengi.admin.data.repository.AdminProductRepository
import com.nemodream.bangkkujaengi.customer.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminProductViewModel @Inject constructor(
    private val adminProductRepository: AdminProductRepository,
): ViewModel() {
    private val _products = MutableLiveData<List<Product>>(emptyList())
    val products: LiveData<List<Product>> = _products

    init {
        loadProducts()
    }

    private fun loadProducts() = viewModelScope.launch {
        runCatching {
            adminProductRepository.getProducts()
        }.onSuccess { products ->
            _products.value = products
        }.onFailure {
            it.printStackTrace()
        }
    }

    fun deleteProduct(productId: String) = viewModelScope.launch {
        runCatching {
            adminProductRepository.deleteProduct(productId)
        }.onSuccess {
            // 삭제된 데이터를 리스트에서 수정한다.
            _products.value = _products.value?.filter { it.productId != productId }
        }.onFailure {
            it.printStackTrace()
        }
    }
}