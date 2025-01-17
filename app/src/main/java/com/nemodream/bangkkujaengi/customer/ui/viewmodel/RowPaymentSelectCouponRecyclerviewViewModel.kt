package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp

class RowPaymentSelectCouponRecyclerviewViewModel() : ViewModel() {

    // 쿠폰 제목
    val tv_row_payment_coupon_title = MutableLiveData<String>()

    // 쿠폰 할인 금액
    val tv_row_payment_coupon_price = MutableLiveData<Int>()
    // 쿠폰 할인 비율
    val tv_row_payment_coupon_rate = MutableLiveData<Int>()

    // 쿠폰 사용 조건
    val tv_row_payment_coupon_condition = MutableLiveData<String>()
    // 쿠폰 사용 기간
    val tv_row_payment_coupon_period = MutableLiveData<Timestamp>()

}