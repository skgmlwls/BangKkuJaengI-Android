package com.nemodream.bangkkujaengi.customer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemodream.bangkkujaengi.customer.data.model.Member
import com.nemodream.bangkkujaengi.customer.data.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val memberRepository: MemberRepository
) : ViewModel() {

    // 사용자 입력 데이터
    val name = MutableLiveData("")
    val id = MutableLiveData("")
    val nickname = MutableLiveData("")
    val password = MutableLiveData("")
    val chkPassword = MutableLiveData("")
    val phoneNumber = MutableLiveData("")
    val verificationCode = MutableLiveData("")

    // 에러 메시지 LiveData
    private val _nameError = MutableLiveData<String?>()
    val nameError: LiveData<String?> = _nameError

    private val _idError = MutableLiveData<String?>()
    val idError: LiveData<String?> = _idError

    private val _nicknameError = MutableLiveData<String?>()
    val nicknameError: LiveData<String?> = _nicknameError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> = _passwordError

    private val _chkPasswordError = MutableLiveData<String?>()
    val chkPasswordError: LiveData<String?> = _chkPasswordError

    private val _phoneNumberError = MutableLiveData<String?>()
    val phoneNumberError: LiveData<String?> = _phoneNumberError

    private val _verificationCodeError = MutableLiveData<String?>()
    val verificationCodeError: LiveData<String?> = _verificationCodeError

    // 중복 확인 상태
    private val _nicknameResult = MutableLiveData<Pair<Boolean, String>>()
    val nicknameResult: LiveData<Pair<Boolean, String>> get() = _nicknameResult

    private val _idResult = MutableLiveData<Pair<Boolean, String>>()
    val idResult: LiveData<Pair<Boolean, String>> get() = _idResult

    // 버튼 활성화 상태
    private val _isIdCheckButtonEnabled = MutableLiveData(false)
    val isIdCheckButtonEnabled: LiveData<Boolean> = _isIdCheckButtonEnabled

    private val _isNicknameCheckButtonEnabled = MutableLiveData(false)
    val isNicknameCheckButtonEnabled: LiveData<Boolean> = _isNicknameCheckButtonEnabled

    private val _isSignUpButtonEnabled = MutableLiveData(false)
    val isSignUpButtonEnabled: LiveData<Boolean> = _isSignUpButtonEnabled

    // 인증 버튼 상태 추가
    private val _isVerificationButtonEnabled = MutableLiveData(false)
    val isVerificationButtonEnabled: LiveData<Boolean> = _isVerificationButtonEnabled

    private val _isVerificationCheckButtonEnabled = MutableLiveData(false)
    val isVerificationCheckButtonEnabled: LiveData<Boolean> = _isVerificationCheckButtonEnabled

    private var isIdChecked = false
    private var isNicknameChecked = false
    private var isVerificationChecked = false

    // 이름
    fun validateName() {
        val value = name.value.orEmpty()
        _nameError.value = when {
            value.isEmpty() -> null
            value.length < 2 -> "이름은 2글자 이상 입력해주세요."
            else -> null
        }
    }

    // 아이디
    fun validateId() {
        val value = id.value.orEmpty()
        _idError.value = when {
            value.isEmpty() -> null
            value.length < 6 -> "아이디는 6글자 이상 입력해주세요."
            else -> null
        }
    }

    // 닉네임
    fun validateNickname() {
        val value = nickname.value.orEmpty()
        _nicknameError.value = when {
            value.isEmpty() -> null
            value.length < 2 -> "닉네임은 2글자 이상 입력해주세요."
            else -> null
        }
    }

    // 비밀번호
    fun validatePassword() {
        val value = password.value.orEmpty()
        _passwordError.value = when {
            value.isEmpty() -> null
            value.length < 8 -> "비밀번호는 8글자 이상 입력해주세요."
            else -> null
        }
    }

    // 비밀번호 확인
    fun validateChkPassword() {
        val value = chkPassword.value.orEmpty()
        _chkPasswordError.value = when {
            value.isEmpty() -> null
            value != password.value -> "비밀번호가 일치하지 않습니다."
            else -> null
        }
    }

    // 전화번호
    fun validatePhoneNumber() {
        val value = phoneNumber.value.orEmpty()
        _phoneNumberError.value = when {
            value.isEmpty() -> null
            value.length != 11 -> "전화번호는 11자리로 입력해주세요."
            else -> null
        }
    }

    // 인증번호
    fun validateVerificationCode() {
        val value = verificationCode.value.orEmpty()
        _verificationCodeError.value = when {
            value.isEmpty() -> null
            value.length != 6 -> "인증번호는 6자리로 입력해주세요"
            else -> null
        }
    }

    // 모든 입력값과 중복 확인 완료 여부에 따라 가입 버튼 활성화 여부 결정
    fun validateSignUpButton() {
        _isSignUpButtonEnabled.value = name.value.orEmpty().isNotEmpty() &&
                id.value.orEmpty().isNotEmpty() &&
                nickname.value.orEmpty().isNotEmpty() &&
                password.value.orEmpty().length >= 8 &&
                chkPassword.value == password.value &&
                phoneNumber.value.orEmpty().length == 11 &&
                verificationCode.value.orEmpty().length == 6 &&
                isIdChecked && isNicknameChecked && isVerificationChecked
    }

    // 회원 데이터 저장
    fun registerMember(callback: (Boolean, String?) -> Unit) {
        val member = Member(
            memberName = name.value.orEmpty(),
            memberId = id.value.orEmpty(),
            memberNickName = nickname.value.orEmpty(),
            memberPassword = password.value.orEmpty(),
            memberPhoneNumber = phoneNumber.value.orEmpty(),
            createAt = System.currentTimeMillis()
        )
        memberRepository.addMemberData(member, callback)
    }

    // 아이디 중복 확인
    fun checkIdAvailability(id: String) {
        viewModelScope.launch {
            try {
                val trimmedId = id.trim().lowercase()
                val available = memberRepository.isIdAvailable(trimmedId)

                val message = if (available) {
                    isIdChecked = true // 중복 확인 완료
                    "사용 가능한 아이디입니다."
                } else {
                    isIdChecked = false
                    "이미 사용 중인 아이디입니다."
                }
                _idResult.value = Pair(available, message)
                validateSignUpButton() // 가입 버튼 상태 업데이트
            } catch (e: Exception) {
                _idResult.value = Pair(false, "아이디 중복 확인 중 오류가 발생했습니다.")
            }
        }
    }

    // 닉네임 중복 확인
    fun checkNicknameAvailability(nickname: String) {
        viewModelScope.launch {
            try {
                val trimmedNickname = nickname.trim().lowercase()
                val available = memberRepository.isNicknameAvailable(trimmedNickname)

                val message = if (available) {
                    isNicknameChecked = true // 중복 확인 완료
                    "사용 가능한 닉네임입니다."
                } else {
                    isNicknameChecked = false
                    "이미 사용 중인 닉네임입니다."
                }
                _nicknameResult.value = Pair(available, message)
                // 가입 버튼 상태 업데이트
                validateSignUpButton()
            } catch (e: Exception) {
                _nicknameResult.value = Pair(false, "닉네임 중복 확인 중 오류가 발생했습니다.")
            }
        }
    }

    // 입력 필드 상태에 따라 중복 확인 버튼 활성화 여부 결정
    fun validateIdCheckButton() {
        val value = id.value.orEmpty()
        _isIdCheckButtonEnabled.value = value.length >= 6
        // 아이디 변경 시 중복 확인 다시 필요
        isIdChecked = false
    }

    fun validateNicknameCheckButton() {
        val value = nickname.value.orEmpty()
        _isNicknameCheckButtonEnabled.value = value.length >= 2
        // 닉네임 변경 시 중복 확인 다시 필요
        isNicknameChecked = false
    }
    // 전화번호 인증 요청 버튼 활성화 여부 결정
    fun validateVerificationButton() {
        val value = phoneNumber.value.orEmpty()
        _isVerificationButtonEnabled.value = value.length == 11
    }

    // 인증 코드 확인 버튼 활성화 여부 결정
    fun validateVerificationCheckButton() {
        val value = verificationCode.value.orEmpty()
        _isVerificationCheckButtonEnabled.value = value.length == 6
    }

    // 인증 확인 완료 처리
    fun verifyCodeSuccess() {
        isVerificationChecked = true
        validateSignUpButton() // 가입 버튼 상태 업데이트
    }
}