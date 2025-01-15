package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PaymentViewModel() : ViewModel() {

    // 배송지 이름
    val til_payment_name_text = MutableLiveData<String>()
    // 배송지 전화번호
    val til_payment_phone_number_text = MutableLiveData<String>()
    // 배송지 주소
    val til_payment_address_text = MutableLiveData<String>()

}