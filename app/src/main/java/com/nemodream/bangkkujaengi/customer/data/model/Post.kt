package com.nemodream.bangkkujaengi.customer.data.model

data class Post(
    // 게시글 고유 아이디
    val id: Int,

    // 작성자 아이디
    val nickname: String,

    // 게시글 제목
    val title: String,

    // 게시글 내용
    val content: String,

    // 게시 사진들
    val imageList: List<String>,

    // 사진 위 태그들
    // val productTagPinList : List<Tag>,

    // 좋아요 수
    val likeCount: Int = 0,

    // 댓글 수
    val commentCount: Int = 0,


    // val createAt: Long = System.currentMillis(), 게시글 작성일시
    // isDelete: Boolean = false
    // val createAt: Long = System.currentMillis()
)
