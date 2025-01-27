package com.nemodream.bangkkujaengi.customer.data.model

import android.widget.ImageView

data class Tag(
    val tagX: Float,
    val tagY: Float,
    val tagProductInfo: Product,
    var pinView: ImageView? = null // 태그 핀의 UI 요소
)