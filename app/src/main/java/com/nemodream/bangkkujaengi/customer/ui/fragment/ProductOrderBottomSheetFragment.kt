package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nemodream.bangkkujaengi.customer.ui.custom.CustomDialog
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.ProductDetailViewModel
import com.nemodream.bangkkujaengi.databinding.FragmentProductOrderBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductOrderBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentProductOrderBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductDetailViewModel by viewModels()

    private var selectedColor: String? = null

    private val productId: String by lazy { arguments?.getString(PRODUCT_ID) ?: "" }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            behavior.apply {
                isDraggable = true
                state = BottomSheetBehavior.STATE_EXPANDED
                skipCollapsed = true
            }
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
        observeViewModel()
        setupViews()
        setupListeners()
    }

    private fun observeViewModel() {
        viewModel.quantity.observe(viewLifecycleOwner) { quantity ->
            binding.tvQuantity.text = quantity.toString()
        }
    }

    private fun setupViews() {
        // 뷰 초기화 및 설정
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        binding.viewProductOrderColorArea.setOnClickListener { view ->
            val popup = PopupMenu(requireContext(), view, Gravity.TOP)

            // 서버에서 받아온 데이터라고 가정
            val colorItems = listOf(
                "빨강",
                "노랑",
                "파랑"
            )

            // 동적으로 메뉴 아이템 추가
            colorItems.forEachIndexed { index, colorName ->
                popup.menu.add(Menu.NONE, index, index, colorName)
            }

            popup.setOnMenuItemClickListener { menuItem ->
                selectedColor = colorItems[menuItem.itemId]
                binding.tvProductOrderColor.text = selectedColor
                binding.tvProductOrderColorErrorMessage.visibility = View.GONE
                true
            }

            popup.show()
        }
    }

    private fun setupListeners() {
        with(binding) {

            btnCart.setOnClickListener {
                if (selectedColor == null) {
                    tvProductOrderColorErrorMessage.visibility = View.VISIBLE
                    return@setOnClickListener
                }

                // 장바구니 정보 저장하는 함수
                viewModel.saveCartProduct(productId)

                CustomDialog(
                    context = requireContext(),
                    message = "장바구니에 상품이 이동했습니다\n" +
                            "장바구니로 이동하시겠습니까?",
                    confirmText = "확인",
                    cancelText = "계속 쇼핑하기",
                    onConfirm = {
                        // 확인 버튼 클릭 시 처리
                    },
                    onCancel = {
                    }
                ).show()

                dismiss()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PRODUCT_ID = "PRODUCT_ID"

        fun newInstance(productId: String): ProductOrderBottomSheetFragment {
            return ProductOrderBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(PRODUCT_ID, productId)
                }
            }
        }
    }
}