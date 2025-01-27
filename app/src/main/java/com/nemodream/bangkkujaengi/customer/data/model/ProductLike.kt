package com.nemodream.bangkkujaengi.customer.data.model

data class ProductLike(
    val userId: String = "",
    val productIds: List<String> = emptyList(),
    val updatedAt: Long = System.currentTimeMillis()
)