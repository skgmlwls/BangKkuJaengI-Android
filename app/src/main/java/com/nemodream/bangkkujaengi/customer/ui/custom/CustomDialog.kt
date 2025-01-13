package com.nemodream.bangkkujaengi.customer.ui.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.nemodream.bangkkujaengi.databinding.DialogCustomBinding

class CustomDialog(
    context: Context,
    private val message: String,
    private val confirmText: String = "확인",    // 기본값 설정
    private val cancelText: String = "취소",
    private val onConfirm: () -> Unit,
    private val onCancel: () -> Unit
) {
    private val dialog = Dialog(context)
    private lateinit var binding: DialogCustomBinding

    fun show() {
        binding = DialogCustomBinding.inflate(dialog.layoutInflater)

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
            tvMessage.text = message
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