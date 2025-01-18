package com.nemodream.bangkkujaengi.admin.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemodream.bangkkujaengi.admin.data.repository.AdminCouponRepository
import com.nemodream.bangkkujaengi.customer.data.model.Coupon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminCouponViewModel @Inject constructor(
    private val couponRepository: AdminCouponRepository,
): ViewModel() {
    init {
        loadCoupon()
    }

    // 쿠폰 리스트
    private val _couponList = MutableLiveData<List<Coupon>>(emptyList())
    val couponList: LiveData<List<Coupon>> get() = _couponList

    // 쿠폰 로드하기
    private fun loadCoupon() = viewModelScope.launch {
        runCatching {
            couponRepository.loadCoupon()
        }.onSuccess {
            // 성공시 리스트 갱신
            _couponList.value = it
            Log.d("AdminCouponViewModel", "loadCoupon: $it")
        }.onFailure { e ->
            Log.e("AdminCouponViewModel", "loadCoupon: $e")
        }
    }

}