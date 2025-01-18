package com.nemodream.bangkkujaengi.admin.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.databinding.FragmentAdminAddCouponBinding
import com.nemodream.bangkkujaengi.utils.showSnackBar
import java.text.SimpleDateFormat
import java.util.Locale

class AdminAddCouponFragment: Fragment() {
    private var _binding: FragmentAdminAddCouponBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminAddCouponBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListeners() {
        with(binding) {
            toolbarAddCoupon.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            btnCouponAddLimitDate.setOnClickListener {
                showDatePicker()
            }

            groupAdminCouponAddDiscount.addOnButtonCheckedListener { group, checkedId, isChecked ->
                // 할인율이 케츠라면 텍스트필드도 할인율로 보이기
                if (checkedId == R.id.btn_discount_percent && isChecked) {
                    tfAdminCouponAddPrice.visibility = View.GONE
                    tfAdminCouponAddPercent.visibility = View.VISIBLE
                    return@addOnButtonCheckedListener
                }
                // 할인 가격이 체크라면 텍스트 필드를 할인 가격으로 보이기
                if (checkedId == R.id.btn_discount_price && isChecked) {
                    tfAdminCouponAddPercent.visibility = View.GONE
                    tfAdminCouponAddPrice.visibility = View.VISIBLE
                    return@addOnButtonCheckedListener
                }
            }
        }
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("쿠폰 유효기간 선택")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        // DatePicker가 날짜 선택되었을 때의 리스너
        datePicker.addOnPositiveButtonClickListener { selection ->
            // 선택된 날짜를 원하는 형식으로 포맷팅
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormatter.format(selection)

            // 현재 날짜보다 이전의 날짜를 선택할 수 없도록 설정
            if (selection < MaterialDatePicker.todayInUtcMilliseconds()) {
                requireContext().showSnackBar(binding.root, "현재 날짜보다 이전은 선택할 수 없습니다.")
                return@addOnPositiveButtonClickListener
            }
            binding.tfAdminCouponAddLimitDate.editText?.setText(formattedDate)
        }
        datePicker.show(parentFragmentManager, "DATE_PICKER")
    }

}