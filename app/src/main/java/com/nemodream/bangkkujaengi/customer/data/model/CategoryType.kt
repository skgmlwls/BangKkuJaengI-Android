package com.nemodream.bangkkujaengi.customer.data.model

enum class CategoryType {
    ALL,
    FURNITURE,
    LIGHTING,
    FABRIC,
    DECO,
    BROADCAST;

    fun getTabTitle(): String = when(this) {
        ALL -> "전체"
        FURNITURE -> "가구"
        LIGHTING -> "조명"
        FABRIC -> "패브릭"
        DECO -> "데코/식물"
        BROADCAST -> "방송상품"
    }

    companion object {
        fun fromString(type: String): CategoryType {
            return valueOf(type.uppercase())
        }
    }
}