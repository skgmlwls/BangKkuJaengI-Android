package com.nemodream.bangkkujaengi.customer.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.OrderHistoryProductViewModel
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.OrderHistoryViewModel
import com.nemodream.bangkkujaengi.databinding.RowOrderHistoryBinding
import java.text.SimpleDateFormat
import java.util.Locale

class OrderHistoryAdapter(
    val orderHistoryViewModel: OrderHistoryViewModel,
    ) : RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {

    inner class OrderHistoryViewHolder(val rowOrderHistoryBinding: RowOrderHistoryBinding) :
        RecyclerView.ViewHolder(rowOrderHistoryBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val rowOrderHistoryBinding = RowOrderHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return OrderHistoryViewHolder(rowOrderHistoryBinding)
    }

    override fun getItemCount(): Int = orderHistoryViewModel.order_history_date_list.value!!.size

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val orderHistoryProductViewModel = OrderHistoryProductViewModel()

        // yyyyMMdd 형식의 날짜를 yyyy년 mm월 dd일로 변환
        holder.rowOrderHistoryBinding.tvRowOrderHistoryDate.text = convertDateFormat(
            orderHistoryViewModel.order_history_date_list.value!![position].toString()
        )

        setting_order_history_prodcut_list_by_date(orderHistoryViewModel, orderHistoryProductViewModel, position)

        setting_order_history_product_recyclerview(holder.rowOrderHistoryBinding, orderHistoryProductViewModel, holder.itemView.context)

    }

    // yyyyMMdd 형식의 날짜를 yyyy년 mm월 dd일로 변환하는 메소드
    fun convertDateFormat(rawDate: String): String {
        return try {
            // 입력 형식 정의
            val inputFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            // 출력 형식 정의
            val outputFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())

            // 입력 날짜 파싱 후 새로운 형식으로 변환
            val date = inputFormat.parse(rawDate)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            rawDate // 변환 실패 시 원본 반환
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////

    // 날짜에 맞는 항목을 따로 리스트에 넣는 함수
    fun setting_order_history_prodcut_list_by_date(
        orderHistoryViewModel: OrderHistoryViewModel,
        orderHistoryProductViewModel: OrderHistoryProductViewModel,
        position: Int,
    ) {

        // 날짜에 해당하는 항목을 필터링하여 새로운 리스트 생성
        val filteredList = orderHistoryViewModel.order_history_product_list.value?.filter {
            val dateFormatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val uniqueDates = dateFormatter.format(it.purchaseDate?.toDate()).toInt()
            orderHistoryViewModel.order_history_date_list.value!![position] == uniqueDates
        }

        orderHistoryProductViewModel.order_history_product_list.value = filteredList

        Log.d("filteredList", "${orderHistoryProductViewModel.order_history_product_list.value}")
    }

    // 날짜에 해당하는 항목 recyclerview //////////////////////////////////////////////////////////////
    fun setting_order_history_product_recyclerview(
        rowOrderHistoryBinding: RowOrderHistoryBinding,
        orderHistoryProductViewModel: OrderHistoryProductViewModel,
        context: Context
    ) {
        rowOrderHistoryBinding.apply {
            rvOrderHistoryProduct.adapter = OrderHistoryProductAdapter(
                orderHistoryViewModel,
                orderHistoryProductViewModel
            )
            rvOrderHistoryProduct.layoutManager = LinearLayoutManager(context)
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


}