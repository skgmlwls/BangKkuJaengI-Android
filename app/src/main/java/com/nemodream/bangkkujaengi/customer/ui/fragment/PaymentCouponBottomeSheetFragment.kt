package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.databinding.FragmentPaymentCouponBottomeSheetBinding
import com.nemodream.bangkkujaengi.databinding.RowPaymentCouponRecyclerviewBinding

class PaymentCouponBottomeSheetFragment : BottomSheetDialogFragment() {

    lateinit var fragmentPaymentCouponBottomeSheetBinding: FragmentPaymentCouponBottomeSheetBinding
    val testData = Array(10) {
        "쿠폰명 $it"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentPaymentCouponBottomeSheetBinding = FragmentPaymentCouponBottomeSheetBinding.inflate(inflater, container, false)

        // 쿠폰 목록 recyclerview 설정 메소드 호출
        setting_recyclerview()

        return fragmentPaymentCouponBottomeSheetBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // BottomSheet의 Behavior 가져오기
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)

        // BottomSheet 높이 고정
        bottomSheet.layoutParams.height = 1500 // 원하는 높이 (예: 600dp)
        behavior.peekHeight = 600 // 초기 상태의 높이 설정
        behavior.isFitToContents = true // 내용 크기에 따라 크기 변하지 않도록 설정
        behavior.state = BottomSheetBehavior.STATE_EXPANDED // 항상 확장 상태 유지
    }

    // 쿠폰 목록 recyclerview 설정
    fun setting_recyclerview() {
        fragmentPaymentCouponBottomeSheetBinding.apply {
            rvPaymentCouponList.adapter = coupon_recyclerview_adapter()
            rvPaymentCouponList.layoutManager = LinearLayoutManager(context)
        }
    }

    // 쿠폰 목록 recyclerview adapter
    inner class coupon_recyclerview_adapter : RecyclerView.Adapter<coupon_recyclerview_adapter.coupon_recyclerview_viewholder>() {
        inner class coupon_recyclerview_viewholder(val rowPaymentCouponRecyclerviewBinding: RowPaymentCouponRecyclerviewBinding) :
                RecyclerView.ViewHolder(rowPaymentCouponRecyclerviewBinding.root)

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): coupon_recyclerview_viewholder {
            val rowPaymentCouponRecyclerviewBinding = RowPaymentCouponRecyclerviewBinding.inflate(layoutInflater, parent, false)
            val coupon_recyclerview_viewholder = coupon_recyclerview_viewholder(rowPaymentCouponRecyclerviewBinding)

            return coupon_recyclerview_viewholder
        }

        override fun getItemCount(): Int {
            return testData.size
        }

        override fun onBindViewHolder(holder: coupon_recyclerview_viewholder, position: Int) {
            holder.rowPaymentCouponRecyclerviewBinding.tvRowPaymentCouponName.text = testData[position]
        }
    }
}