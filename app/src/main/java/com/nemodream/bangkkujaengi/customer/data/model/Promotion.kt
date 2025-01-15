package com.nemodream.bangkkujaengi.customer.data.model

data class Promotion(
    val title: String,
    val order: Int,
    val isActive: Boolean = true,
    val startDate: Long = 0L,
    val endDate: Long = 0L,
    val productIds: List<String> = emptyList()
)

sealed class PromotionItem
data class PromotionHeader(val title: String) : PromotionItem()
data class PromotionProducts(val products: List<Product>) : PromotionItem()