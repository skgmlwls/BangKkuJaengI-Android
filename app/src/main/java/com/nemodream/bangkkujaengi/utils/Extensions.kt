package com.nemodream.bangkkujaengi.utils

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.nemodream.bangkkujaengi.R

/*
* 이미지 로드 확장 함수
* 이미지를 불러오고 있는 중에는 gray_300
* 이미지를 불러오지 못했을 경우에는 gray_500
* url을 받아서 이미지를 불러온다
* */
fun ImageView.loadImage(url: String) {
    Glide.with(context)
        .load(url)
        .centerCrop()
        .placeholder(R.color.gray_300)
        .error(R.color.gray_500)
        .into(this)
}

/*
* SnackBar 확장 함수
* 메세지를 받아 SnackBar로 보여주는 확장함수
* */
fun Fragment.showSnackBar(message: String) {
    view?.let { Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show() }
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/*
* Number의 확장함수로 숫자를 천 단위에서 ,구분하는 함수
* */
fun Number.toCommaString(): String {
    return String.format("%,d", this)
}