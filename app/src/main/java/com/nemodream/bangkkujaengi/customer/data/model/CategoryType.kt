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

enum class SubCategoryType(val categoryType: CategoryType, val title: String) {
    // 전체 카테고리 옵션들
    ALL_FURNITURE(CategoryType.FURNITURE, "전체"),
    ALL_LIGHTING(CategoryType.LIGHTING, "전체"),
    ALL_FABRIC(CategoryType.FABRIC, "전체"),
    ALL_DECO(CategoryType.DECO, "전체"),
    ALL_BROADCAST(CategoryType.BROADCAST, "전체"),

    // 가구 서브카테고리
    CHAIR(CategoryType.FURNITURE, "의자"),
    TABLE(CategoryType.FURNITURE, "테이블/식탁/책상"),
    DRAWER(CategoryType.FURNITURE, "서랍/수납장"),
    VANITY(CategoryType.FURNITURE, "화장대"),
    DISPLAY_CABINET(CategoryType.FURNITURE, "진열장"),

    // 조명 서브카테고리
    LONG_STAND(CategoryType.LIGHTING, "장스탠드"),
    SHORT_STAND(CategoryType.LIGHTING, "단스탠드"),
    MOOD_LIGHT(CategoryType.LIGHTING, "무드등"),
    FLUORESCENT(CategoryType.LIGHTING, "형광등"),

    // 패브릭 서브카테고리
    BLANKET(CategoryType.FABRIC, "이불"),
    PILLOW(CategoryType.FABRIC, "베개/베개커버"),
    MATTRESS_COVER(CategoryType.FABRIC, "매트리스 커버"),
    RUG(CategoryType.FABRIC, "러그"),
    CURTAIN(CategoryType.FABRIC, "커튼"),

    // 데코/식물 서브카테고리
    PLANT(CategoryType.DECO, "식물"),
    INTERIOR_ITEM(CategoryType.DECO, "인테리어 소품"),
    CLOCK(CategoryType.DECO, "시계"),
    DIFFUSER(CategoryType.DECO, "디퓨저/캔들"),
    DESK_DECO(CategoryType.DECO, "데스크");

    companion object {
        fun getSubCategories(categoryType: CategoryType): List<SubCategoryType> {
            // CategoryType.ALL인 경우에는 빈 리스트 반환
            if (categoryType == CategoryType.ALL) return emptyList()
            return entries.filter { it.categoryType == categoryType }
        }

        fun fromString(type: String): SubCategoryType {
            return valueOf(type.uppercase())
        }
    }
}