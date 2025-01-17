package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nemodream.bangkkujaengi.customer.data.model.Member
import com.nemodream.bangkkujaengi.customer.data.model.Post
import com.nemodream.bangkkujaengi.customer.data.repository.SocialFollowingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SocialFollowingViewModel @Inject constructor(
    private val repository: SocialFollowingRepository
) : ViewModel() {

    // 팔로잉 멤버 목록 데이터를 관리
    private val _followingMembers = MutableLiveData<List<Member>>()
    val followingMembers: LiveData<List<Member>> get() = _followingMembers

    // 선택한 멤버의 게시글 목록 데이터를 관리
    private val _memberPosts = MutableLiveData<List<Post>>()
    val memberPosts: LiveData<List<Post>> get() = _memberPosts

    // 현재 선택된 멤버를 관리
    private val _selectedMember = MutableLiveData<Member?>()
    val selectedMember: LiveData<Member?> get() = _selectedMember

    // 멤버별 팔로잉 상태를 관리
    private val followingStates = mutableMapOf<String, Boolean>()

    // 선택된 멤버의 팔로잉 상태
    private val _isFollowing = MutableLiveData<Boolean>()
    val isFollowing: LiveData<Boolean> get() = _isFollowing

    // 팔로잉 멤버 목록 로드
    fun loadFollowingMembers() {
        val members = repository.getFollowingMembers() // 데이터베이스나 API에서 데이터 가져옴
        _followingMembers.value = members

        // 각 멤버의 초기 팔로잉 상태를 true로 설정
        members.forEach { member ->
            followingStates[member.id] = true
        }

        // 첫 번째 멤버를 기본 선택
        if (members.isNotEmpty()) {
            selectMember(members.first())
        }
    }

    // 특정 멤버를 선택
    fun selectMember(member: Member) {
        _selectedMember.value = member // 선택된 멤버 설정
        _isFollowing.value = followingStates[member.id] ?: false // 해당 멤버의 팔로잉 상태 설정
        loadMemberPosts(member) // 해당 멤버의 게시글 로드
    }

    // 선택된 멤버의 팔로잉 상태를 반전
    fun toggleFollowing() {
        val selectedId = _selectedMember.value?.id ?: return
        val currentState = followingStates[selectedId] ?: false
        val newState = !currentState

        followingStates[selectedId] = newState
        _isFollowing.value = newState
    }

    // 특정 멤버의 게시글 로드
    private fun loadMemberPosts(member: Member) {
        _memberPosts.value = repository.getPostsByMember(member) // 멤버의 게시글 데이터 가져오기
    }
}