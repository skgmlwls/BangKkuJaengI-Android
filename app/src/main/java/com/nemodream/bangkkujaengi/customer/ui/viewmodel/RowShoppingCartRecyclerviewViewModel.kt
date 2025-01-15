package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RowShoppingCartRecyclerviewViewModel() : ViewModel() {
    // 상품 선택 여부
    var cb_row_shopping_cart_product_select_checked = MutableLiveData<Boolean>(false)
    // 상품명
    var tv_row_shopping_cart_product_name = MutableLiveData<String>("상품명")
    // 할인 비율
    var tv_row_shopping_cart_product_sale_percent = MutableLiveData<Int>(0)
    // 원가 (할인 전 가격)
    var tv_row_shopping_cart_product_origin_price = MutableLiveData<Int>(0)
    // 판매가 (할인 후 가격)
    var tv_row_shopping_cart_product_sale_price = MutableLiveData<Int>(0)
    // 수량
    var tv_row_shopping_cart_product_cnt = MutableLiveData<Int>(0)
    // 배송비
    // var tv_row_shopping_cart_product_delivery_cost = MutableLiveData<Int>(3000)

    // 이미지 경로
    var iv_row_shopping_cart_product_image = MutableLiveData<String>()

}