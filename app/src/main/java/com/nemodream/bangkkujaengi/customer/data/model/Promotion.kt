package com.nemodream.bangkkujaengi.customer.data.model

data class Promotion(
    val title: String,
    val order: Int,
    val isActive: Boolean,
    val startDate: Long,
    val endDate: Long,
    val productIds: List<String>,
    val filterField: String? = null,     // 필터링할 필드
    val filterValue: Int? = null,        // 필터링 기준값
    val filterType: String? = null,      // 필터링 타입
)

sealed class PromotionItem
data class PromotionHeader(val title: String) : PromotionItem()
data class PromotionProducts(val products: List<Product>) : PromotionItem()