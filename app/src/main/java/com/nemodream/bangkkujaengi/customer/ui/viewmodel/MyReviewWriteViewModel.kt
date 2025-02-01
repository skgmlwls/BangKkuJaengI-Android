package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemodream.bangkkujaengi.customer.data.model.Review
import com.nemodream.bangkkujaengi.customer.data.repository.MyReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MyReviewWriteViewModel @Inject constructor(
    private val repository: MyReviewRepository
) : ViewModel() {

    private val _rating = MutableLiveData(0)  // 현재 선택된 별점
    val rating: LiveData<Int> get() = _rating

    private val _reviewContent = MutableLiveData("")  // 입력된 리뷰 내용
    val reviewContent: LiveData<String> get() = _reviewContent

    private val _isSubmitEnabled = MutableLiveData(false)  // 작성 완료 버튼 활성화 상태
    val isSubmitEnabled: LiveData<Boolean> get() = _isSubmitEnabled

    private val _reviewSubmitResult = MutableLiveData<Boolean>()  // 리뷰 저장 결과
    val reviewSubmitResult: LiveData<Boolean> get() = _reviewSubmitResult

    private val _isSubmitting = MutableLiveData(false)
    val isSubmitting: LiveData<Boolean> get() = _isSubmitting

    private val _productName = MutableLiveData<String>()
    val productName: LiveData<String> get() = _productName

    private val _productImageUrl = MutableLiveData<String>()
    val productImageUrl: LiveData<String> get() = _productImageUrl

    // Purchase의 productId를 기반으로 Product 데이터 로드
    fun loadProductData(productId: String) {
        viewModelScope.launch {
            val productData = repository.fetchProductDataByPurchase(productId)
            if (productData != null) {
                _productName.value = productData.productTitle
                _productImageUrl.value = productData.imageUrl
            } else {
                _productName.value = "상품 정보를 불러올 수 없습니다."
                _productImageUrl.value = ""
            }
        }
    }

    // 별점 선택 함수
    fun selectRating(selectedStar: Int) {
        _rating.value = selectedStar
        validateInput()
    }

    // 리뷰 내용 업데이트 함수
    fun updateReviewContent(content: String) {
        _reviewContent.value = content
        validateInput()
    }

    // 입력 유효성 검사 함수
    private fun validateInput() {
        _isSubmitEnabled.value = (_rating.value ?: 0) > 0 && (_reviewContent.value?.length ?: 0) >= 20
    }

    fun submitReview(productId: String, documentId: String) {
        viewModelScope.launch {
            _isSubmitting.value = true  // 리뷰 저장 중 상태로 변경

            val memberId = repository.fetchMemberId(documentId)
            if (memberId.isNullOrEmpty()) {
                _reviewSubmitResult.value = false
                _isSubmitting.value = false  // 작업 완료 후 상태 변경
                return@launch
            }

            val review = Review(
                id = "",
                productId = productId,
                productTitle = _productName.value ?: "상품명 없음",
                reviewDate = getCurrentDate(),
                rating = _rating.value ?: 0,
                memberId = memberId,
                content = _reviewContent.value ?: "",
                isDelete = false
            )
            val result = repository.submitReview(review)
            _reviewSubmitResult.value = result

            if (!result) {
                _isSubmitting.value = false  // 실패 시 버튼 다시 활성화
            }
        }
    }

    // 현재 날짜를 문자열로 변환
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
}