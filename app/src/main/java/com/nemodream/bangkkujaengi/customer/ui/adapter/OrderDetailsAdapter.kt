package com.nemodream.bangkkujaengi.customer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.customer.data.repository.ShoppingCartRepository
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.OrderDetailsViewModel
import com.nemodream.bangkkujaengi.databinding.RowOrderDetailsRecyclerviewBinding
import com.nemodream.bangkkujaengi.utils.loadImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class OrderDetailsAdapter(
    val orderDetailsViewModel : OrderDetailsViewModel,
    val viewLifecycleOwner : LifecycleOwner
) : RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>() {

    inner class OrderDetailsViewHolder(val rowOrderDetailsRecyclerviewBinding: RowOrderDetailsRecyclerviewBinding) :
        RecyclerView.ViewHolder(rowOrderDetailsRecyclerviewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val rowOrderDetailsRecyclerviewBinding = RowOrderDetailsRecyclerviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return OrderDetailsViewHolder(rowOrderDetailsRecyclerviewBinding)
    }

    override fun getItemCount(): Int = orderDetailsViewModel.order_history_product_list.value!!.size

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {

        // 상품 이미지 세팅
        setting_order_details_image(orderDetailsViewModel.order_history_product_list.value!![position].images, holder)

        // 리사이클러뷰 텍스트 세팅
        setting_order_details_recyclerview(holder.rowOrderDetailsRecyclerviewBinding, position)

    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////

    fun setting_order_details_image(image : String, holder: OrderDetailsViewHolder) {
        // 상품 이미지 로드
        viewLifecycleOwner.lifecycleScope.launch {
            val work1 = async(Dispatchers.IO) {
                ShoppingCartRepository.getting_image(image)
            }
            val uri_image = work1.await()

            holder.rowOrderDetailsRecyclerviewBinding.ivRowOrderDetailsProductImage.loadImage(uri_image)
        }
    }

    // 텍스트 세팅
    fun setting_order_details_recyclerview(rowOrderDetailsRecyclerviewBinding : RowOrderDetailsRecyclerviewBinding, position: Int) {
        rowOrderDetailsRecyclerviewBinding.apply {
            // 상품명 텍스트
            tvRowOrderDetailsProductName.text = orderDetailsViewModel.order_history_product_list.value!![position].productTitle

            // 옵션 텍스트
            if (orderDetailsViewModel.order_history_product_list.value!![position].color != "") {
                tvRowOrderDetailsProductOption.text = "옵션 : " + orderDetailsViewModel.order_history_product_list.value!![position].color
            }
            else {
                tvRowOrderDetailsProductOption.text = "옵션 : 없음"
            }

            // 세일 비율 텍스트
            tvRowOrderDetailsProductSalePercent.text = orderDetailsViewModel.order_history_product_list.value!![position].saleRate.toString() + "%"

            // 상품 할인 전 가격
            tvRowOrderDetailsProductOriginPrice.text  = NumberFormat.getNumberInstance(Locale.KOREA).format(
                orderDetailsViewModel.order_history_product_list.value!![position].productCost
            ) + " 원"

            // 상품 할인 후 가격
            tvRowOrderDetailsProductSalePrice.text  = NumberFormat.getNumberInstance(Locale.KOREA).format(
                orderDetailsViewModel.order_history_product_list.value!![position].totPrice
            ) + " 원"

            // 상품 갯수
            tvRowOrderDetailsProductCnt.text = NumberFormat.getNumberInstance(Locale.KOREA).format(
                orderDetailsViewModel.order_history_product_list.value!![position].purchaseQuantity
            ) + "개"
        }
    }

}