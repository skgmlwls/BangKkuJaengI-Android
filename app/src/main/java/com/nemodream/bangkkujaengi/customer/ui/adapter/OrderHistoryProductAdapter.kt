package com.nemodream.bangkkujaengi.customer.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.customer.data.model.PurchaseState
import com.nemodream.bangkkujaengi.customer.data.repository.ShoppingCartRepository
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.OrderHistoryProductViewModel
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.OrderHistoryViewModel
import com.nemodream.bangkkujaengi.databinding.RowOrderHistoryProductBinding
import com.nemodream.bangkkujaengi.utils.loadImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class OrderHistoryProductAdapter(
    val orderHistoryViewModel : OrderHistoryViewModel,
    val orderHistoryProductViewModel : OrderHistoryProductViewModel,
) : RecyclerView.Adapter<OrderHistoryProductAdapter.OrderHistoryProductViewHolder>() {

    inner class OrderHistoryProductViewHolder(val rowOrderHistoryProductBinding: RowOrderHistoryProductBinding) :
            RecyclerView.ViewHolder(rowOrderHistoryProductBinding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderHistoryProductViewHolder {
        val rowOrderHistoryProductBinding = RowOrderHistoryProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return OrderHistoryProductViewHolder(rowOrderHistoryProductBinding)
    }

    override fun getItemCount(): Int = orderHistoryProductViewModel.order_history_product_list.value!!.size

    override fun onBindViewHolder(holder: OrderHistoryProductViewHolder, position: Int) {

        // 주문 상품 리스트 이미지 세팅
        setting_product_list_image(holder.rowOrderHistoryProductBinding, position)

        // 주문 상품 리스트 텍스트 세팅
        setting_product_list_text(holder.rowOrderHistoryProductBinding, position)

    }

    // 주문 상품 리스트 텍스트 세팅
    fun setting_product_list_text(holder: RowOrderHistoryProductBinding, position: Int) {

        // 주문 상품 이름
        holder.tvRowOrderHistoryProductProductName.text =
            orderHistoryProductViewModel.order_history_product_list.value!![position].productTitle

        // 주문 상품 가격
        val formattedPrice = NumberFormat.getNumberInstance(Locale.KOREA).format(
            orderHistoryProductViewModel.order_history_product_list.value!![position].totPrice
        ) + " 원"
        holder.tvRowOrderHistoryProductPrice.text = formattedPrice

        // 주문 상품 수량
        val formattedCnt = NumberFormat.getNumberInstance(Locale.KOREA).format(
            orderHistoryProductViewModel.order_history_product_list.value!![position].purchaseQuantity
        ) + "개"
        holder.tvRowOrderHistoryProductCnt.text = formattedCnt

        holder.tvRowOrderHistoryProductDeliveryState.text = when(orderHistoryProductViewModel.order_history_product_list.value!![position].purchaseState) {
            PurchaseState.READY_TO_SHIP.name -> "배송 준비 중"
            PurchaseState.SHIPPING.name -> "배송중"
            else -> "배송완료"
        }

    }

    // 주문 상품 리스트 이미지 세팅
    fun setting_product_list_image(holder: RowOrderHistoryProductBinding, position: Int) {
        // 상품 이미지 로드
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                ShoppingCartRepository.getting_image(orderHistoryProductViewModel.order_history_product_list.value!![position].images)
            }
            val uri_image = work1.await()

            holder.ivRowOrderHistoryProductImage.loadImage(uri_image)
        }
    }

}