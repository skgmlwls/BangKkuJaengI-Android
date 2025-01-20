package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.databinding.FragmentOrderDetailsBinding

class OrderDetailsFragment : Fragment() {

    lateinit var fragmentOrderDetailsBinding : FragmentOrderDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentOrderDetailsBinding = FragmentOrderDetailsBinding.inflate(inflater, container, false)



        return fragmentOrderDetailsBinding.root
    }

}