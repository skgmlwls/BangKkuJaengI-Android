package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nemodream.bangkkujaengi.customer.data.model.Coupon
import com.nemodream.bangkkujaengi.customer.data.model.CouponType
import com.nemodream.bangkkujaengi.customer.data.model.PaymentProduct
import com.nemodream.bangkkujaengi.customer.data.model.Product
import com.nemodream.bangkkujaengi.customer.data.repository.PaymentRepository
import com.nemodream.bangkkujaengi.customer.ui.adapter.PaymentProductAdapter
import com.nemodream.bangkkujaengi.customer.ui.adapter.SelectCouponAdapter
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.PaymentViewModel
import com.nemodream.bangkkujaengi.databinding.FragmentPaymentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class PaymentFragment : Fragment() {

    lateinit var fragmentPaymentBinding: FragmentPaymentBinding

    val paymentViewModel: PaymentViewModel by viewModels()

    // 테스트
    val testData = Array(3) {
        "상품명 $it"
    }

    // 유저 ID
    var user_id: String = ""
    // 유저 전화번호
    var user_phone_number: String = ""
    // 유저 주소
    var user_address: String = ""

    var payment_product_user_id = ""

    // 장바구니에서 체크표시한 상품의 갯수, 상품 문서 id 등을 담는 변수
    var payment_product_list = PaymentProduct()

    // 결제할 상품 정보를 담을 리스트
    // var payment_product_data_list = mutableListOf<Product>()

    // 선택된 쿠폰 목록을 담을 리스트
    var checked_coupon_document_id_list = mutableListOf<String>()


    // var select_coupon_list = mutableListOf<Coupon>()

    var selectedPosition = 0

    var selectedDocumentId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentPaymentBinding = FragmentPaymentBinding.inflate(inflater, container, false)

        // 유저 아이디 세팅
        getting_user_id()
        // 리사이클러뷰 설정
        setting_recycledrview_order()
        // 버튼 설정
        setting_button()
        // textInputLayout의 값을 세팅하고 업데이트 하는 메소드 호출
        setting_textInputLayout_delivery_address()
        // 결제 금액 텍스트 옵저버 세팅
        setting_payment_amount_text_observe()
        // 선택된 쿠폰 옵저버 세팅
        // setting_select_coupon_observe()

        
        // 선택된 쿠폰 목록 recyclerview 설정
        setting_recyclerview_select_coupon()
        // 선택된 쿠폰 리스트 초기화
        setting_checked_coupon_list()

        return fragmentPaymentBinding.root
    }

    fun setting_checked_coupon_list() {
        // PaymentCouponBottomSheetFragment에서 결과 수신
        parentFragmentManager.setFragmentResultListener("couponResultKey", viewLifecycleOwner) { requestKey, result ->
            // 선택한 쿠폰 리스트 초기화
            paymentViewModel.select_coupon_list.value?.clear()
            // 선택한 쿠폰 문서 id 리스트 초기화
            checked_coupon_document_id_list.clear()

            this.selectedPosition = result.getInt("select_position")
            this.selectedDocumentId = result.getString("select_document_id")!!

            Log.d("12345", "selectedPosition : ${selectedPosition}")
            Log.d("12345", "selectedDocumentId : ${selectedDocumentId}")

            checked_coupon_document_id_list.clear()

            // 쿠폰 리스트 중 선택된 position 을 ViewModel 에 설정
            paymentViewModel.checked_position.value = selectedPosition
            checked_coupon_document_id_list.add(selectedDocumentId!!)

            // setting_checked_coupon_list(selectedDocumentId, selectedPosition)

            refresh_select_coupon_recyclerview()
        }
    }

    // 유저 아이디 세팅 메소드
    fun getting_user_id() {
        arguments?.let {
            user_id = PaymentFragmentArgs.fromBundle(it).userId
            user_phone_number = PaymentFragmentArgs.fromBundle(it).userPhoneNumber
            user_address = PaymentFragmentArgs.fromBundle(it).userAddress
        }
    }

    // textInputLayout의 값을 세팅하고 업데이트 메소드
    fun setting_textInputLayout_delivery_address() {

        // 이름 뷰모델 세팅
        paymentViewModel.til_payment_name_text.value = user_id
        // 전화번호 뷰모델 세팅
        paymentViewModel.til_payment_phone_number_text.value = user_phone_number
        // 주소 뷰모델 세팅
        paymentViewModel.til_payment_address_text.value = user_address
        
        // 이름 옵저버
        paymentViewModel.til_payment_name_text.observe(viewLifecycleOwner) {
            fragmentPaymentBinding.tilPaymentName.editText?.setText(it) // 값을 설정
        }
        // 전화번호 옵저버
        paymentViewModel.til_payment_phone_number_text.observe(viewLifecycleOwner) {
            fragmentPaymentBinding.tilPaymentPhoneNumber.editText?.setText(it)
        }
        // 주소 옵저버
        // 초기 배송지의 주소 값을 세팅
        paymentViewModel.til_payment_address_text.observe(viewLifecycleOwner) {
            fragmentPaymentBinding.tilPaymentAddress.editText?.setText(it)
        }
    }

    // 결제 금액 텍스트 세팅
    fun setting_payment_amount_text_observe() {

        paymentViewModel.payment_product_data_list.observe(viewLifecycleOwner) {

            // 총 상품 가격 옵저버
            paymentViewModel.tv_payment_tot_price_text.observe(viewLifecycleOwner) {
                val formattedPrice = NumberFormat.getNumberInstance(Locale.KOREA).format(it) + " 원"
                fragmentPaymentBinding.tvPaymentTotPrice.text = formattedPrice
            }

            // 총 할인 가격 옵저버
            paymentViewModel.tv_payment_tot_sale_price_text.observe(viewLifecycleOwner) {
                val formattedPrice = "- " + NumberFormat.getNumberInstance(Locale.KOREA).format(it) + " 원"
                fragmentPaymentBinding.tvPaymentTotSalePrice.text = formattedPrice
            }

            // 총 합 금액
            paymentViewModel.tv_payment_tot_sum_price_text.observe(viewLifecycleOwner) {
                val formattedPrice = NumberFormat.getNumberInstance(Locale.KOREA).format(it) + " 원"
                fragmentPaymentBinding.tvPaymentTotSumPrice.text = formattedPrice
            }

            // 배송비 옵저버
            paymentViewModel.tv_payment_tot_delivery_cost_text.observe(viewLifecycleOwner) {
                val formattedPrice = "+ " + NumberFormat.getNumberInstance(Locale.KOREA).format(it) + " 원"
                fragmentPaymentBinding.tvPaymentTotDeliveryCost.text = formattedPrice
            }

            // 쿠폰 할인 옵저버
//            paymentViewModel.tv_payment_coupon_sale_price_text.observe(viewLifecycleOwner) {
//                val formattedPrice = "- " + NumberFormat.getNumberInstance(Locale.KOREA).format(it) + " 원"
//                fragmentPaymentBinding.tvPaymentCouponSalePrice.text = formattedPrice
//            }

        }
    }

    // 선택된 쿠폰 옵저버 세팅
    fun setting_select_coupon_observe() {

        paymentViewModel.select_coupon_list.observe(viewLifecycleOwner) {

            paymentViewModel.tv_payment_coupon_sale_price_text.observe(viewLifecycleOwner) {
                val formattedPrice = NumberFormat.getNumberInstance(Locale.KOREA).format(it) + " 원"
                fragmentPaymentBinding.tvPaymentTotSalePrice.text = formattedPrice
                paymentViewModel.tv_payment_tot_sum_price_text.value =
                    paymentViewModel.tv_payment_tot_price_text.value!! -
                            paymentViewModel.tv_payment_tot_sale_price_text.value!! -
                            it
            }

        }

    }

    fun setting_button() {

        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {

            }
        }

        fragmentPaymentBinding.apply {
            btnPaymentCouponListShow.setOnClickListener {

                // 쿠폰 목록 보여주기
                // PaymentCouponBottomSheetFragment 에 데이터 전달
                val bottomSheetFragment = PaymentCouponBottomSheetFragment(this@PaymentFragment). apply {
                    arguments = Bundle().apply {
                        putString("user_id", user_id)
//                        if (checked_coupon_document_id_list.size != 0) {
//                            putString("selected_document_id", checked_coupon_document_id_list[0])
//                        }
                    }
                }
                bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)

            }
        }
    }

    /////////////////////////////////// 주문 목록 recyclerview //////////////////////////////////////
    fun setting_recycledrview_order() {

        // 장바구니에서 체크된 데이터를 가져온다
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                PaymentRepository.getting_payment_product_by_checked(user_id)
            }
            val result = work1.await()

            // 디버깅용 로그 추가
            result.forEach {
                val cartData = it["checked_cart_data"] as? PaymentProduct
                payment_product_user_id = cartData!!.userId
                cartData.items.forEach {
                    payment_product_list = cartData
                }
            }

            Log.d("test1234", "payment_product_list : ${payment_product_list}")

            // 장바구니에서 체크된 데이터에서 가져온 상품 document_id를 통해 상품정보를 가져온다
            val work2 = async(Dispatchers.IO) {
                PaymentRepository.getting_prodcut_by_product_document_id(payment_product_list.items.map { it.productId })
            }.await()

            work2.forEach {
                paymentViewModel.payment_product_data_list.value?.add(it["product_data"] as Product)
            }


            // 결제 금액 텍스트 값 세팅
            var position = 0
            paymentViewModel.payment_product_data_list.value?.forEach {

                // 총 상품 가격 뷰모델 값 세팅
                paymentViewModel.tv_payment_tot_price_text.value =
                    paymentViewModel.tv_payment_tot_price_text.value?.plus(it.price * payment_product_list.items[position].quantity)

                // 총 할인 가격 뷰모델 값 세팅
                paymentViewModel.tv_payment_tot_sale_price_text.value =
                    paymentViewModel.tv_payment_tot_sale_price_text.value?.plus(
                        (it.price - (it.price * (1 - (it.saleRate / 100.0))).toInt() ) * payment_product_list.items[position].quantity
                    )

                // 총 합 금액 뷰모델 값 세팅
                paymentViewModel.tv_payment_tot_sum_price_text.value =
                    paymentViewModel.tv_payment_tot_sum_price_text.value?.plus(
                        ((it.price * (1 - (it.saleRate / 100.0))).toInt() * payment_product_list.items[position].quantity)
                    )


                position++
            }

            // 총 합 금액에 배송비 추가
            paymentViewModel.tv_payment_tot_sum_price_text.value =
                paymentViewModel.tv_payment_tot_sum_price_text.value?.plus(
                    paymentViewModel.tv_payment_tot_delivery_cost_text.value!!
                )

            Log.d("test1234", "payment_product_data_list : ${paymentViewModel.tv_payment_tot_price_text.value}")

            fragmentPaymentBinding.apply {
                rvPaymentOrderProductList.adapter = PaymentProductAdapter(
                    payment_product_list,
                    paymentViewModel.payment_product_data_list.value!!,
                    paymentViewModel,
                    viewLifecycleOwner
                )
                rvPaymentOrderProductList.layoutManager = LinearLayoutManager(context)
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////// 선택된 쿠폰 목록 recyclerview ///////////////////////////////////
    fun refresh_select_coupon_recyclerview() {
        viewLifecycleOwner.lifecycleScope.launch {
            val work1 = async(Dispatchers.IO) {
                Log.d("512", "checked_coupon_document_id_list : ${checked_coupon_document_id_list}")
                PaymentRepository.getting_coupon_by_select_coupon_document_id(checked_coupon_document_id_list)
            }
            val coupon_list = work1.await()
            // Log.d("666", "coupon_document_id_list : ${paymentViewModel.select_coupon_list.value?}")

            coupon_list.forEach {
                paymentViewModel.select_coupon_list.value?.add(it["coupon_data"] as Coupon)
            }

            if (coupon_list.size != 0) {
                paymentViewModel.select_coupon_list.value?.forEach {
                    if (it.couponType == CouponType.SALE_PRICE.str) {
                        paymentViewModel.tv_payment_tot_sum_price_text.value =
                            paymentViewModel.tv_payment_tot_sum_price_text.value?.minus(it.salePrice)
                        paymentViewModel.tv_payment_coupon_sale_price_text.value =
                            it.salePrice
                    }
                }
            }
            else {
                paymentViewModel.tv_payment_coupon_sale_price_text.value = 0
            }

            // Log.d("5", "coupon_document_id_list : ${paymentViewModel.select_coupon_list.value?}")
            fragmentPaymentBinding.rvPaymentSelectCouponList.adapter?.notifyDataSetChanged()
        }
    }

    fun setting_recyclerview_select_coupon() {
        fragmentPaymentBinding.apply {
            rvPaymentSelectCouponList.adapter = SelectCouponAdapter(
                paymentViewModel.select_coupon_list.value!!,
                this@PaymentFragment,
                viewLifecycleOwner
            )
            rvPaymentSelectCouponList.layoutManager = LinearLayoutManager(context)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

}