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
    lateinit var cart_data_list: ShoppingCart

    // 장바구니에 담긴 상품 정보를 담을 리스트
    var cart_product_data_list = mutableListOf<Product>()

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
        // recycelrview 초기 세팅 메소드 호출
        setting_recyclerview()
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

            // 버튼 텍스트 업데이트
            shoppingCartViewModel.btn_shopping_cart_buy_text.value = "구매하기"

            // 로딩바 비활성화
            fragmentShoppingCartBinding.pdShoppingCartProductListLoading.visibility = View.GONE
            // recyclerview 활성화
            fragmentShoppingCartBinding.rvShoppingCartProductList.visibility = View.VISIBLE
            // recycelrview 업데이트

            fragmentShoppingCartBinding.rvShoppingCartProductList.adapter?.notifyDataSetChanged()
        }
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

            return shopping_cart_recyclerview_viewholder(rowShoppingCartRecyclerviewBinding)
        }

        override fun getItemCount(): Int {
            return cart_product_data_list.size
        }

        override fun onBindViewHolder(
            holder: shopping_cart_recyclerview_viewholder,
            position: Int
        ) {
            val rowShoppingCartViewModel = RowShoppingCartRecyclerviewViewModel()
            Log.d("test333", "setting_toolbar: ${cart_data_list.items[position].checked}")

            // 상품 이미지 로딩
            holder.rowShoppingCartRecyclerviewBinding.pbRowShoppingCartProductImageLoading.visibility = View.VISIBLE
            holder.rowShoppingCartRecyclerviewBinding.ivRowShoppingCartProductImage.visibility = View.GONE

            // 상품 이미지 로드
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO) {
                    ShoppingCartRepository.getting_image(cart_product_data_list[position].images[0])
                }
                val uri_image = work1.await()
                holder.rowShoppingCartRecyclerviewBinding.pbRowShoppingCartProductImageLoading.visibility = View.GONE
                holder.rowShoppingCartRecyclerviewBinding.ivRowShoppingCartProductImage.visibility = View.VISIBLE
                holder.rowShoppingCartRecyclerviewBinding.ivRowShoppingCartProductImage.loadImage(uri_image)
            }

            /////////////////////////////ViewModel 초기 값 설정////////////////////////////////////////
            // 상품 선택 여부 초기 데이터 저장
            rowShoppingCartViewModel.cb_row_shopping_cart_product_select_checked.value =
                    cart_data_list.items[position].checked

            // 상품명 초기 데이터 저장
            rowShoppingCartViewModel.tv_row_shopping_cart_product_name.value =
                cart_product_data_list[position].productName

            // 할인 비율 초기 데이터 저장
            rowShoppingCartViewModel.tv_row_shopping_cart_product_sale_percent.value =
                cart_product_data_list[position].saleRate

            // 할인 전 가격 초기 데이터 저장
            rowShoppingCartViewModel.tv_row_shopping_cart_product_origin_price.value =
                cart_product_data_list[position].price * cart_data_list.items[position].quantity


            // 할인 후 가격 초기 데이터 저장
            // 원가
            val originalPrice = cart_product_data_list[position].price
            // 할인율
            val discountRate = cart_product_data_list[position].saleRate
            rowShoppingCartViewModel.tv_row_shopping_cart_product_sale_price.value =
                ((originalPrice * (1 - (discountRate / 100.0))).toInt() * cart_data_list.items[position].quantity)

            // 상품 갯수 초기 데이터 저장
            rowShoppingCartViewModel.tv_row_shopping_cart_product_cnt.value =
                cart_data_list.items[position].quantity

            // 상품 이미지 초기 경로 저장
            rowShoppingCartViewModel.iv_row_shopping_cart_product_image.value = 
                cart_product_data_list[position].images[0]

            //////////////////////////// 상품 가격 요약 텍스트 초기 데이터 //////////////////////////////
            // 총 상품 가격 초기 데이터 저장
            if (cart_data_list.items[position].checked) {
                shoppingCartViewModel.tv_shopping_cart_tot_price_text.value =
                    shoppingCartViewModel.tv_shopping_cart_tot_price_text.value!!.plus(
                        rowShoppingCartViewModel.tv_row_shopping_cart_product_origin_price.value!!
                    )

                shoppingCartViewModel.tv_shopping_cart_tot_sale_price_text.value =
                    shoppingCartViewModel.tv_shopping_cart_tot_sale_price_text.value!!.plus(
                        rowShoppingCartViewModel.tv_row_shopping_cart_product_origin_price.value!! -
                                rowShoppingCartViewModel.tv_row_shopping_cart_product_sale_price.value!!
                    )

                // 총 합 금액 업데이트
                shoppingCartViewModel.tv_shopping_cart_tot_sum_price_text.value =
                    shoppingCartViewModel.tv_shopping_cart_tot_price_text.value!! -
                            shoppingCartViewModel.tv_shopping_cart_tot_sale_price_text.value!! +
                            shoppingCartViewModel.tv_shopping_cart_tot_delivery_cost_text.value!!
            }

            ////////////////////////////////////////////////////////////////////////////////////////


            ////////////////////////////////////// UI 업데이트 ///////////////////////////////////////
            rowShoppingCartViewModel.cb_row_shopping_cart_product_select_checked.observe(viewLifecycleOwner) {
                holder.rowShoppingCartRecyclerviewBinding.cbRowShoppingCartProductSelect.isChecked = it
            }

            // 상품명 업데이트
            rowShoppingCartViewModel.tv_row_shopping_cart_product_name.observe(viewLifecycleOwner) {
                holder.rowShoppingCartRecyclerviewBinding.tvRowShoppingCartProductName.text = it
            }
            // 할인 비율 업데이트
            rowShoppingCartViewModel.tv_row_shopping_cart_product_sale_percent.observe(viewLifecycleOwner) {
                holder.rowShoppingCartRecyclerviewBinding.tvRowShoppingCartProductSalePercent.text = it.toString() + "%"
            }
            // 원가 (할인 전 가격) 업데이트
            rowShoppingCartViewModel.tv_row_shopping_cart_product_origin_price.observe(viewLifecycleOwner) {
                val formattedPrice = NumberFormat.getNumberInstance(Locale.KOREA).format(it) + " 원"
                holder.rowShoppingCartRecyclerviewBinding.tvRowShoppingCartProductOriginPrice.apply {
                    text = formattedPrice
                    // Paint.STRIKE_THRU_TEXT_FLAG : 텍스트에 사선을 추가하는 플래그
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG // 텍스트에 사선 추가
                }
            }
            // 판매가 (할인 후 가격) 업데이트
            rowShoppingCartViewModel.tv_row_shopping_cart_product_sale_price.observe(viewLifecycleOwner) {
                val formattedPrice = NumberFormat.getNumberInstance(Locale.KOREA).format(it) + " 원"
                holder.rowShoppingCartRecyclerviewBinding.tvRowShoppingCartProductSalePrice.text = formattedPrice
            }
            // 수량 업데이트
            rowShoppingCartViewModel.tv_row_shopping_cart_product_cnt.observe(viewLifecycleOwner) {
                holder.rowShoppingCartRecyclerviewBinding.tvRowShoppingCartProductCnt.text = it.toString()
            }
            ////////////////////////////////////////////////////////////////////////////////////////////


            /////////////////////////////// 버튼, 체크 박스 이벤트 처리  ///////////////////////////////

            // 체크박스 상태 변경 시 결제 가격 요약을 업데이트
            holder.rowShoppingCartRecyclerviewBinding.cbRowShoppingCartProductSelect.setOnCheckedChangeListener { _, isChecked ->
                rowShoppingCartViewModel.cb_row_shopping_cart_product_select_checked.value = isChecked

                // 상품의 CheckBox를 체크할 경우
                if (isChecked) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val work1 = async(Dispatchers.IO) {
                            ShoppingCartRepository.update_cart_item_checked(
                                cart_user_id,
                                cart_data_list.items[position].productId,
                                isChecked
                            )
                        }.join()
                        // 총 상품 가격 업데이트
                        shoppingCartViewModel.tv_shopping_cart_tot_price_text.value =
                            shoppingCartViewModel.tv_shopping_cart_tot_price_text.value!!.plus(
                                rowShoppingCartViewModel.tv_row_shopping_cart_product_origin_price.value!!
                            )

                        // 총 할인 가격 업데이트
                        shoppingCartViewModel.tv_shopping_cart_tot_sale_price_text.value =
                            shoppingCartViewModel.tv_shopping_cart_tot_sale_price_text.value!!.plus(
                                rowShoppingCartViewModel.tv_row_shopping_cart_product_origin_price.value!! -
                                        rowShoppingCartViewModel.tv_row_shopping_cart_product_sale_price.value!!
                            )

                        // 총 합 금액 업데이트
                        shoppingCartViewModel.tv_shopping_cart_tot_sum_price_text.value =
                            shoppingCartViewModel.tv_shopping_cart_tot_price_text.value!! -
                                    shoppingCartViewModel.tv_shopping_cart_tot_sale_price_text.value!! +
                                    shoppingCartViewModel.tv_shopping_cart_tot_delivery_cost_text.value!!

                    }
                }

                // 상품의 CheckBox의 체크를 해제할 경우
                else {
                    CoroutineScope(Dispatchers.Main).launch {
                        val work1 = async(Dispatchers.IO) {
                            ShoppingCartRepository.update_cart_item_checked(
                                cart_user_id,
                                cart_data_list.items[position].productId,
                                isChecked
                            )
                        }.join()

                        // 총 상품 가격 업데이트
                        shoppingCartViewModel.tv_shopping_cart_tot_price_text.value =
                            shoppingCartViewModel.tv_shopping_cart_tot_price_text.value!!.minus(
                                rowShoppingCartViewModel.tv_row_shopping_cart_product_origin_price.value!!
                            )

                        // 총 할인 가격 업데이트
                        shoppingCartViewModel.tv_shopping_cart_tot_sale_price_text.value =
                            shoppingCartViewModel.tv_shopping_cart_tot_sale_price_text.value!!.minus(
                                rowShoppingCartViewModel.tv_row_shopping_cart_product_origin_price.value!! -
                                        rowShoppingCartViewModel.tv_row_shopping_cart_product_sale_price.value!!
                            )

                        shoppingCartViewModel.tv_shopping_cart_tot_sum_price_text.value =
                            shoppingCartViewModel.tv_shopping_cart_tot_sum_price_text.value!!.minus(
                                // 총 상품 가격
                                rowShoppingCartViewModel.tv_row_shopping_cart_product_origin_price.value!! +
                                        // 총 할인 가격
                                        shoppingCartViewModel.tv_shopping_cart_tot_sale_price_text.value!! +
                                        // 총 배송비
                                        shoppingCartViewModel.tv_shopping_cart_tot_delivery_cost_text.value!!
                            )

                        // 총 합 금액 업데이트
                        shoppingCartViewModel.tv_shopping_cart_tot_sum_price_text.value =
                            shoppingCartViewModel.tv_shopping_cart_tot_price_text.value!! -
                                    shoppingCartViewModel.tv_shopping_cart_tot_sale_price_text.value!! +
                                    shoppingCartViewModel.tv_shopping_cart_tot_delivery_cost_text.value!!
                    }

                }

            }

            // 수량 증가 버튼 클릭 이벤트
            holder.rowShoppingCartRecyclerviewBinding.ibtnRowShoppingCartProductCntAdd.setOnClickListener {
                // 버튼 클릭 시 ProgressBar를 표시하고 버튼 숨기기
                holder.rowShoppingCartRecyclerviewBinding.pbRowShoppingCartLoading.visibility = View.VISIBLE
                holder.rowShoppingCartRecyclerviewBinding.ibtnRowShoppingCartProductCntAdd.visibility = View.GONE
                holder.rowShoppingCartRecyclerviewBinding.ibtnRowShoppingCartProductCntRemove.visibility = View.GONE
                holder.rowShoppingCartRecyclerviewBinding.tvRowShoppingCartProductCnt.visibility = View.GONE

                // 수량 1 증가
                rowShoppingCartViewModel.tv_row_shopping_cart_product_cnt.value =
                    rowShoppingCartViewModel.tv_row_shopping_cart_product_cnt.value?.plus(1)

                Log.d("test00", "setting_toolbar: ${cart_product_data_list[position].productId}")
                // 수량 데이터 업데이트
                CoroutineScope(Dispatchers.Main).launch {
                    val work1 = async(Dispatchers.IO) {
                        ShoppingCartRepository.update_cart_item_quantity(
                            cart_user_id,
                            cart_data_list.items[position].productId,
                            rowShoppingCartViewModel.tv_row_shopping_cart_product_cnt.value!!
                        )
                    }
                    work1.await()

                    // 할인 전 가격 값 업데이트
                    rowShoppingCartViewModel.tv_row_shopping_cart_product_origin_price.value =
                        cart_product_data_list[position].price * rowShoppingCartViewModel.tv_row_shopping_cart_product_cnt.value!!


                    // 할인 후 가격 값 업데이트
                    // 원가
                    val originalPrice = cart_product_data_list[position].price
                    // 할인율
                    val discountRate = cart_product_data_list[position].saleRate
                    rowShoppingCartViewModel.tv_row_shopping_cart_product_sale_price.value =
                        ((originalPrice * (1 - (discountRate / 100.0))).toInt() * rowShoppingCartViewModel.tv_row_shopping_cart_product_cnt.value!!)


                    // checkbox 가 true일 경우 총 상품 가격 값을 업데이트 한다.
                    if (rowShoppingCartViewModel.cb_row_shopping_cart_product_select_checked.value!!) {

                        // 총 상품 가격 값 업데이트
                        shoppingCartViewModel.tv_shopping_cart_tot_price_text.value =
                            shoppingCartViewModel.tv_shopping_cart_tot_price_text.value!!.plus(
                                cart_product_data_list[position].price
                            )

                        // 총 할인 가격 값 업데이트
                        // 원가
                        val originalPrice = cart_product_data_list[position].price
                        // 할인율
                        val discountRate = cart_product_data_list[position].saleRate
                        shoppingCartViewModel.tv_shopping_cart_tot_sale_price_text.value =
                            shoppingCartViewModel.tv_shopping_cart_tot_sale_price_text.value!!.plus(
                                cart_product_data_list[position].price -
                                        ((originalPrice * (1 - (discountRate / 100.0))).toInt())
                            )

                        // 총 합 금액 업데이트
                        shoppingCartViewModel.tv_shopping_cart_tot_sum_price_text.value =
                            shoppingCartViewModel.tv_shopping_cart_tot_price_text.value!! -
                                    shoppingCartViewModel.tv_shopping_cart_tot_sale_price_text.value!! +
                                    shoppingCartViewModel.tv_shopping_cart_tot_delivery_cost_text.value!!
                    }

                    // 작업 완료 후 ProgressBar 숨기고 버튼 복원
                    holder.rowShoppingCartRecyclerviewBinding.pbRowShoppingCartLoading.visibility = View.GONE
                    holder.rowShoppingCartRecyclerviewBinding.ibtnRowShoppingCartProductCntAdd.visibility = View.VISIBLE
                    holder.rowShoppingCartRecyclerviewBinding.ibtnRowShoppingCartProductCntRemove.visibility = View.VISIBLE
                    holder.rowShoppingCartRecyclerviewBinding.tvRowShoppingCartProductCnt.visibility = View.VISIBLE
                }
            }

            // 수량 감소 버튼 클릭 이벤트
            holder.rowShoppingCartRecyclerviewBinding.ibtnRowShoppingCartProductCntRemove.setOnClickListener {
                if (rowShoppingCartViewModel.tv_row_shopping_cart_product_cnt.value!! > 0) {
                    // 버튼 클릭 시 ProgressBar를 표시하고 버튼 숨기기
                    holder.rowShoppingCartRecyclerviewBinding.pbRowShoppingCartLoading.visibility = View.VISIBLE
                    holder.rowShoppingCartRecyclerviewBinding.ibtnRowShoppingCartProductCntAdd.visibility = View.GONE
                    holder.rowShoppingCartRecyclerviewBinding.ibtnRowShoppingCartProductCntRemove.visibility = View.GONE
                    holder.rowShoppingCartRecyclerviewBinding.tvRowShoppingCartProductCnt.visibility = View.GONE

                    // 수량 1 감소
                    rowShoppingCartViewModel.tv_row_shopping_cart_product_cnt.value =
                        rowShoppingCartViewModel.tv_row_shopping_cart_product_cnt.value?.minus(1)

                    // 수량 데이터 업데이트
                    CoroutineScope(Dispatchers.Main).launch {
                        val work1 = async(Dispatchers.IO) {
                            ShoppingCartRepository.update_cart_item_quantity(
                                cart_user_id,
                                cart_data_list.items[position].productId,
                                rowShoppingCartViewModel.tv_row_shopping_cart_product_cnt.value!!
                            )
                        }
                        work1.join()

                        // 할인 전 가격 값 업데이트
                        rowShoppingCartViewModel.tv_row_shopping_cart_product_origin_price.value =
                            cart_product_data_list[position].price * rowShoppingCartViewModel.tv_row_shopping_cart_product_cnt.value!!

                        // 할인 후 가격 값 업데이트
                        // 원가
                        val originalPrice = cart_product_data_list[position].price
                        // 할인율
                        val discountRate = cart_product_data_list[position].saleRate
                        rowShoppingCartViewModel.tv_row_shopping_cart_product_sale_price.value =
                            ((originalPrice * (1 - (discountRate / 100.0))).toInt() * rowShoppingCartViewModel.tv_row_shopping_cart_product_cnt.value!!)

                        // checkbox 가 true일 경우 총 상품 가격 값을 업데이트 한다.
                        if (rowShoppingCartViewModel.cb_row_shopping_cart_product_select_checked.value!!) {
                            // 총 상품 가격 값 업데이트
                            shoppingCartViewModel.tv_shopping_cart_tot_price_text.value =
                                shoppingCartViewModel.tv_shopping_cart_tot_price_text.value!!.minus(
                                    cart_product_data_list[position].price
                                )

                            // 총 할인 가격 값 업데이트
                            // 원가
                            val originalPrice = cart_product_data_list[position].price
                            // 할인율
                            val discountRate = cart_product_data_list[position].saleRate
                            shoppingCartViewModel.tv_shopping_cart_tot_sale_price_text.value =
                                shoppingCartViewModel.tv_shopping_cart_tot_sale_price_text.value!!.minus(
                                    cart_product_data_list[position].price -
                                            ((originalPrice * (1 - (discountRate / 100.0))).toInt())
                                )

                            // 총 합 금액 업데이트
                            shoppingCartViewModel.tv_shopping_cart_tot_sum_price_text.value =
                                shoppingCartViewModel.tv_shopping_cart_tot_price_text.value!! -
                                        shoppingCartViewModel.tv_shopping_cart_tot_sale_price_text.value!! +
                                            shoppingCartViewModel.tv_shopping_cart_tot_delivery_cost_text.value!!

                        }

                        // 작업 완료 후 ProgressBar 숨기고 버튼 복원
                        holder.rowShoppingCartRecyclerviewBinding.pbRowShoppingCartLoading.visibility = View.GONE
                        holder.rowShoppingCartRecyclerviewBinding.ibtnRowShoppingCartProductCntAdd.visibility = View.VISIBLE
                        holder.rowShoppingCartRecyclerviewBinding.ibtnRowShoppingCartProductCntRemove.visibility = View.VISIBLE
                        holder.rowShoppingCartRecyclerviewBinding.tvRowShoppingCartProductCnt.visibility = View.VISIBLE
                    }

                }
            }

            // 삭제 버튼 클릭
            holder.rowShoppingCartRecyclerviewBinding.btnRowShoppingCartProductDelete.setOnClickListener {
                // 로딩바 활성화
                fragmentShoppingCartBinding.pdShoppingCartProductListLoading.visibility = View.VISIBLE
                // recyclerview 비활성화
                fragmentShoppingCartBinding.rvShoppingCartProductList.visibility = View.GONE

                CoroutineScope(Dispatchers.Main).launch {
                    val work1 = async(Dispatchers.IO) {
                        ShoppingCartRepository.delete_cart_item_by_product_id(
                            cart_user_id,
                            cart_data_list.items[position].productId
                        )
                    }
                    work1.await()

                    refresh_recyclerview_shopping_cart()
                }
            }

            // 선택 삭제 버튼 클릭
            fragmentShoppingCartBinding.btnShoppingCartSelectProductDelete.setOnClickListener {
                // 로딩바 활성화
                fragmentShoppingCartBinding.pdShoppingCartProductListLoading.visibility = View.VISIBLE
                // recyclerview 비활성화
                fragmentShoppingCartBinding.rvShoppingCartProductList.visibility = View.GONE

                CoroutineScope(Dispatchers.Main).launch {
                    val work1 = async(Dispatchers.IO) {
                        ShoppingCartRepository.delete_cart_item_by_checked(
                            cart_user_id,
                        )
                    }
                    work1.await()

                    rowShoppingCartViewModel.cb_row_shopping_cart_product_select_checked.value = false

                    refresh_recyclerview_shopping_cart()
                }
            }

            ////////////////////////////////////////////////////////////////////////////////////


        }

    }
}