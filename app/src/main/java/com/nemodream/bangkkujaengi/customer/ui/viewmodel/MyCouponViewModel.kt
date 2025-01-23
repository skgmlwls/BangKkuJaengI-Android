package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemodream.bangkkujaengi.customer.data.model.Coupon
import com.nemodream.bangkkujaengi.customer.data.repository.CouponRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCouponViewModel @Inject constructor(
    private val couponRepository: CouponRepository,
): ViewModel() {

    // 쿠폰 리스트
    private val _couponList = MutableLiveData<List<Coupon>>(emptyList())
    val couponList: LiveData<List<Coupon>> = _couponList

    // 로딩
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading


    fun getCouponList() = viewModelScope.launch {
        _isLoading.value = true
        runCatching {
            couponRepository.getCouponList()
        }.onSuccess {
            _couponList.value = it
            _isLoading.value = false
            Log.d("getCouponList", "getCouponList: $it")
        }.onFailure {
            it.printStackTrace()
            _isLoading.value = false
        }
    }
}