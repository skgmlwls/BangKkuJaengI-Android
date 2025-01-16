package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nemodream.bangkkujaengi.customer.data.model.Member
import com.nemodream.bangkkujaengi.customer.data.repository.SocialFollowingAllRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SocialFollowingAllViewModel @Inject constructor(
    private val repository: SocialFollowingAllRepository
) : ViewModel() {

    // 팔로잉 멤버 목록 데이터를 관리
    private val _followingMembers = MutableLiveData<List<Member>>()
    val followingMembers: LiveData<List<Member>> get() = _followingMembers


    // 팔로잉 멤버 목록 로드
    fun loadFollowingAllMembers() {
        val members = repository.getFollowingMembers() // 데이터베이스나 API에서 데이터 가져옴
        _followingMembers.value = members
    }
}