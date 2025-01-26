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
                followerCount = 1,
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
                followingCount = 2,
                followingList = emptyList(),
                followerCount = 2
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
                followingCount = 3,
                followingList = emptyList(),
                followerCount = 3
            ),
            Member(
                id = "4",
                memberId = "user4",
                memberPassword = "password",
                memberName = "User Two",
                memberNickName = "닉네임4",
                memberPhoneNumber = "010-5678-1234",
                memberProfileImage = "https://example.com/profile4.jpg",
                point = 3000,
                isActive = true,
                createAt = System.currentTimeMillis(),
                followingCount = 4,
                followingList = emptyList(),
                followerCount = 4
            ),
            Member(
                id = "5",
                memberId = "user5",
                memberPassword = "password",
                memberName = "User Two",
                memberNickName = "닉네임5",
                memberPhoneNumber = "010-5678-1234",
                memberProfileImage = "https://example.com/profile5.jpg",
                point = 3000,
                isActive = true,
                createAt = System.currentTimeMillis(),
                followingCount = 5,
                followingList = emptyList(),
                followerCount = 5
            ),
            Member(
                id = "6",
                memberId = "user6",
                memberPassword = "password",
                memberName = "User One",
                memberNickName = "닉네임6",
                memberPhoneNumber = "010-1234-5678",
                memberProfileImage = "https://example.com/profile6.jpg",
                point = 3000,
                isActive = true,
                createAt = System.currentTimeMillis(),
                followingCount = 6,
                followingList = emptyList(),
                followerCount = 6
            ),Member(
                id = "7",
                memberId = "user7",
                memberPassword = "password",
                memberName = "User One",
                memberNickName = "닉네임7",
                memberPhoneNumber = "010-1234-5678",
                memberProfileImage = "https://example.com/profile7.jpg",
                point = 3000,
                isActive = true,
                createAt = System.currentTimeMillis(),
                followingCount = 7,
                followingList = emptyList(),
                followerCount = 7
            ),Member(
                id = "8",
                memberId = "user8",
                memberPassword = "password",
                memberName = "User One",
                memberNickName = "닉네임8",
                memberPhoneNumber = "010-1234-5678",
                memberProfileImage = "https://example.com/profile8.jpg",
                point = 3000,
                isActive = true,
                createAt = System.currentTimeMillis(),
                followingCount = 8,
                followingList = emptyList(),
                followerCount = 8
            ),Member(
                id = "9",
                memberId = "user9",
                memberPassword = "password",
                memberName = "User One",
                memberNickName = "닉네임9",
                memberPhoneNumber = "010-1234-5678",
                memberProfileImage = "https://example.com/profile9.jpg",
                point = 3000,
                isActive = true,
                createAt = System.currentTimeMillis(),
                followingCount = 9,
                followingList = emptyList(),
                followerCount = 9
            ),
        )
    }

    /**
     * 특정 회원이 작성한 게시글 목록을 가져옵니다.
     */
    fun getPostsByMember(member: Member): List<Post> {
        // 더미 데이터 생성 (실제 구현 시 네트워크 또는 데이터베이스와 연동)
        val allPosts = listOf(
            Post(
                id = "post11",
                nickname = "닉네임1",
                authorProfilePicture = "https://example.com/image1.jpg",
                title = "게시글 제목 11",
                content = "게시글 내용 11",
                imageList = listOf("https://example.com/post1_img11.jpg"),
                savedCount = 10,
                commentCount = 5,
                productTagPinList = listOf()
            ),
            Post(
                id = "post22",
                nickname = "닉네임1",
                authorProfilePicture = "https://example.com/image1.jpg",
                title = "게시글 제목 22",
                content = "게시글 내용 22",
                imageList = listOf("https://example.com/post2_img11.jpg"),
                savedCount = 15,
                commentCount = 8,
                productTagPinList = listOf()
            ),
            Post(
                id = "post32",
                nickname = "닉네임2",
                authorProfilePicture = "https://example.com/image1.jpg",
                title = "게시글 제목 33",
                content = "게시글 내용 33",
                imageList = listOf("https://example.com/post2_img11.jpg"),
                savedCount = 15,
                commentCount = 8,
                productTagPinList = listOf()
            )
        )
        // 특정 회원의 닉네임으로 필터링
        return allPosts.filter { it.nickname == member.memberNickName }
    }
}