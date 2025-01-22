package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nemodream.bangkkujaengi.customer.data.model.Product
import com.nemodream.bangkkujaengi.customer.data.model.Purchase

class OrderDetailsViewModel() : ViewModel() {

    // 구매 내역 항목 리스트
    var order_history_product_list = MutableLiveData<List<Purchase>>()
    
    // 총 상품 가격
    var tot_product_price = MutableLiveData<Int>(0)
    // 총 할인 가격
    var tot_sale_price = MutableLiveData<Int>(0)
    // 쿠폰 할인
    var sale_coupon_price = MutableLiveData<Int>(0)
    // 총 배송비
    var tot_delivery_price = MutableLiveData<Int>(0)
    // 총 결제 금액
    var tot_payment_price = MutableLiveData<Int>(0)


}