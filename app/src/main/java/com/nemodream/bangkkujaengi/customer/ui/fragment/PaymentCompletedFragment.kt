package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.databinding.FragmentPaymentCompletedBinding

class PaymentCompletedFragment : Fragment() {

    lateinit var fragmentPaymentCompletedBinding: FragmentPaymentCompletedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentPaymentCompletedBinding = FragmentPaymentCompletedBinding.inflate(inflater, container, false)

        setting_image()

        return fragmentPaymentCompletedBinding.root
    }

    fun setting_image() {
        fragmentPaymentCompletedBinding.ivPaymentCompleted.setImageResource(R.drawable.ic_symbol)
    }

}