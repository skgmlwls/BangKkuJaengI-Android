package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nemodream.bangkkujaengi.customer.data.model.Product
import com.nemodream.bangkkujaengi.customer.data.repository.PaymentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PaymentViewModel() : ViewModel() {

    // 배송지 이름
    val til_payment_name_text = MutableLiveData<String>()
    // 배송지 전화번호
    val til_payment_phone_number_text = MutableLiveData<String>()
    // 배송지 주소
    val til_payment_address_text = MutableLiveData<String>()

    // 선택된 쿠폰 position
    var checked_position = MutableLiveData<Int>()
    // 선택된 쿠폰 document_id
    var checked_document_id = MutableLiveData<List<String>>()

}