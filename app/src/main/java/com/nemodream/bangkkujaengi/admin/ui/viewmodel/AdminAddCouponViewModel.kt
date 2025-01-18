package com.nemodream.bangkkujaengi.admin.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.nemodream.bangkkujaengi.admin.data.repository.AdminCouponRepository
import com.nemodream.bangkkujaengi.customer.data.model.Coupon
import com.nemodream.bangkkujaengi.customer.data.model.CouponType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AdminAddCouponViewModel @Inject constructor(
    private val adminCouponRepository: AdminCouponRepository,
): ViewModel() {
    private val _isSubmitEnabled = MutableLiveData(false)
    val isSubmitEnabled: LiveData<Boolean> = _isSubmitEnabled

    // 할인율/원할인 체크 버튼 (true: 할인율, false: 원할인)
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
        discount: String,
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


    /*
    * 쿠폰 데이터 만들기
    * 타입이 RATE면 할인율에 값을 넣고
    * PRICE면 할인 가격에 값을 넣는데
    * 다른 곳에는 0
    * */
    fun createAndSaveCoupon(
        title: String,
        description: String,
        limitDate: String,
        minPrice: String,
        discount: String,
    ) = viewModelScope.launch {
        runCatching {
            val isPercentDiscount = isDiscountPercent.value ?: true

            // limitDate(yyyy-MM-dd)를 Timestamp로 변환
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val endDate = dateFormat.parse(limitDate)?.time?.let {
                Timestamp(it / 1000, 0)
            }

            Coupon(
                id = "",  // ID는 Firebase에서 자동 생성
                title = title,
                conditionDescription = description,
                startCouponDate = Timestamp.now(),  // 현재 시간부터
                endCouponDate = endDate,  // 입력받은 limitDate
                couponDataLimit = 0,  // 기본값
                couponType = if (isPercentDiscount) {
                    CouponType.SALE_RATE.str
                } else {
                    CouponType.SALE_PRICE.str
                },
                salePrice = if (!isPercentDiscount) discount.toIntOrNull() ?: 0 else 0,
                saleRate = if (isPercentDiscount) discount.toIntOrNull() ?: 0 else 0,
                morePrice = minPrice.toIntOrNull() ?: 0,
                isActivity = true  // 생성 시 활성화 상태
            )
        }.onSuccess { coupon ->
            // 성공 처리
            adminCouponRepository.saveCoupon(coupon)
        }.onFailure { e ->
            // 에러 처리
        }
    }
}