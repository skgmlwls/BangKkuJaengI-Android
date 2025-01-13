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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.databinding.FragmentProductOrderBottomSheetBinding

class ProductOrderBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentProductOrderBottomSheetBinding? = null
    private val binding get() = _binding!!

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

        setupViews()
        setupListeners()
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
                val selectedColor = colorItems[menuItem.itemId]
                binding.tvProductOrderColor.text = selectedColor
                true
            }

            popup.show()
        }
    }

    private fun setupListeners() {
        binding.apply {
            // 주문하기 버튼 클릭 리스너
            btnOrder.setOnClickListener {
                // TODO: 주문 처리 로직 구현
                dismiss()
            }

            // 수량 증가 버튼
            btnIncrease.setOnClickListener {
                updateQuantity(true)
            }

            // 수량 감소 버튼
            btnDecrease.setOnClickListener {
                updateQuantity(false)
            }
        }
    }

    private fun updateQuantity(increase: Boolean) {
        val currentQuantity = binding.tvQuantity.text.toString().toInt()
        val newQuantity = if (increase) currentQuantity + 1 else (currentQuantity - 1).coerceAtLeast(1)
        if (newQuantity > 10) return
        binding.tvQuantity.text = newQuantity.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_UNIT_PRICE = "unit_price"

        fun newInstance(unitPrice: Int): ProductOrderBottomSheetFragment {
            return ProductOrderBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_UNIT_PRICE, unitPrice)
                }
            }
        }
    }
}