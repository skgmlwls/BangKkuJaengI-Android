package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemodream.bangkkujaengi.customer.data.model.CategoryType
import com.nemodream.bangkkujaengi.customer.data.model.Product
import com.nemodream.bangkkujaengi.customer.data.model.SubCategoryType
import com.nemodream.bangkkujaengi.customer.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryProductViewModel @Inject constructor(
    private val repository: ProductRepository,
) : ViewModel() {
    // 카테고리를 저장하는 LiveData
    private val _categoryType = MutableLiveData<CategoryType>()

    // 선택된 서브카테고리를 저장하는 LiveData
    private val _selectedSubCategory = MutableLiveData<SubCategoryType>()

    // 상품 목록을 저장하는 LiveData
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    /*
    * 선택된 상위 카테고리를 업데이트하고 해당 카테고리의 첫 번째 서브카테고리를 자동으로 선택
    * */
    fun updateCategory(type: CategoryType) {
        _categoryType.value = type
        SubCategoryType.getSubCategories(type).firstOrNull()?.let {
            updateSubCategory(it)
        }
    }

    /*
    * 선택된 서브카테고리를 업데이트하고 해당하는 상품 목록을 가져옴
    * */
    fun updateSubCategory(subCategory: SubCategoryType) {
        _selectedSubCategory.value = subCategory
        fetchProducts()
    }

    /*
    * 현재 선택된 카테고리와 서브카테고리에 해당하는 상품 목록을 가져옴
    * 성공 시: 상품 목록 업데이트
    * 실패 시: 에러 로깅 후 빈 목록으로 설정
    * */
    private fun fetchProducts() = viewModelScope.launch {
        runCatching {
            _categoryType.value?.let { category ->
                _selectedSubCategory.value?.let { subCategory ->
                    Log.d("CategoryProductViewModel", "Fetching products - Category: ${category.name}, SubCategory: ${subCategory.title}")
                    repository.getProducts(category, subCategory)
                }
            }
        }.onSuccess { products ->
            products?.let {
                Log.d("CategoryProductViewModel", "Products fetched successfully: ${it.size} items")
                _products.value = it
            }
        }.onFailure { throwable ->
            Log.e("CategoryProductViewModel", "Failed to fetch products: ${throwable.message}")
            _products.value = emptyList()
        }
    }

}