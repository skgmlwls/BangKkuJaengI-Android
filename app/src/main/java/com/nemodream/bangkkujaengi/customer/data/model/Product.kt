package com.nemodream.bangkkujaengi.customer.data.model

data class Product(
    val productId: String = "",
    val productName: String = "",
    val description: String = "",
    val images: List<String> = emptyList(),
    val isBest: Boolean = false,
    val category: CategoryType = CategoryType.ALL,  // develop 브랜치에서 추가된 필드
    val price: Int = 0,
    val productCount: Int = 0,
    val saleRate: Int = 0,        // ShoppingCart2 브랜치에서 추가된 필드
    val saledPrice: Int = 0,
    val searchKeywords: List<String> = productName.split(" "),

)