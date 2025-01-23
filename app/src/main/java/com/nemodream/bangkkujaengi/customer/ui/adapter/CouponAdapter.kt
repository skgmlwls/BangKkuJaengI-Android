package com.nemodream.bangkkujaengi.customer.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.customer.data.model.Coupon
import com.nemodream.bangkkujaengi.databinding.RowPaymentSelectCouponRecyclerviewBinding
import com.nemodream.bangkkujaengi.utils.toCommaString
import com.nemodream.bangkkujaengi.utils.toFormattedDate

class CouponAdapter: ListAdapter<Coupon, CouponAdapter.CouponViewHolder>(CouponDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        return CouponViewHolder(
            RowPaymentSelectCouponRecyclerviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
        )
    }

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CouponViewHolder(
        private val binding: RowPaymentSelectCouponRecyclerviewBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(coupon: Coupon) {
            with(binding) {
                tvRowPaymentCouponTitle.text = coupon.title
                tvRowPaymentCouponPrice.text =
                    if (coupon.couponType == "SALE_RATE") "${coupon.saleRate} %" else "${coupon.salePrice.toCommaString()} 원"
                tvRowPaymentCouponCondition.text = coupon.conditionDescription
                tvRowPaymentCouponPeriod.text = "~ ${coupon.endCouponDate?.toFormattedDate()}"
                btnRowRowPaymentCouponDelete.visibility = View.GONE
            }
        }
    }
}

class CouponDiffCallback : DiffUtil.ItemCallback<Coupon>() {
    override fun areItemsTheSame(oldItem: Coupon, newItem: Coupon): Boolean {
        return oldItem.startCouponDate == newItem.startCouponDate
    }

    override fun areContentsTheSame(oldItem: Coupon, newItem: Coupon): Boolean {
        return oldItem == newItem
    }
}