package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemodream.bangkkujaengi.customer.data.model.Post
import com.nemodream.bangkkujaengi.customer.data.repository.SocialRankRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocialRankViewModel @Inject constructor(
    private val repository: SocialRankRepository
) : ViewModel() {

    // 게시글 목록
    private val _posts = MutableLiveData<List<Post>>(emptyList())
    val posts: LiveData<List<Post>> get() = _posts

    /**
     * 게시글 목록 로드
     */
    fun loadPosts() {
        viewModelScope.launch {
            // 게시글 목록을 Firebase에서 가져온다
            val postList = repository.getPostsBySavedDecending()
            _posts.value = postList
        }
    }
}