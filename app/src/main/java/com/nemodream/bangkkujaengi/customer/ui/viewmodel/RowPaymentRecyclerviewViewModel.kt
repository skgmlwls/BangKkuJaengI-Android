package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RowPaymentRecyclerviewViewModel() : ViewModel() {

    // 상품 이름
    var tv_row_payment_product_name = MutableLiveData<String>("상품명")
    // 상품 원가
    var tv_row_payment_product_origin_price = MutableLiveData<Int>(1000)
    // 상품 할인률
    var tv_row_payment_product_sale_percent = MutableLiveData<Int>(10)
    // 상품 할인가
    var tv_row_payment_product_sale_price = MutableLiveData<Int>(900)

}