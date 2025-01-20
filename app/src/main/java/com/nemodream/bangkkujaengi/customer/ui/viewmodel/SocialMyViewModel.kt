package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemodream.bangkkujaengi.customer.data.model.Member
import com.nemodream.bangkkujaengi.customer.data.model.Post
import com.nemodream.bangkkujaengi.customer.data.repository.SocialDiscoveryRepository
import com.nemodream.bangkkujaengi.customer.data.repository.SocialMyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocialMyViewModel @Inject constructor(
    private val repository: SocialMyRepository
) : ViewModel() {

    // 내 프로필 바
    private val _myProfile = MutableLiveData<Member?>()
    val myProfile: LiveData<Member?> get() = _myProfile

    // 게시글 목록
    private val _posts = MutableLiveData<List<Post>>(emptyList())
    val posts: LiveData<List<Post>> get() = _posts


    // 내 프로필 업데이트
    fun loadMyProfile() {
        _myProfile.value = repository.getMyProfile()
    }

     // 게시글 목록 로드
    fun loadPosts() {
        viewModelScope.launch {
            val postList = repository.getPosts()
            _posts.value = postList
        }
    }

    // 게시글 필터링 함수
    fun filterPostsByType(type: String) {
        val member = _myProfile.value ?: return
        val allPosts = _posts.value ?: return

        _posts.value = when (type) {
            "MY_POSTS" -> allPosts.filter { it.nickname == member.memberNickName }
            "SAVED_POSTS" -> member.savedPost
            else -> allPosts
        }
    }
}