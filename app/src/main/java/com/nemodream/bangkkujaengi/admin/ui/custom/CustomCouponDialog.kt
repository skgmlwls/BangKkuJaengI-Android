package com.nemodream.bangkkujaengi.admin.ui.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.nemodream.bangkkujaengi.databinding.DialogCustomCouponBinding

class CustomCouponDialog(
    context: Context,
    private val title: String,
    private val description: String,
    private val limitDate: String,
    private val minPrice: String,
    private val discount: String,
    private val confirmText: String = "확인",
    private val cancelText: String = "취소",
    private val onConfirm: () -> Unit,
    private val onCancel: () -> Unit
) {
    private val dialog = Dialog(context)
    private lateinit var binding: DialogCustomCouponBinding

    fun show() {
        binding = DialogCustomCouponBinding.inflate(dialog.layoutInflater)

        with(dialog) {
            setContentView(binding.root)
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val params = attributes
                params.width = (context.resources.displayMetrics.density * 320).toInt()
                attributes = params
            }
            setCancelable(false)
        }

        with(binding) {
            tvCouponTitle.text = title
            tvCouponDescription.text = description
            tvCouponLimitDate.text = limitDate
            tvCouponMinPrice.text = minPrice
            tvCouponDiscount.text = discount

            btnConfirm.text = confirmText
            btnCancel.text = cancelText

            // 확인 버튼 클릭
            btnConfirm.setOnClickListener {
                onConfirm()
                dialog.dismiss()
            }

            // 취소 버튼 클릭
            btnCancel.setOnClickListener {
                onCancel()
                dialog.dismiss()
            }
        }

        dialog.show()
    }
}