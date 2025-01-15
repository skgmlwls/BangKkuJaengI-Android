package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.customer.data.model.Product
import com.nemodream.bangkkujaengi.customer.data.model.ShoppingCart
import com.nemodream.bangkkujaengi.customer.data.repository.ShoppingCartRepository
import com.nemodream.bangkkujaengi.customer.ui.adapter.ShoppingCartAdapter
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.RowShoppingCartRecyclerviewViewModel
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.ShoppingCartViewModel
import com.nemodream.bangkkujaengi.databinding.FragmentShoppingCartBinding
import com.nemodream.bangkkujaengi.databinding.RowShoppingCartRecyclerviewBinding
import com.nemodream.bangkkujaengi.utils.loadImage
import com.nemodream.bangkkujaengi.utils.navigateToChildFragment
import com.nemodream.bangkkujaengi.utils.replaceParentFragment
import com.nemodream.bangkkujaengi.utils.replaceParentFragment2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class ShoppingCartFragment : Fragment() {

    lateinit var fragmentShoppingCartBinding: FragmentShoppingCartBinding
    val shoppingCartViewModel: ShoppingCartViewModel by viewModels()

    var productList = Array(3) {
        "상품 테스트 ${it}"
    }

    // var cart_document_id_list = mutableListOf<String>()

    var user_id = "testuser"

    // 장바구니 document id를 변수
    var cart_user_id:String = ""
    // 장바구니 아이템 정보를 담을 리스트
    // var cart_data_list = mutableListOf<ShoppingCart>()
    var cart_data_list = ShoppingCart()

    // 장바구니에 담긴 상품 정보를 담을 리스트
    var cart_product_data_list = mutableListOf<Product>()

    var checked_product_data_list = mutableListOf<Product>()

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
        fn_btn_shopping_cart_buy()
//        // recycelrview 초기 세팅 메소드 호출
//        setting_recyclerview()
        // recycelrview 업데이트 메소드 호출
        refresh_recyclerview_shopping_cart()


        fn_test_button()

        return fragmentShoppingCartBinding.root
    }

    // 툴바 세팅 메소드
    fun setting_toolbar() {

    }

    // 초기 총 상품 가격 요약 텍스트 세팅 메소드
    fun setting_price_textview() {

        //////////////////////////////// 상품 가격 요약 업데이트 //////////////////////////////////////
        // 총 상품 가격 업데이트
        shoppingCartViewModel.tv_shopping_cart_tot_price_text.observe(viewLifecycleOwner){
            val formattedPrice = NumberFormat.getNumberInstance(Locale.KOREA).format(it) + " 원"
            fragmentShoppingCartBinding.tvShoppingCartTotPrice.text = formattedPrice
        }

        // 총 할인 가격 업데이트
        shoppingCartViewModel.tv_shopping_cart_tot_sale_price_text.observe(viewLifecycleOwner){
            val formattedPrice = "- " + NumberFormat.getNumberInstance(Locale.KOREA).format(it) + " 원"
            fragmentShoppingCartBinding.tvShoppingCartTotSalePrice.text = formattedPrice
        }

        // 총 배송비 가격 업데이트
        shoppingCartViewModel.tv_shopping_cart_tot_delivery_cost_text.observe(viewLifecycleOwner){
            val formattedPrice = "+ " + NumberFormat.getNumberInstance(Locale.KOREA).format(it) + " 원"
            fragmentShoppingCartBinding.tvShoppingCartTotDeliveryCost.text = formattedPrice
        }
        
        // 총 합 금액  업데이트
        shoppingCartViewModel.tv_shopping_cart_tot_sum_price_text.observe(viewLifecycleOwner){
            val formattedPrice = NumberFormat.getNumberInstance(Locale.KOREA).format(it) + " 원"
            fragmentShoppingCartBinding.tvShoppingCartTotSumPrice.text = formattedPrice
        }
        ////////////////////////////////////////////////////////////////////////////////////////////

    }

    // 버튼 기능 메소드
    fun fn_btn_shopping_cart_buy() {
        shoppingCartViewModel.btn_shopping_cart_buy_text.observe(viewLifecycleOwner){
            fragmentShoppingCartBinding.btnShoppingCartBuy.text = it
        }
        fragmentShoppingCartBinding.btnShoppingCartBuy.setOnClickListener {
            val data_budle = Bundle()
            // 유저 ID 전달
            data_budle.putString("user_id", user_id)
            // 유저 전화 번호 전달
            data_budle.putString("user_phone_number", "01034381511")
            // 유저 주소 전달
            data_budle.putString("user_address", "서울시 강남구 역삼동")
            replaceParentFragment2(PaymentFragment(), "payment_fragment", data_budle)
        }
    }

    fun fn_test_button() {
        fragmentShoppingCartBinding.btnShoppingCartItemAdd.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO) {
                    ShoppingCartRepository.add_cart_items_by_product_ids(user_id)
                }.await()
            }
        }
    }

    // recycelrview 업데이트 메소드
    fun refresh_recyclerview_shopping_cart() {

        // 로딩바 활성화
        fragmentShoppingCartBinding.pdShoppingCartProductListLoading.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.Main).launch {

            // 장바구니 데이터를 가져온다
            val work1 = async(Dispatchers.IO) {
                // 테스트용 아이디를 임의로 넣었음
                ShoppingCartRepository.getting_shopping_cart_item_by_userId(user_id)
            }.await()

            cart_user_id = ""
            cart_data_list = ShoppingCart()

            cart_product_data_list = mutableListOf<Product>()

            if (work1.size == 0) {
                fragmentShoppingCartBinding.pdShoppingCartProductListLoading.visibility = View.GONE
                fragmentShoppingCartBinding.tvShoppingCartEmptyText.visibility = View.VISIBLE
                fragmentShoppingCartBinding.btnShoppingCartSelectProductDelete.visibility = View.GONE
                fragmentShoppingCartBinding.tvShoppingCartEmptyText.setText("장바구니가\n비어있습니다")
                return@launch
            }
            work1.forEach {
                // 장바구니 document id를 가져온다
                cart_user_id = it["cart_document_id"] as String

                // 장바구니 데이터를 가져온다
                work1.map {
                    val cart_data = it["cart_data"] as ShoppingCart
                    cart_data.items.forEach {
                        cart_data_list = cart_data
                    }
                }
            }

            Log.d("asd", "${cart_user_id}")

            // 장바구니 데이터에서 가져온 상품 document_id를 통해 상품정보를 가져온다
            val work2 = async(Dispatchers.IO) {
                // Log.d("test300", "setting_toolbar: ${cart_data_list[0].items.forEach { it.productId }}")
                ShoppingCartRepository.getting_prodcut_by_product_document_id(cart_data_list.items.map { it.productId })
            }.await()
            work2.forEach {
                cart_product_data_list.add(it["product_data"] as Product)
            }

            cart_product_data_list.forEach {
                Log.d("test300", "setting_toolbar: ${it.productName}")
                Log.d("test300", "setting_toolbar: ${it.images}")
            }

            // recycelrview 초기 세팅 메소드 호출
            setting_recyclerview()

            // 버튼 텍스트 업데이트
            shoppingCartViewModel.btn_shopping_cart_buy_text.value = "구매하기"

            // 로딩바 비활성화
            fragmentShoppingCartBinding.pdShoppingCartProductListLoading.visibility = View.GONE
            // recyclerview 활성화
            fragmentShoppingCartBinding.rvShoppingCartProductList.visibility = View.VISIBLE

            fragmentShoppingCartBinding.rvShoppingCartProductList.adapter?.notifyDataSetChanged()
        }
    }

    // recycelrview 초기 세팅 메소드
    fun setting_recyclerview() {
        fragmentShoppingCartBinding.rvShoppingCartProductList.apply {
            adapter = ShoppingCartAdapter(
                cart_user_id,
                cart_product_data_list,
                cart_data_list,
                viewLifecycleOwner,
                shoppingCartViewModel,
                fragmentShoppingCartBinding,
                this@ShoppingCartFragment
            ) { refresh_recyclerview_shopping_cart() } // Refresh callback
            layoutManager = LinearLayoutManager(context)
        }
    }


}