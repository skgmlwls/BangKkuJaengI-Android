package com.nemodream.bangkkujaengi.customer.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.customer.data.model.Coupon
import com.nemodream.bangkkujaengi.customer.data.model.CouponType
import com.nemodream.bangkkujaengi.customer.ui.fragment.PaymentFragment
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.RowPaymentCouponRecyclerviewViewModel
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.RowPaymentSelectCouponRecyclerviewViewModel
import com.nemodream.bangkkujaengi.databinding.RowPaymentCouponRecyclerviewBinding
import com.nemodream.bangkkujaengi.databinding.RowPaymentSelectCouponRecyclerviewBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class SelectCouponAdapter(
    val checked_coupon_list : MutableList<Coupon>,
    val paymentFragment: PaymentFragment,
    val viewLifecycleOwner: LifecycleOwner
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

        // 옵저버 세팅 메소드 호출
        setting_text_observe(
            holder.rowPaymentSelectCouponRecyclerviewBinding,
            rowPaymentSelectCouponRecyclerviewViewModel,
            position
        )

        // 뷰모델 초기 값 세팅 메소드 호출
        setting_viewmodel_value(
            holder.rowPaymentSelectCouponRecyclerviewBinding,
            rowPaymentSelectCouponRecyclerviewViewModel,
            position
        )

        holder.rowPaymentSelectCouponRecyclerviewBinding.btnRowRowPaymentCouponDelete.setOnClickListener {
            paymentFragment.checked_coupon_document_id_list.clear()
            paymentFragment.select_coupon_list.clear()

            Log.d("5", "${paymentFragment.checked_coupon_document_id_list}")

            paymentFragment.refresh_select_coupon_recyclerview()
        }


    }

    // 옵저버 세팅 메소드
    fun setting_text_observe(
        rowPaymentSelectCouponRecyclerviewBinding: RowPaymentSelectCouponRecyclerviewBinding,
        rowPaymentSelectCouponRecyclerviewViewModel: RowPaymentSelectCouponRecyclerviewViewModel,
        position: Int
    ) {
        // 쿠폰 제목 옵저버
        rowPaymentSelectCouponRecyclerviewViewModel.tv_row_payment_select_coupon_title.observe(viewLifecycleOwner) {
            rowPaymentSelectCouponRecyclerviewBinding.tvRowPaymentCouponTitle.text = it
        }

        // 쿠폰 할인 비율 옵저버
        if(checked_coupon_list[position].couponType == CouponType.SALE_RATE.str) {
            rowPaymentSelectCouponRecyclerviewViewModel.tv_row_payment_select_coupon_rate.observe(viewLifecycleOwner) {
                rowPaymentSelectCouponRecyclerviewBinding.tvRowPaymentCouponPrice.text = it.toString() + "%"
            }
        }
        // 쿠폰 할인 금액 옵저버
        else {
            rowPaymentSelectCouponRecyclerviewViewModel.tv_row_payment_select_coupon_price.observe(viewLifecycleOwner) {
                val formattedPrice = NumberFormat.getNumberInstance(Locale.KOREA).format(it) + "원"
                rowPaymentSelectCouponRecyclerviewBinding.tvRowPaymentCouponPrice.text = formattedPrice
            }
        }

        // 할인 조건  옵저버
        rowPaymentSelectCouponRecyclerviewViewModel.tv_row_payment_select_coupon_condition.observe(viewLifecycleOwner) {
            rowPaymentSelectCouponRecyclerviewBinding.tvRowPaymentCouponCondition.text = it
        }

        // 쿠폰 끝나는 기간 옵저버
        rowPaymentSelectCouponRecyclerviewViewModel.tv_row_payment_select_coupon_period.observe(viewLifecycleOwner) {
            val date = it.toDate().time // Timestamp를 Date로 변환
            val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
            simpleDateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul") // 한국 시간대 설정
            rowPaymentSelectCouponRecyclerviewBinding.tvRowPaymentCouponPeriod.text = "~" + simpleDateFormat.format(date)
        }

    }

    // 뷰모델 초기 값 세팅 메소드
    fun setting_viewmodel_value(
        rowPaymentSelectCouponRecyclerviewBinding: RowPaymentSelectCouponRecyclerviewBinding,
        rowPaymentSelectCouponRecyclerviewViewModel: RowPaymentSelectCouponRecyclerviewViewModel,
        position: Int
    ) {
        rowPaymentSelectCouponRecyclerviewBinding.apply {

            // 쿠폰 제목 뷰모델 세팅
            rowPaymentSelectCouponRecyclerviewViewModel.tv_row_payment_select_coupon_title.value =
                checked_coupon_list[position].title

            // 쿠폰 할인 가격 뷰모델 세팅
            rowPaymentSelectCouponRecyclerviewViewModel.tv_row_payment_select_coupon_price.value =
                checked_coupon_list[position].salePrice

            // 쿠폰 할인률 뷰모델 세팅
            rowPaymentSelectCouponRecyclerviewViewModel.tv_row_payment_select_coupon_rate.value =
                checked_coupon_list[position].saleRate

            // 쿠폰 조건 뷰모델 세팅
            rowPaymentSelectCouponRecyclerviewViewModel.tv_row_payment_select_coupon_condition.value =
                checked_coupon_list[position].conditionDescription

            // 쿠폰 ~까지 뷰모델 세팅
            rowPaymentSelectCouponRecyclerviewViewModel.tv_row_payment_select_coupon_period.value =
                checked_coupon_list[position].endCouponDate

        }
    }

}