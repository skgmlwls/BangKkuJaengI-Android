package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp

class RowPaymentCouponRecyclerviewViewModel() : ViewModel() {

    // 쿠폰 이름
    val tv_row_payment_coupon_title = MutableLiveData<String>()
    // 쿠폰 할인 가격 또는 비율
    // 쿠폰 할인 금액
    val tv_row_payment_coupon_price = MutableLiveData<Int>()
    // 쿠폰 할인 비율
    val tv_row_payment_coupon_rate = MutableLiveData<Int>()

    // val tv_row_payment_coupon_price = MutableLiveData<Int>(50000)
    // 쿠폰 조건
    val tv_row_payment_coupon_condition = MutableLiveData<String>()
    // 쿠폰 기간
    val tv_row_payment_coupon_end_date = MutableLiveData<Timestamp>()

    // 쿠폰 타입 ( 할인률 or 할인금액 )
    val coupon_checked_sale_type = MutableLiveData<String>()


}