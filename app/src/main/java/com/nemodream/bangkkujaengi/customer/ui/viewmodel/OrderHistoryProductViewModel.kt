package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nemodream.bangkkujaengi.customer.data.model.Purchase

class OrderHistoryProductViewModel() : ViewModel() {

    // 구매 내역 항목 리스트
    var order_history_product_list = MutableLiveData<List<Purchase>>()

    // 구매 날짜 리스트
    var order_history_date_list = MutableLiveData<List<Int>>()

}