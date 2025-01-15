package com.nemodream.bangkkujaengi.customer.data.model

data class Member(
    val id: String,

    // 아이디
    val memberId: String,

    // 비밀번호
    val memberPassword: String,

    // 이름
    val memberName: String,

    // 닉네임
    val memberNickName: String,

    // 전화번호
    val memberPhoneNumber: String,

    // 프로필 사진
    val memberProfileImage: String?,

    // 적립금(임시 적립금 3000)
    val point: Int = 3000,

    // 탈퇴여부
    val isActive: Boolean,

    // 회원가입 시간
    val createAt: Long,

    // 팔로잉 수
    val followingCount:Int,

    // 팔로잉 목록
    val followingList: List<Member>,

    // 팔로워 수
    val followerCount:Int,
)