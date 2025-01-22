package com.nemodream.bangkkujaengi.admin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.customer.data.model.Coupon
import com.nemodream.bangkkujaengi.databinding.RowPaymentSelectCouponRecyclerviewBinding
import com.nemodream.bangkkujaengi.utils.toCommaString
import com.nemodream.bangkkujaengi.utils.toFormattedDate

class AdminCouponAdapter(
    private val onCouponDeleteListener: OnCouponDeleteListener,
): ListAdapter<Coupon, AdminCouponAdapter.AdminCouponViewHolder>(CouponDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminCouponViewHolder {
        return AdminCouponViewHolder(
            RowPaymentSelectCouponRecyclerviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onCouponDelete = { position -> onCouponDeleteListener.onCouponDelete(getItem(position)) }
        )
    }

    override fun onBindViewHolder(holder: AdminCouponViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AdminCouponViewHolder(
        private val binding: RowPaymentSelectCouponRecyclerviewBinding,
        onCouponDelete: (position: Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnRowRowPaymentCouponDelete.setOnClickListener {
                onCouponDelete(adapterPosition)
            }
        }

        fun bind(coupon: Coupon) {
            with(binding) {
                tvRowPaymentCouponTitle.text = coupon.title
                tvRowPaymentCouponPrice.text =
                    if (coupon.couponType == "SALE_RATE") "${coupon.saleRate} %" else "${coupon.salePrice.toCommaString()} 원"
                tvRowPaymentCouponCondition.text = coupon.conditionDescription
                tvRowPaymentCouponPeriod.text = "~ ${coupon.endCouponDate?.toFormattedDate()}"
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

interface OnCouponDeleteListener {
    fun onCouponDelete(coupon: Coupon)
}