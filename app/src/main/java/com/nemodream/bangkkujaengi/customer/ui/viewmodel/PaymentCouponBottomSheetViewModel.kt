package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PaymentCouponBottomSheetViewModel() : ViewModel() {

    // 유저 아이디
    var user_id = MutableLiveData<String>()

    // 선택된 쿠폰의 포지션
    var checked_position = MutableLiveData<Int>()

    // 선택된 쿠폰의 문서 id
    val checked_document_id = MutableLiveData<String>()

    // BottomSheet가 보여지는지 여부
    val bottom_sheet_show = MutableLiveData<Boolean>(true)

    // 쿠폰 문서 id를 담을 리스트
    var coupon_document_id_list = MutableLiveData<String>()

}