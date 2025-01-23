package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.toSpannable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.databinding.FragmentMyPageBinding
import com.nemodream.bangkkujaengi.utils.showToast

class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var appContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {
        with(binding) {
            // '3000원'을 텍스트 스타일을 볼드체로 변경
            SpannableString(tvMyPageLoginSubTitle.text).apply {
                setSpan(
                    StyleSpan(Typeface.BOLD),
                    tvMyPageLoginSubTitle.text.indexOf("3000"),
                    tvMyPageLoginSubTitle.text.indexOf("3000") + "3000".length + 1,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE,
                )
                tvMyPageLoginSubTitle.text = this
            }
        }
    }

    private fun setupListeners() {
        with(binding) {
            viewMyPageLogin.setOnClickListener {
                val action = MyPageFragmentDirections.actionNavigationMyPageToSignInActivity()
                findNavController().navigate(action)
            }

            tvMyPageNonMemberInquiry.setOnClickListener {
                val action = MyPageFragmentDirections.actionNavigationMyPageToNavigationNonMemberOrder()
                findNavController().navigate(action)
            }

            // 메뉴 선택
            toolbarMyPage.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_cart -> {
                        val action = MyPageFragmentDirections.actionNavigationMyPageToNavigationCart()
                        findNavController().navigate(action)
                        true
                    }
                    else -> false
                }
            }

            tvMyPageSetting.setOnClickListener {
                appContext.showToast("준비중입니다.")
            }
            tvMyPageContact.setOnClickListener {
                appContext.showToast("준비중입니다.")
            }
            tvMyPageCenter.setOnClickListener {
                appContext.showToast("준비중입니다.")
            }
            tvMyPageNotice.setOnClickListener {
                appContext.showToast("준비중입니다.")
            }
        }
    }
}