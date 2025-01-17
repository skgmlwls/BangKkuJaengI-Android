package com.nemodream.bangkkujaengi.customer.data.model

data class PaymentProduct (
    var items : List<PaymentItems> = emptyList(),
    var userId : String = ""
)

data class PaymentItems(
    var productId: String = "",
    var quantity: Int = 0,
    var checked: Boolean = false
)