package com.nemodream.bangkkujaengi.customer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.customer.data.model.Coupon
import com.nemodream.bangkkujaengi.customer.data.model.CouponType
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.RowPaymentCouponRecyclerviewViewModel
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.RowPaymentSelectCouponRecyclerviewViewModel
import com.nemodream.bangkkujaengi.databinding.RowPaymentCouponRecyclerviewBinding
import com.nemodream.bangkkujaengi.databinding.RowPaymentSelectCouponRecyclerviewBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class SelectCouponAdapter(
    val checked_coupon_list : MutableList<Coupon>
) : RecyclerView.Adapter<SelectCouponAdapter.SelectCouponViewHolder>() {

    inner class SelectCouponViewHolder(val rowPaymentSelectCouponRecyclerviewBinding: RowPaymentSelectCouponRecyclerviewBinding) :
            RecyclerView.ViewHolder(rowPaymentSelectCouponRecyclerviewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectCouponViewHolder {
        val rowPaymentSelectCouponRecyclerviewBinding = RowPaymentSelectCouponRecyclerviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return SelectCouponViewHolder(rowPaymentSelectCouponRecyclerviewBinding)
    }

    override fun getItemCount(): Int = checked_coupon_list.size

    override fun onBindViewHolder(holder: SelectCouponViewHolder, position: Int) {
        val rowPaymentSelectCouponRecyclerviewViewModel = RowPaymentSelectCouponRecyclerviewViewModel()

//        // 옵저버 세팅 메소드 호출
//        setting_text_observe(
//            holder.rowPaymentSelectCouponRecyclerviewBinding,
//            rowPaymentCouponRecyclerviewViewModel,
//            position
//        )
//
//        // 뷰모델 초기 값 세팅 메소드 호출
//        setting_viewmodel_value(
//            rowPaymentCouponRecyclerviewViewModel,
//            holder.rowPaymentCouponRecyclerviewBinding,
//            position
//        )


    }

//    // 옵저버 세팅 메소드
//    fun setting_text_observe(
//        rowPaymentCouponRecyclerviewBinding: RowPaymentCouponRecyclerviewBinding,
//        rowPaymentCouponRecyclerviewViewModel: RowPaymentCouponRecyclerviewViewModel,
//        position: Int
//    ) {
//        rowPaymentCouponRecyclerviewViewModel.tv_row_payment_coupon_title.observe(viewLifecycleOwner) {
//            rowPaymentCouponRecyclerviewBinding.tvRowPaymentCouponTitle.text = it
//        }
//
//        if(coupon_list[position].couponType == CouponType.SALE_RATE.str) {
//            rowPaymentCouponRecyclerviewViewModel.tv_row_payment_coupon_rate.observe(viewLifecycleOwner) {
//                rowPaymentCouponRecyclerviewBinding.tvRowPaymentCouponPrice.text = it.toString() + "%"
//            }
//        }
//        else {
//            rowPaymentCouponRecyclerviewViewModel.tv_row_payment_coupon_price.observe(viewLifecycleOwner) {
//                val formattedPrice = NumberFormat.getNumberInstance(Locale.KOREA).format(it) + "원"
//                rowPaymentCouponRecyclerviewBinding.tvRowPaymentCouponPrice.text = formattedPrice
//            }
//        }
//
//        rowPaymentCouponRecyclerviewViewModel.tv_row_payment_coupon_condition.observe(viewLifecycleOwner) {
//            rowPaymentCouponRecyclerviewBinding.tvRowPaymentCouponCondition.text = it
//        }
//
//        rowPaymentCouponRecyclerviewViewModel.tv_row_payment_coupon_end_date.observe(viewLifecycleOwner) {
//            val date = it.toDate().time // Timestamp를 Date로 변환
//            val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
//            simpleDateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul") // 한국 시간대 설정
//            rowPaymentCouponRecyclerviewBinding.tvRowPaymentCouponPeriod.text = "~" + simpleDateFormat.format(date)
//        }
//
//    }
//
//    // 뷰모델 초기 값 세팅 메소드
//    fun setting_viewmodel_value(
//        rowPaymentCouponRecyclerviewViewModel: RowPaymentCouponRecyclerviewViewModel,
//        rowPaymentCouponRecyclerviewBinding: RowPaymentCouponRecyclerviewBinding,
//        position: Int
//    ) {
//        rowPaymentCouponRecyclerviewBinding.apply {
//
//            // 쿠폰 제목 뷰모델 세팅
//            rowPaymentCouponRecyclerviewViewModel.tv_row_payment_coupon_title.value =
//                coupon_list[position].title
//
//            // 쿠폰 할인 가격 뷰모델 세팅
//            rowPaymentCouponRecyclerviewViewModel.tv_row_payment_coupon_price.value =
//                coupon_list[position].salePrice
//
//            // 쿠폰 할인률 뷰모델 세팅
//            rowPaymentCouponRecyclerviewViewModel.tv_row_payment_coupon_rate.value =
//                coupon_list[position].saleRate
//
//            // 쿠폰 조건 뷰모델 세팅
//            rowPaymentCouponRecyclerviewViewModel.tv_row_payment_coupon_condition.value =
//                coupon_list[position].conditionDescription
//
//            // 쿠폰 ~까지 뷰모델 세팅
//            rowPaymentCouponRecyclerviewViewModel.tv_row_payment_coupon_end_date.value =
//                coupon_list[position].endCouponDate
//
//        }
//    }

}