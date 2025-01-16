package com.nemodream.bangkkujaengi.customer.data.model

data class ShoppingCart(
    var items : List<Cart> = emptyList(),
    var userId : String = ""
)

data class Cart(
    var productId: String = "",
    var quantity: Int = 0,
    var color: String = "",
    var checked: Boolean = false
)