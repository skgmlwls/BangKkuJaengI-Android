package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.customer.data.model.Product
import com.nemodream.bangkkujaengi.customer.ui.custom.CustomDialog
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.ProductDetailViewModel
import com.nemodream.bangkkujaengi.databinding.FragmentProductOrderBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductOrderBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentProductOrderBottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var appContext: Context

    private val viewModel: ProductDetailViewModel by viewModels()

    private var selectedColor: String? = null

    private val product: Product by lazy {
        requireArguments().getParcelable(PRODUCT_KEY)!!
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(appContext, theme).apply {
            behavior.apply {
                isDraggable = true
                state = BottomSheetBehavior.STATE_EXPANDED
                skipCollapsed = true
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductOrderBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomSheetUI()
        setupListeners()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * BottomSheet의 UI를 초기화하고 설정하는 함수
     * - Dialog 창 크기 및 배경 설정
     * - 색상 선택을 위한 DropDown 설정
     * - 색상 선택 이벤트 처리
     */
    private fun setupBottomSheetUI() {
        setupDialogWindow()
        setupColorDropdown()
        handleColorSelection()
    }

    /**
     * Dialog 창의 크기와 배경을 설정하는 함수
     */
    private fun setupDialogWindow() {
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    /**
     * 색상 선택을 위한 DropDown 메뉴를 설정하는 함수
     */
    private fun setupColorDropdown() {
        ArrayAdapter(
            appContext,
            R.layout.item_dropdown_category,
            product.colors
        ).apply {
            binding.autoCompleteTextView.setAdapter(this)
        }
    }

    /**
     * 색상 선택 시 이벤트를 처리하는 함수
     * - 선택된 색상을 ViewModel에 업데이트
     * - 에러 메시지 숨김 처리
     */
    private fun handleColorSelection() {
        binding.autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedColor = product.colors[position]
            viewModel.updateSelectedColor(selectedColor)
            binding.tvProductOrderColorErrorMessage.visibility = View.GONE
        }
    }

    private fun setupListeners() {
        with(binding) {
            btnCart.setOnClickListener {
                // 선택된 색상이 없으면 에러 메시지 표시 후 early return
                if (selectedColor == null) {
                    tvProductOrderColorErrorMessage.visibility = View.VISIBLE
                    return@setOnClickListener
                }
                // 장바구니 정보 저장하는 함수
                viewModel.saveCartProduct(product.productId)
                showActionDialog()
            }

            // 주문하기 버튼 클릭 리스너
            btnOrder.setOnClickListener {
                // TODO: 주문 처리 로직 구현
                dismiss()
            }

            // 수량 증가 버튼
            btnIncrease.setOnClickListener {
                viewModel.updateQuantity(true)
            }

            // 수량 감소 버튼
            btnDecrease.setOnClickListener {
                viewModel.updateQuantity(false)
            }
        }
    }

    // LiveData 관찰
    private fun observeViewModel() {
        // 재고 수량 관찰
        viewModel.quantity.observe(viewLifecycleOwner) { quantity ->
            binding.tvQuantity.text = "$quantity"
        }

        // 선택 컬러 관찰
        viewModel.selectedColor.observe(viewLifecycleOwner) { color ->
            selectedColor = color
        }
    }

    // 장바구니 화면 이동 확인 Dialog
    private fun showActionDialog() {
        CustomDialog(
            context = requireContext(),
            message = "장바구니에 상품이 이동했습니다\n" +
                    "장바구니로 이동하시겠습니까?",
            confirmText = "확인",
            cancelText = "계속 쇼핑하기",
            onConfirm = {
                // 확인 버튼 클릭 시 처리
            },
            onCancel = {}
        ).show()
        dismiss()
    }

    companion object {
        private const val PRODUCT_KEY = "PRODUCT_KEY"

        fun newInstance(product: Product): ProductOrderBottomSheetFragment {
            return ProductOrderBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PRODUCT_KEY, product)
                }
            }
        }
    }
}