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

    private val _followingMembers = MutableLiveData<List<Member>>()
    val followingMembers: LiveData<List<Member>> get() = _followingMembers

    private val _memberPosts = MutableLiveData<List<Post>>()
    val memberPosts: LiveData<List<Post>> get() = _memberPosts

    private val _selectedMember = MutableLiveData<Member?>()
    val selectedMember: LiveData<Member?> get() = _selectedMember


    fun loadFollowingMembers() {
        val members = repository.getFollowingMembers()
        _followingMembers.value = members

        // 첫 번째 멤버 선택
        if (members.isNotEmpty()) {
            _selectedMember.value = members.first()
            loadMemberPosts(members.first())
        }
    }

    fun selectMember(member: Member) {
        _selectedMember.value = member
        loadMemberPosts(member)
    }

    fun loadMemberPosts(member: Member) {
        _memberPosts.value = repository.getPostsByMember(member)
    }
}
