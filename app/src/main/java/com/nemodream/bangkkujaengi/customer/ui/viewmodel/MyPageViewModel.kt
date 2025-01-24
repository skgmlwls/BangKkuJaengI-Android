package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemodream.bangkkujaengi.customer.data.model.Member
import com.nemodream.bangkkujaengi.customer.data.repository.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val repository: MyPageRepository,
): ViewModel() {

    // 멤버 정보
    private val _memberInfo = MutableLiveData<Member>()
    val memberInfo: LiveData<Member> = _memberInfo

    // 멤버 정보 가져오기
    fun getMemberInfo(memberDocId: String) = viewModelScope.launch {
        runCatching {
            repository.getMemberInfo(memberDocId)
        }.onSuccess {
            _memberInfo.value = it
        }.onFailure {
            it.printStackTrace()
        }
    }
}