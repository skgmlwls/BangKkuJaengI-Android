package com.nemodream.bangkkujaengi.customer.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nemodream.bangkkujaengi.customer.data.model.Member
import com.nemodream.bangkkujaengi.customer.data.model.Post
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

// 하드코딩 테스트 데이터
@Singleton
class SocialMyRepository @Inject constructor() {

    // 더미 데이터 생성 (실제 구현 시 네트워크 또는 데이터베이스와 연동)
    fun getMyProfile(): Member {
        return Member(
            id = "100",
            memberId = "hyein604",
            memberPassword = "password",
            memberName = "김혜인",
            memberNickName = "방꾸쟁이",
            memberPhoneNumber = "010-1234-5678",
            memberProfileImage = "https://example.com/profile1.jpg",
            point = 3000,
            isActive = true,
            createAt = System.currentTimeMillis(),
            followingCount = 5,
            followingList = emptyList(),
            followerCount = 1,
        )
    }

    suspend fun getMyWittenPosts(): List<Post> {
        // 테스트용 더미 데이터 생성
        val allPosts = listOf(
            Post(
                id = "1",
                nickname = "소셜유저",
                authorProfilePicture = "https://example.com/image1.jpg",
                title = "내방소개",
                content = "This is a test post",
                imageList = listOf("https://example.com/image1.jpg"),
                savedCount = 5,
                commentCount = 3,
                productTagPinList = listOf()
            ),
            Post(
                id = "2",
                nickname = "소셜유저",
                authorProfilePicture = "https://example.com/image1.jpg",
                title = "두번째 내방소개",
                content = "Another test post",
                imageList = listOf("https://example.com/image2.jpg"),
                savedCount = 10,
                commentCount = 8,
                productTagPinList = listOf()
            ),
            Post(
                id = "3",
                nickname = "방꾸쟁이유저",
                authorProfilePicture = "https://example.com/image2.jpg",
                title = "첫번째 내방소개",
                content = "Third test post",
                imageList = listOf("https://example.com/image3.jpg"),
                savedCount = 2,
                commentCount = 1,
                productTagPinList = listOf()
            ),
            Post(
                id = "4",
                nickname = "방꾸쟁이유저",
                authorProfilePicture = "https://example.com/image2.jpg",
                title = "다섯번째 내방소개",
                content = "Third test post",
                imageList = listOf("https://example.com/image3.jpg"),
                savedCount = 9,
                commentCount = 0,
                productTagPinList = listOf()
            ),
            Post(
                id = "5",
                nickname = "방꾸쟁이",
                authorProfilePicture = "https://example.com/image3.jpg",
                title = "방꾸쟁이의 첫번째 글",
                content = "Third test post",
                imageList = listOf("https://example.com/image3.jpg"),
                savedCount = 0,
                commentCount = 1,
                productTagPinList = listOf()
            ),
            Post(
                id = "6",
                nickname = "방꾸쟁이",
                authorProfilePicture = "https://example.com/image2.jpg",
                title = "방꾸쟁이의 두번째 글",
                content = "Third test post",
                imageList = listOf("https://example.com/image3.jpg"),
                savedCount = 9,
                commentCount = 0,
                productTagPinList = listOf()
            ),
            Post(
                id = "7",
                nickname = "방꾸쟁이",
                authorProfilePicture = "https://example.com/image2.jpg",
                title = "방꾸쟁이의 세번째 글",
                content = "Third test post",
                imageList = listOf("https://example.com/image3.jpg"),
                savedCount = 9,
                commentCount = 0,
                productTagPinList = listOf()
            ),
            Post(
                id = "8",
                nickname = "방꾸쟁이",
                authorProfilePicture = "https://example.com/image2.jpg",
                title = "방꾸쟁이의 네번째 글",
                content = "Third test post",
                imageList = listOf("https://example.com/image3.jpg"),
                savedCount = 9,
                commentCount = 0,
                productTagPinList = listOf()
            ),
            Post(
                id = "9",
                nickname = "방꾸쟁이",
                authorProfilePicture = "https://example.com/image2.jpg",
                title = "방꾸쟁이의 다섯번째 글",
                content = "Third test post",
                imageList = listOf("https://example.com/image3.jpg"),
                savedCount = 9,
                commentCount = 0,
                productTagPinList = listOf()
            ),
        )
        // 현재 로그인 유저 닉네임으로 필터링
        return allPosts.filter { it.nickname == getMyProfile().memberNickName }
    }

    suspend fun getMySavedPosts(): List<Post> {
        // 테스트용 더미 데이터 생성
        val savedPosts = listOf(
            Post(
                id = "1",
                nickname = "소셜유저",
                authorProfilePicture = "https://example.com/image1.jpg",
                title = "내방소개",
                content = "This is a test post",
                imageList = listOf("https://example.com/image1.jpg"),
                savedCount = 5,
                commentCount = 3,
                productTagPinList = listOf()
            ),
            Post(
                id = "2",
                nickname = "소셜유저",
                authorProfilePicture = "https://example.com/image1.jpg",
                title = "두번째 내방소개",
                content = "Another test post",
                imageList = listOf("https://example.com/image2.jpg"),
                savedCount = 10,
                commentCount = 8,
                productTagPinList = listOf()
            ),
            Post(
                id = "3",
                nickname = "방꾸쟁이유저",
                authorProfilePicture = "https://example.com/image2.jpg",
                title = "첫번째 내방소개",
                content = "Third test post",
                imageList = listOf("https://example.com/image3.jpg"),
                savedCount = 2,
                commentCount = 1,
                productTagPinList = listOf()
            ),
            Post(
                id = "4",
                nickname = "방꾸쟁이유저",
                authorProfilePicture = "https://example.com/image2.jpg",
                title = "다섯번째 내방소개",
                content = "Third test post",
                imageList = listOf("https://example.com/image3.jpg"),
                savedCount = 9,
                commentCount = 0,
                productTagPinList = listOf()
            ),
            Post(
                id = "5",
                nickname = "김혜인",
                authorProfilePicture = "https://example.com/image3.jpg",
                title = "방청소하기",
                content = "Third test post",
                imageList = listOf("https://example.com/image3.jpg"),
                savedCount = 0,
                commentCount = 1,
                productTagPinList = listOf()
            ),
            Post(
                id = "6",
                nickname = "방꾸쟁이",
                authorProfilePicture = "https://example.com/image2.jpg",
                title = "다섯번째 내방소개",
                content = "Third test post",
                imageList = listOf("https://example.com/image3.jpg"),
                savedCount = 9,
                commentCount = 0,
                productTagPinList = listOf()
            ),
            Post(
                id = "7",
                nickname = "방꾸쟁이",
                authorProfilePicture = "https://example.com/image2.jpg",
                title = "다섯번째 내방소개",
                content = "Third test post",
                imageList = listOf("https://example.com/image3.jpg"),
                savedCount = 9,
                commentCount = 0,
                productTagPinList = listOf()
            ),
            Post(
                id = "8",
                nickname = "방꾸쟁이",
                authorProfilePicture = "https://example.com/image2.jpg",
                title = "다섯번째 내방소개",
                content = "Third test post",
                imageList = listOf("https://example.com/image3.jpg"),
                savedCount = 9,
                commentCount = 0,
                productTagPinList = listOf()
            ),
        )
        // 현재 로그인 유저 닉네임으로 필터링
        return savedPosts
    }
}


// 파이어 베이스 사용
//@Singleton
//class SocialDiscoveryRepository @Inject constructor(
//    private val firestore: FirebaseFirestore,
//    private val storage: FirebaseStorage,
//) {
//
//    suspend fun getPosts(): List<Post> =
//        firestore.collection("Posts")
//            .get()
//            .await()
//            .documents
//            .mapNotNull { doc ->
//                doc.toPost()
//            }
//
//    /*
//    * Firebase Firestore에서 게시글 데이터 가져오기
//    * */
//    private fun DocumentSnapshot.toPost(): Post? {
//        val id = getString("id") ?: return null
//        val nickname = getString("nickname") ?: return null
//        val title = getString("title") ?: return null
//        val content = getString("content") ?: return null
//        val imageList = get("imageList") as? List<String> ?: emptyList()
//        val savedCount = getLong("savedCount")?.toInt() ?: 0
//        val commentCount = getLong("commentCount")?.toInt() ?: 0
//
//        return Post(
//            id = id,
//            nickname = nickname,
//            title = title,
//            content = content,
//            imageList = imageList,
//            savedCount = savedCount,
//            commentCount = commentCount
//        )
//    }
//}
