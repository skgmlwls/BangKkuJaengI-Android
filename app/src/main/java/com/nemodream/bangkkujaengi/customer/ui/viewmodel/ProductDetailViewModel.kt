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

    fun loadProduct(productId: String) = viewModelScope.launch {
        runCatching {
            repository.getProducts(productId)
        }.onSuccess {
            _product.value = it
        }.onFailure {
            Log.d("ProductDetailViewModel", "loadProduct: ${it.message}")
        }
    }

}