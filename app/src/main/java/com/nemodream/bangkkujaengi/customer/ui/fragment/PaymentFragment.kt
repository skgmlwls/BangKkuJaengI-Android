package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.PaymentViewModel
import com.nemodream.bangkkujaengi.databinding.FragmentPaymentBinding
import com.nemodream.bangkkujaengi.databinding.RowPaymentRecyclerviewBinding
import com.nemodream.bangkkujaengi.databinding.RowPaymentSelectCouponRecyclerviewBinding

class PaymentFragment : Fragment() {

    lateinit var fragmentPaymentBinding: FragmentPaymentBinding

    val paymentViewModel: PaymentViewModel by viewModels()

    // 테스트
    val testData = Array(3) {
        "상품명 $it"
    }

    // 유저 ID
    var user_id: String = ""
    // 유저 전화번호
    var user_phone_number: String = ""
    // 유저 주소
    var user_address: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentPaymentBinding = FragmentPaymentBinding.inflate(inflater, container, false)

        // 유저 아이디 세팅
        getting_user_id()
        // 리사이클러뷰 설정
        setting_recycledrview_order()
        // 버튼 설정
        setting_button()
        // textInputLayout의 값을 세팅하고 업데이트 하는 메소드 호출
        setting_textInputLayout()
        // 선택된 쿠폰 목록 recyclerview 설정
        setting_recyclerview_select_coupon()

        return fragmentPaymentBinding.root
    }

    // 유저 아이디 세팅 메소드
    fun getting_user_id() {
        user_id = arguments?.getString("user_id").toString()
        user_phone_number = arguments?.getString("user_phone_number").toString()
        user_address = arguments?.getString("user_address").toString()
        Log.d("testId", user_id)
    }

    // textInputLayout의 값을 세팅하고 업데이트 메소드
    fun setting_textInputLayout() {

        paymentViewModel.til_payment_name_text.value = user_id
        paymentViewModel.til_payment_phone_number_text.value = user_phone_number
        paymentViewModel.til_payment_address_text.value = user_address
        // 초기 배송지의 이름 값을 세팅
        paymentViewModel.til_payment_name_text.observe(viewLifecycleOwner) {
            fragmentPaymentBinding.tilPaymentName.editText?.setText(it) // 값을 설정
        }
        // 초기 배송지의 전화번호 값을 세팅
        paymentViewModel.til_payment_phone_number_text.observe(viewLifecycleOwner) {
            fragmentPaymentBinding.tilPaymentPhoneNumber.editText?.setText(it)
        }
        // 초기 배송지의 주소 값을 세팅
        paymentViewModel.til_payment_address_text.observe(viewLifecycleOwner) {
            fragmentPaymentBinding.tilPaymentAddress.editText?.setText(it)
        }
    }

    fun setting_button() {
        fragmentPaymentBinding.apply {
            btnPaymentCouponListShow.setOnClickListener {
                // 쿠폰 목록 보여주기
                val bottomSheetFragment = PaymentCouponBottomeSheetFragment()
                bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
            }
        }
    }

    /////////////////////////////////// 주문 목록 recyclerview //////////////////////////////////////
    fun setting_recycledrview_order() {
        fragmentPaymentBinding.apply {
            rvPaymentOrderProductList.adapter = payment_recyclerview_adapter()
            rvPaymentOrderProductList.layoutManager = LinearLayoutManager(context)
        }
    }

    inner class payment_recyclerview_adapter : RecyclerView.Adapter<payment_recyclerview_adapter.payment_recyclerview_viewholder>() {
        inner class payment_recyclerview_viewholder(val rowPaymentRecyclerviewBinding: RowPaymentRecyclerviewBinding) :
                RecyclerView.ViewHolder(rowPaymentRecyclerviewBinding.root)

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): payment_recyclerview_viewholder {
            val rowPaymentRecyclerviewBinding = RowPaymentRecyclerviewBinding.inflate(layoutInflater, parent, false)
            val payment_recyclerview_viewholder = payment_recyclerview_viewholder(rowPaymentRecyclerviewBinding)

            return payment_recyclerview_viewholder
        }

        override fun getItemCount(): Int {
            return testData.size
        }

        override fun onBindViewHolder(holder: payment_recyclerview_viewholder, position: Int) {
            holder.rowPaymentRecyclerviewBinding.tvRowPaymentProductName.text = testData[position]
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////// 선택된 쿠폰 목록 recyclerview ///////////////////////////////////
    fun setting_recyclerview_select_coupon() {
        fragmentPaymentBinding.apply {
            rvPaymentSelectCouponList.adapter = select_coupon_recyclerview_adapter()
            rvPaymentSelectCouponList.layoutManager = LinearLayoutManager(context)
        }
    }

    inner class select_coupon_recyclerview_adapter : RecyclerView.Adapter<select_coupon_recyclerview_adapter.select_coupon_recyclerview_viewholder>() {

        inner class select_coupon_recyclerview_viewholder(val rowPaymentSelectCouponRecyclerviewBinding: RowPaymentSelectCouponRecyclerviewBinding) :
                RecyclerView.ViewHolder(rowPaymentSelectCouponRecyclerviewBinding.root)

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): select_coupon_recyclerview_viewholder {
            val rowPaymentSelectCouponRecyclerviewBinding = RowPaymentSelectCouponRecyclerviewBinding.inflate(layoutInflater, parent, false)
            val select_coupon_recyclerview_viewholder = select_coupon_recyclerview_viewholder(rowPaymentSelectCouponRecyclerviewBinding)

            return select_coupon_recyclerview_viewholder
        }

        override fun getItemCount(): Int {
            return 1
        }

        override fun onBindViewHolder(
            holder: select_coupon_recyclerview_viewholder,
            position: Int
        ) {

        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

}