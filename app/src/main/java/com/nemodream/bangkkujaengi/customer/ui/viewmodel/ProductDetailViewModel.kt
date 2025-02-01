package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemodream.bangkkujaengi.customer.data.model.Product
import com.nemodream.bangkkujaengi.customer.data.model.PromotionProducts
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

    // 선택된 색상을 저장
    private var _selectedColor = MutableLiveData<String>(null)
    val selectedColor: LiveData<String> = _selectedColor

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadProduct(productId: String, userId: String) = viewModelScope.launch {
        runCatching {
            _isLoading.value = true
            repository.getProducts(productId, userId)
        }.onSuccess {
            _product.value = it
            _isLoading.value = false
        }.onFailure {
            Log.d("ProductDetailViewModel", "loadProduct: ${it.message}")
            _isLoading.value = false
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

    // 선택 색상 업데이트
    fun updateSelectedColor(color: String) {
        _selectedColor.value = color
    }

    // 장바구니 정보 저장
    fun saveCartProduct(productId: String) = viewModelScope.launch {
        val currentQuantity = _quantity.value ?: 1
        runCatching {
            repository.saveCartProduct(productId, currentQuantity, _selectedColor.value ?: "")
        }.onSuccess {
            Log.d("ProductDetailViewModel", "saveCartProduct: $it")
        }.onFailure {
            Log.d("ProductDetailViewModel", "saveCartProduct: ${it.message}")
        }
    }

    fun toggleFavorite(memberId: String, productId: String) = viewModelScope.launch {
        runCatching {
            repository.toggleProductLikeState(memberId, productId)
        }.onSuccess {
            _product.value = _product.value?.copy(
                like = !(_product.value?.like ?: false),
                likeCount = if (_product.value?.like != true)
                    (_product.value?.likeCount ?: 0) + 1
                else
                    (_product.value?.likeCount ?: 0) - 1
            )
        }.onFailure { e ->
            Log.e("HomeViewModel", "좋아요 상태 변경 실패: ", e)
        }
    }

    // 최근 본 상품 저장
    fun saveRecentViewProduct(productId: String, userId: String) = viewModelScope.launch {
        runCatching {
            repository.saveRecentViewProduct(productId, userId)
        }.onSuccess {
            Log.d("ProductDetailViewModel", "saveRecentViewProduct: $it")
        }.onFailure {
                Log.d("ProductDetailViewModel", "saveRecentViewProduct: ${it.message}")
            }

    }
}