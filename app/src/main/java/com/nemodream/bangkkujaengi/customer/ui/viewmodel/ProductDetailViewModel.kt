package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemodream.bangkkujaengi.customer.data.model.Product
import com.nemodream.bangkkujaengi.customer.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: HomeRepository,
): ViewModel() {
    private var _product = MutableLiveData<Product>()
    val product: LiveData<Product> = _product

    // 장바구니 담을 상품 수량
    private var _quantity = MutableLiveData(1)
    val quantity: LiveData<Int> = _quantity

    fun loadProduct(productId: String) = viewModelScope.launch {
        runCatching {
            repository.getProducts(productId)
        }.onSuccess {
            _product.value = it
        }.onFailure {
            Log.d("ProductDetailViewModel", "loadProduct: ${it.message}")
        }
    }

    // 수량 업데이트
    fun updateQuantity(increase: Boolean) {
        val currentQuantity = _quantity.value ?: 1
        val newQuantity = if (increase) {
            (currentQuantity + 1).coerceAtMost(10)
        } else {
            (currentQuantity - 1).coerceAtLeast(1)
        }
        _quantity.value = newQuantity
    }

    // 장바구니 정보 저장
    fun saveCartProduct(productId: String) = viewModelScope.launch {
        val currentQuantity = _quantity.value ?: 1

        runCatching {
            repository.saveCartProduct(productId, currentQuantity)
        }.onSuccess {
            Log.d("ProductDetailViewModel", "saveCartProduct: $it")
        }.onFailure {
            Log.d("ProductDetailViewModel", "saveCartProduct: ${it.message}")
        }
    }
}