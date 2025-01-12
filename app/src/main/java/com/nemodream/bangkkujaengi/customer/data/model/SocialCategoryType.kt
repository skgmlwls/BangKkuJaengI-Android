package com.nemodream.bangkkujaengi.customer.data.model

enum class SocialCategoryType {
    DISCOVERY,
    RANK,
    FOLLOWING,
    MY;

    fun getSocialTabTitle(): String = when(this) {
        DISCOVERY -> "발견"
        RANK -> "랭킹"
        FOLLOWING -> "팔로잉"
        MY -> "내방꾸"
    }

    companion object {
        fun fromString(type: String): SocialCategoryType {
            return SocialCategoryType.valueOf(type.uppercase())
        }
    }
}