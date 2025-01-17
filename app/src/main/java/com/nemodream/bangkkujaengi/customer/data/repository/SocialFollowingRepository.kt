package com.nemodream.bangkkujaengi.customer.data.repository

import com.nemodream.bangkkujaengi.customer.data.model.Member
import com.nemodream.bangkkujaengi.customer.data.model.Post
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocialFollowingRepository @Inject constructor(){

    /**
     * 사용자가 팔로잉 중인 회원 목록을 가져온다
     */
    fun getFollowingMembers(): List<Member> {
        // 더미 데이터 생성 (실제 구현 시 네트워크 또는 데이터베이스와 연동)
        return listOf(
            Member(
                id = "1",
                memberId = "user1",
                memberPassword = "password",
                memberName = "User One",
                memberNickName = "닉네임1",
                memberPhoneNumber = "010-1234-5678",
                memberProfileImage = "https://example.com/profile1.jpg",
                point = 3000,
                isActive = true,
                createAt = System.currentTimeMillis(),
                followingCount = 5,
                followingList = emptyList(),
            ),
            Member(
                id = "2",
                memberId = "user2",
                memberPassword = "password",
                memberName = "User Two",
                memberNickName = "닉네임2",
                memberPhoneNumber = "010-5678-1234",
                memberProfileImage = "https://example.com/profile2.jpg",
                point = 3000,
                isActive = true,
                createAt = System.currentTimeMillis(),
                followingCount = 3,
                followingList = emptyList()
            ),
            Member(
                id = "3",
                memberId = "user3",
                memberPassword = "password",
                memberName = "User Two",
                memberNickName = "닉네임3",
                memberPhoneNumber = "010-5678-1234",
                memberProfileImage = "https://example.com/profile3.jpg",
                point = 3000,
                isActive = true,
                createAt = System.currentTimeMillis(),
                followingCount = 6,
                followingList = emptyList()
            )
        )
    }

    /**
     * 특정 회원이 작성한 게시글 목록을 가져옵니다.
     */
    fun getPostsByMember(member: Member): List<Post> {
        // 더미 데이터 생성 (실제 구현 시 네트워크 또는 데이터베이스와 연동)
        return listOf(
            Post(
                id = "post11",
                nickname = "닉네임1",
                title = "게시글 제목 11",
                content = "게시글 내용 11",
                imageList = listOf("https://example.com/post1_img11.jpg"),
                savedCount = 10,
                commentCount = 5
            ),
            Post(
                id = "post22",
                nickname = "닉네임1",
                title = "게시글 제목 22",
                content = "게시글 내용 22",
                imageList = listOf("https://example.com/post2_img11.jpg"),
                savedCount = 15,
                commentCount = 8
            )
        )
    }
}
