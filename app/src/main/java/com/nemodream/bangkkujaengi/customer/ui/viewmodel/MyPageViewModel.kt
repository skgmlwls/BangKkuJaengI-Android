package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemodream.bangkkujaengi.customer.data.model.Member
import com.nemodream.bangkkujaengi.customer.data.model.Product
import com.nemodream.bangkkujaengi.customer.data.repository.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val repository: MyPageRepository,
): ViewModel() {

    // 멤버 정보
    private val _memberInfo = MutableLiveData<Member>()
    val memberInfo: LiveData<Member> = _memberInfo

    // 최근 본 상품
    private val _recentProductList = MutableLiveData<List<Product>>(emptyList())
    val recentProductList: LiveData<List<Product>> = _recentProductList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    // 멤버 정보 가져오기
    fun getMemberInfo(memberDocId: String) = viewModelScope.launch {
        runCatching {
            repository.getMemberInfo(memberDocId)
        }.onSuccess {
            _memberInfo.value = it
        }.onFailure {
            it.printStackTrace()
        }
    }

    // 최근 본 상품을 불러오는 코드
    fun loadRecentProduct(userId: String) = viewModelScope.launch {
        runCatching {
            _isLoading.value = true
            val products = repository.loadRecentProduct(userId)
            products.map { product ->
                val imageUrl = repository.getImageUrl(product.images.first())
                product.copy(images = listOf(imageUrl))
            }
        }.onSuccess {
            _recentProductList.value = it
            _isLoading.value = false
        }.onFailure {
            it.printStackTrace()
            _isLoading.value = false
        }
    }

}