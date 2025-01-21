package com.nemodream.bangkkujaengi.customer.ui.fragment.findAccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.databinding.FragmentFindPasswordBinding
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.FindPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindPasswordFragment : Fragment() {

    private var _binding: FragmentFindPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FindPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isVerified.observe(viewLifecycleOwner) { isVerified ->
            binding.btnFindPwResetPw.isEnabled = isVerified
        }
    }

    private fun setupListeners() {
        // 인증 요청 버튼
        binding.btnFindPwVerification.setOnClickListener {
            val id = binding.tfFindPwId.editText?.text.toString()
            val phoneNumber = binding.tfFindPwPhoneNumber.editText?.text.toString()

            if (id.isBlank() || phoneNumber.isBlank()) {
                Toast.makeText(requireContext(), "아이디와 전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.requestVerification(id, phoneNumber)
            }
        }

        // 인증 확인 버튼
        binding.btnFindPwCheckVerification.setOnClickListener {
            val inputCode = binding.tfFindPwVerificationCode.editText?.text.toString()

            if (inputCode.isBlank()) {
                Toast.makeText(requireContext(), "인증번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.verifyCode(inputCode)
            }
        }

        // 비밀번호 재설정 버튼
        binding.btnFindPwResetPw.setOnClickListener {
            val memberId = viewModel.memberId.value
            if (!memberId.isNullOrEmpty()) {
                val bundle = Bundle().apply {
                    putString("memberId", memberId) // 전달할 userId
                }
                val fragment = ResetPasswordFragment().apply {
                    arguments = bundle
                }
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_find_info, fragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                Toast.makeText(requireContext(), "인증되지 않은 사용자입니다.", Toast.LENGTH_SHORT).show()
            }
        }
        // 로그인 화면으로 이동
//        binding.btnFindPwLogin.setOnClickListener {
//            requireActivity().finish()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}