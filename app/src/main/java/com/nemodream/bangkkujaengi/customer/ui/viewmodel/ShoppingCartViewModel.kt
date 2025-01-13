package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShoppingCartViewModel() : ViewModel() {
    // 총 상품 가격 요약 텍스트
    // 총 상품 가격
    var tv_shopping_cart_tot_price_text = MutableLiveData<Int>(0)
    // 총 할인 가격
    var tv_shopping_cart_tot_sale_price_text = MutableLiveData<Int>(0)
    // 총 배송비 가격
    var tv_shopping_cart_tot_delivery_cost_text = MutableLiveData<Int>(3000)
    // 총 합 금액
    var tv_shopping_cart_tot_sum_price_text = MutableLiveData<Int>(0)

    // 상품 가격 요약 텍스트 값 설정
    fun setting_price_text() {

    }
}