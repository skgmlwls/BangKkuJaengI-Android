package com.nemodream.bangkkujaengi.admin.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.admin.ui.custom.CustomCouponDialog
import com.nemodream.bangkkujaengi.admin.ui.viewmodel.AdminAddCouponViewModel
import com.nemodream.bangkkujaengi.databinding.FragmentAdminAddCouponBinding
import com.nemodream.bangkkujaengi.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class AdminAddCouponFragment : Fragment() {
    private var _binding: FragmentAdminAddCouponBinding? = null
    private val binding get() = _binding!!

    private lateinit var appContext: Context
    private val viewModel: AdminAddCouponViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext = context
    }

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
        setupTextChangeListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.isSubmitEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.btnCouponAddSubmit.isEnabled = isEnabled
        }

        // 할인타입
        viewModel.isDiscountPercent.observe(viewLifecycleOwner) { isPercent ->
            if (isPercent) {
                binding.tfAdminCouponAddPrice.visibility = View.GONE
                binding.tfAdminCouponAddPercent.visibility = View.VISIBLE
                return@observe
            }

            binding.tfAdminCouponAddPercent.visibility = View.GONE
            binding.tfAdminCouponAddPrice.visibility = View.VISIBLE
        }
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

            groupAdminCouponAddDiscount.addOnButtonCheckedListener { _, checkedId, _ ->
                viewModel.setDiscountPercent(checkedId == R.id.btn_discount_percent)
            }

            btnCouponAddSubmit.setOnClickListener {
                CustomCouponDialog(
                    appContext,
                    tfAdminCouponAddTitle.editText?.text.toString(),
                    tfAdminCouponAddDescription.editText?.text.toString(),
                    tfAdminCouponAddLimitDate.editText?.text.toString(),
                    tfAdminCouponAddMinPrice.editText?.text.toString(),
                    if (viewModel.isDiscountPercent.value!!) "${tfAdminCouponAddPercent.editText?.text} %" else "${tfAdminCouponAddPrice.editText?.text} 원",
                    "쿠폰 등록",
                    "취소",
                    onConfirm = {
                        viewModel.createAndSaveCoupon(
                            tfAdminCouponAddTitle.editText?.text.toString(),
                            tfAdminCouponAddDescription.editText?.text.toString(),
                            tfAdminCouponAddLimitDate.editText?.text.toString(),
                            tfAdminCouponAddMinPrice.editText?.text.toString(),
                            if (viewModel.isDiscountPercent.value!!) tfAdminCouponAddPercent.editText?.text.toString() else tfAdminCouponAddPrice.editText?.text.toString()
                        )
                        findNavController().navigateUp()
                    },
                    onCancel = {},
                ).show()
            }
        }
    }

    private fun setupTextChangeListeners() {
        with(binding) {
            tfAdminCouponAddTitle.editText?.doAfterTextChanged { validateFields() }
            tfAdminCouponAddDescription.editText?.doAfterTextChanged { validateFields() }
            tfAdminCouponAddLimitDate.editText?.doAfterTextChanged { validateFields() }
            tfAdminCouponAddMinPrice.editText?.doAfterTextChanged { validateFields() }
            tfAdminCouponAddPercent.editText?.doAfterTextChanged { validateFields() }
        }
    }

    /*
    * 입력 필드 유효성 검증
    * - 모든 필드의 값을 ViewModel에 전달하여 검증
    * */
    private fun validateFields() {
        with(binding) {
            viewModel.validateFields(
                tfAdminCouponAddTitle.editText?.text.toString(),
                tfAdminCouponAddDescription.editText?.text.toString(),
                tfAdminCouponAddLimitDate.editText?.text.toString(),
                tfAdminCouponAddMinPrice.editText?.text.toString(),
                if (viewModel.isDiscountPercent.value!!) tfAdminCouponAddPercent.editText?.text.toString() else tfAdminCouponAddPrice.editText?.text.toString()
            )
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