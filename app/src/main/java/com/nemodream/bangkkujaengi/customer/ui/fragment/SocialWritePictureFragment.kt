package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nemodream.bangkkujaengi.R

class SocialWritePictureFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment의 레이아웃 설정
        return inflater.inflate(R.layout.fragment_social_write_picture, container, false)
    }
}