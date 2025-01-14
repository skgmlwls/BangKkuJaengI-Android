package com.nemodream.bangkkujaengi.customer.data.model

//data class Product(
//    val productId: String,
//    val productName: String,
//    val description: String,
//    val images: List<String>,
//    val isBest: Boolean,
//    val price: Int,
//    val productCount: Int,
//    val saledPrice: Int,
//)

data class Product(
    val productId: String = "",
    val productName: String = "",
    val description: String = "",
    val images: List<String> = emptyList(),
    val isBest: Boolean = false,
    val price: Int = 0,
    val productCount: Int = 0,
    val saleRate : Int = 0,
    val saledPrice: Int = 0,
)