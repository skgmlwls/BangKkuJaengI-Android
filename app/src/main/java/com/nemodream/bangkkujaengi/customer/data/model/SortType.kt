package com.nemodream.bangkkujaengi.customer.data.model

enum class SortType {
    PURCHASE,    // 구매순
    REVIEW,      // 리뷰순
    PRICE_HIGH,  // 가격 높은순
    PRICE_LOW,   // 가격 낮은순
    VIEWS,       // 조회순
    LATEST,      // 최신순
    DISCOUNT;    // 할인순

    fun toDisplayString(): String {
        return when (this) {
            PURCHASE -> "구매 많은 순"
            REVIEW -> "리뷰 많은 순"
            PRICE_HIGH -> "가격 높은 순"
            PRICE_LOW -> "가격 낮은 순"
            VIEWS -> "조회 많은 순"
            LATEST -> "최신순"
            DISCOUNT -> "할인율 높은 순"
        }
    }
}