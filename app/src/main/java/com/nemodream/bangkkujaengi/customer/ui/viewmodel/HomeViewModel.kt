package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemodream.bangkkujaengi.customer.data.model.Banner
import com.nemodream.bangkkujaengi.customer.data.model.PromotionItem
import com.nemodream.bangkkujaengi.customer.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
): ViewModel() {

    // 배너 아이템
    private var _bannerItems = MutableLiveData<List<Banner>>(emptyList())
    val bannerItems: LiveData<List<Banner>> = _bannerItems

    // 프로모션 아이템
    private var _promotionItems = MutableLiveData<List<PromotionItem>>(emptyList())
    val promotionItems: LiveData<List<PromotionItem>> = _promotionItems

    // 홈 화면 배너 리스트 불러오기
    fun loadBannerItems() = viewModelScope.launch {
        runCatching {
            homeRepository.getBanners()
        }.onSuccess { items ->
            _bannerItems.value = items
        }.onFailure {
            e -> Log.d("HomeViewModel", "loadBannerItems: $e")
        }
    }

    // 홈 화면 프로모션 데이터 불러오기
    fun loadPromotions() = viewModelScope.launch {
        runCatching {
            homeRepository.getPromotionSections()
        }.onSuccess { items ->
            _promotionItems.value = items
        }.onFailure { e ->
            Log.d("HomeViewModel", "loadPromotions: $e")
        }
    }
}