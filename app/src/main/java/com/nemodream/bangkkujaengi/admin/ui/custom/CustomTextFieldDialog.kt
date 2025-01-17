package com.nemodream.bangkkujaengi.admin.ui.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.nemodream.bangkkujaengi.databinding.DialogCustomTextfieldBinding

class CustomTextFieldDialog(
    context: Context,
    private val message: String,
    private val hint: String = "",
    private val confirmText: String = "확인",
    private val cancelText: String = "취소",
    private val onConfirm: (String) -> Unit,
    private val onCancel: () -> Unit
) {
    private val dialog = Dialog(context)
    private lateinit var binding: DialogCustomTextfieldBinding

    fun show() {
        binding = DialogCustomTextfieldBinding.inflate(dialog.layoutInflater)

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
            tfEdit.hint = hint

            // 확인 버튼 클릭
            btnConfirm.setOnClickListener {
                val text = tfEdit.editText?.text.toString()
                onConfirm(text)
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