package com.nemodream.bangkkujaengi.admin.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminAddCouponViewModel @Inject constructor(): ViewModel() {
    private val _isSubmitEnabled = MutableLiveData(false)
    val isSubmitEnabled: LiveData<Boolean> = _isSubmitEnabled

    // 할인율 원할인 체크 버튼
    private val _isDiscountPercent = MutableLiveData(true)
    val isDiscountPercent: LiveData<Boolean> = _isDiscountPercent

    fun setDiscountPercent(isChecked: Boolean) {
        _isDiscountPercent.value = isChecked
    }

    /*
    * 입력 필드 유효성 검증
    * - 모든 필수 입력 필드와 이미지 존재 여부 확인
    * - 검증 결과에 따라 등록 버튼 활성화 상태 변경
    * */
    fun validateFields(
        title: String,
        description: String,
        limitDate: String,
        minPrice: String,
        discount: String
    ) {
        // 이미지도 함께 검증
        val isValid = title.isNotBlank() &&
                description.isNotBlank() &&
                limitDate.isNotBlank() &&
                minPrice.isNotBlank() &&
                discount.isNotBlank()
        _isSubmitEnabled.value = isValid
        Log.d("AdminAddCouponViewModel", "validateFields: $isValid")
    }
}