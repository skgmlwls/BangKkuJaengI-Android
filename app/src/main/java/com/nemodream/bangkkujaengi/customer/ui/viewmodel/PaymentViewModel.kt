package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nemodream.bangkkujaengi.customer.data.model.Coupon
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

    // 결제할 상품 정보를 담을 리스트
    var payment_product_data_list = MutableLiveData<MutableList<Product>>().apply {
        value = mutableListOf() // 초기화
    }

    // 선택된 쿠폰 목록을 담을 리스트
    var select_coupon_list = MutableLiveData<MutableList<Coupon>>().apply {
        value = mutableListOf()
    }

    // 선택된 쿠폰 position
    var checked_position = MutableLiveData<Int>()

    // 총 상품 가격
    val tv_payment_tot_price_text = MutableLiveData<Int>(0)
    // 총 할인 가격
    val tv_payment_tot_sale_price_text = MutableLiveData<Int>(0)
    // 쿠폰 할인
    val tv_payment_coupon_sale_price_text = MutableLiveData<Int>(0)
    // 총 배송비
    val tv_payment_tot_delivery_cost_text = MutableLiveData<Int>(3000)
    // 총 합 금액
    val tv_payment_tot_sum_price_text = MutableLiveData<Int>(0)

}