package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.RowShoppingCartRecyclerviewViewModel
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.ShoppingCartViewModel
import com.nemodream.bangkkujaengi.databinding.FragmentShoppingCartBinding
import com.nemodream.bangkkujaengi.databinding.RowShoppingCartRecyclerviewBinding

class ShoppingCartFragment : Fragment() {

    lateinit var fragmentShoppingCartBinding: FragmentShoppingCartBinding
    val shoppingCartViewModel: ShoppingCartViewModel by viewModels()

    var productList = Array(3) {
        "상품 테스트 ${it}"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShoppingCartBinding  = FragmentShoppingCartBinding.inflate(inflater, container, false)

        // 툴바 세팅 메소드 호출
        setting_toolbar()
        // 초기 총 상품 가격 요약 텍스트 세팅 메소드 호출
        setting_price_textview()
        // 버튼 기능 메소드 호출
        fn_button()
        // recycelrview 초기 세팅 메소드 호출
        setting_recyclerview()

        return fragmentShoppingCartBinding.root
    }

    // 툴바 세팅 메소드
    fun setting_toolbar() {
        
    }

    // 초기 총 상품 가격 요약 텍스트 세팅 메소드
    fun setting_price_textview() {
        // 총 상품 가격
        shoppingCartViewModel.tv_shopping_cart_tot_price_text.observe(viewLifecycleOwner){
            fragmentShoppingCartBinding.tvShoppingCartTotPrice.text = it.toString()
        }

        // 총 할인 가격
        shoppingCartViewModel.tv_shopping_cart_tot_sale_price_text.observe(viewLifecycleOwner){
            fragmentShoppingCartBinding.tvShoppingCartTotSalePrice.text = it.toString()
        }

        // 총 배송비 가격
        shoppingCartViewModel.tv_shopping_cart_tot_delivery_cost_text.observe(viewLifecycleOwner){
            fragmentShoppingCartBinding.tvShoppingCartTotDeliveryCost.text = it.toString()
        }

        // 총 합 금액
        // 처음에 배송비 3000을 더해준다
        shoppingCartViewModel.tv_shopping_cart_tot_sum_price_text.value =
            shoppingCartViewModel.tv_shopping_cart_tot_price_text.value!! +
                    shoppingCartViewModel.tv_shopping_cart_tot_delivery_cost_text.value!!
        shoppingCartViewModel.tv_shopping_cart_tot_sum_price_text.observe(viewLifecycleOwner){
            fragmentShoppingCartBinding.tvShoppingCartTotSumPrice.text = it.toString()
        }
    }

    // 버튼 기능 메소드
    fun fn_button() {
        
    }

    // recycelrview 초기 세팅 메소드
    fun setting_recyclerview() {
        fragmentShoppingCartBinding.apply {
            rvShoppingCartProductList.adapter = shopping_cart_recyclerview_adapter()
            rvShoppingCartProductList.layoutManager = LinearLayoutManager(context)
        }
    }

    inner class shopping_cart_recyclerview_adapter : RecyclerView.Adapter<shopping_cart_recyclerview_adapter.shopping_cart_recyclerview_viewholder>() {

        inner class shopping_cart_recyclerview_viewholder(val rowShoppingCartRecyclerviewBinding: RowShoppingCartRecyclerviewBinding) :
            RecyclerView.ViewHolder(rowShoppingCartRecyclerviewBinding.root)

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): shopping_cart_recyclerview_viewholder {
            val rowShoppingCartRecyclerviewBinding = RowShoppingCartRecyclerviewBinding.inflate(layoutInflater, parent, false)
            val shopping_cart_recyclerview_viewholder = shopping_cart_recyclerview_viewholder(rowShoppingCartRecyclerviewBinding)
            val rowShoppingCartViewModel = RowShoppingCartRecyclerviewViewModel()

            //////////////////////////// ViewModel 값 설정 및 UI 업데이트 /////////////////////////////
            // 상품명 업데이트
            rowShoppingCartViewModel.tv_row_shopping_cart_product_name.observe(viewLifecycleOwner) {
                rowShoppingCartRecyclerviewBinding.tvRowShoppingCartProductName.text = it
            }
            // 할인 비율 업데이트
            rowShoppingCartViewModel.tv_row_shopping_cart_product_sale_percent.observe(viewLifecycleOwner) {
                rowShoppingCartRecyclerviewBinding.tvRowShoppingCartProductSalePercent.text = it.toString()
            }
            // 원가 (할인 전 가격) 업데이트
            rowShoppingCartViewModel.tv_row_shopping_cart_product_origin_price.observe(viewLifecycleOwner) {
                rowShoppingCartRecyclerviewBinding.tvRowShoppingCartProductOriginPrice.text = it.toString()
            }
            // 판매가 (할인 후 가격) 업데이트
            rowShoppingCartViewModel.tv_row_shopping_cart_product_sale_price.observe(viewLifecycleOwner) {
                rowShoppingCartRecyclerviewBinding.tvRowShoppingCartProductSalePrice.text = it.toString()
            }
            // 수량 업데이트
            rowShoppingCartViewModel.tv_row_shopping_cart_product_cnt.observe(viewLifecycleOwner) {
                rowShoppingCartRecyclerviewBinding.tvRowShoppingCartProductCnt.text = it.toString()
            }

            // 총 상품 가격 업데이트 ( 각 아이템들의 할인 전 가격을 더해서 총 상품 가격에 저장하기 위함 )
            shoppingCartViewModel.tv_shopping_cart_tot_price_text.value =
                    shoppingCartViewModel.tv_shopping_cart_tot_price_text.value!! +
                            rowShoppingCartViewModel.tv_row_shopping_cart_product_origin_price.value!!
            

            shoppingCartViewModel.tv_shopping_cart_tot_sale_price_text.value =
                shoppingCartViewModel.tv_shopping_cart_tot_sale_price_text.value!! +
                        (rowShoppingCartViewModel.tv_row_shopping_cart_product_origin_price.value!! -
                                rowShoppingCartViewModel.tv_row_shopping_cart_product_sale_price.value!!)

            // 총 합 금액 업데이트 ( 각 아이템들의 할인된 가격을 더해서 총 합 금액에 저장하기 위함 )
            shoppingCartViewModel.tv_shopping_cart_tot_sum_price_text.value =
                shoppingCartViewModel.tv_shopping_cart_tot_sum_price_text.value!! +
                rowShoppingCartViewModel.tv_row_shopping_cart_product_sale_price.value!!


            // 수량 증가 버튼 클릭 이벤트
            rowShoppingCartRecyclerviewBinding.ibtnRowShoppingCartProductCntAdd.setOnClickListener {
                // 수량 1 증가
                rowShoppingCartViewModel.tv_row_shopping_cart_product_cnt.value =
                    rowShoppingCartViewModel.tv_row_shopping_cart_product_cnt.value?.plus(1)

                // 할인 전 상품 가격 증가
                rowShoppingCartViewModel.tv_row_shopping_cart_product_origin_price.value =
                    rowShoppingCartViewModel.tv_row_shopping_cart_product_origin_price.value!! *
                            rowShoppingCartViewModel.tv_row_shopping_cart_product_cnt.value!!
                
                // 할인 후 상품 가격 증가
                
            }
            // 수량 증가 버튼 클릭 이벤트
            rowShoppingCartRecyclerviewBinding.ibtnRowShoppingCartProductCntRemove.setOnClickListener {
                if (rowShoppingCartViewModel.tv_row_shopping_cart_product_cnt.value!! > 0) {
                    rowShoppingCartViewModel.tv_row_shopping_cart_product_cnt.value =
                        rowShoppingCartViewModel.tv_row_shopping_cart_product_cnt.value?.minus(1)
                }
            }



//            rowShoppingCartRecyclerviewBinding.root.setOnClickListener {
//                Log.d("test100", "클릭")
//            }


            return shopping_cart_recyclerview_viewholder
        }

        override fun getItemCount(): Int {
            return productList.size
        }

        override fun onBindViewHolder(
            holder: shopping_cart_recyclerview_viewholder,
            position: Int
        ) {
            // holder.rowShoppingCartRecyclerviewBinding.tvRowShoppingCartProductName.text = productList[position]
        }

    }
}