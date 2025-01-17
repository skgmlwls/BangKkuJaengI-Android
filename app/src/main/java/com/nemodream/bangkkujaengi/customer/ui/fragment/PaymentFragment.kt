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
    var payment_product_data_list = mutableListOf<Product>()

    // 선택된 쿠폰 목록을 담을 리스트
    var checked_coupon_document_id_list = mutableListOf<String>()

    //
    var select_coupon_list = mutableListOf<Coupon>()

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
        setting_textInputLayout()
        // 선택된 쿠폰 목록 recyclerview 설정
        setting_recyclerview_select_coupon()

        // PaymentCouponBottomSheetFragment에서 결과 수신
        parentFragmentManager.setFragmentResultListener("couponResultKey", viewLifecycleOwner) { requestKey, result ->
            // 선택한 쿠폰 리스트 초기화
            select_coupon_list.clear()
            // 선택한 쿠폰 문서 id 리스트 초기화
//            checked_coupon_document_id_list.clear()

            val selectedPosition = result.getInt("select_position")
            val selectedDocumentId = result.getString("select_document_id")

            // 이전에 선택했던 쿠폰과 같지 않으면 선택한 쿠폰 리스트 초기화 후 선택한 쿠폰 추가
            if (paymentViewModel.checked_position.value != selectedPosition){
                checked_coupon_document_id_list.clear()

                // 쿠폰 리스트 중 선택된 position 을 ViewModel 에 설정
                paymentViewModel.checked_position.value = selectedPosition
                checked_coupon_document_id_list.add(selectedDocumentId!!)
            }

            // setting_checked_coupon_list(selectedDocumentId, selectedPosition)

            refresh_select_coupon_recyclerview()
        }



        return fragmentPaymentBinding.root
    }

    fun setting_checked_coupon_list(selectedDocumentId : String?, selectedPosition : Int) {
        // 이전에 선택해서 삭제했던 쿠폰이 였어도 추가할 수 있도록 체크하는 변수
        var check_coupon = 0

        // 선택된 쿠폰 리스트에 이미 있는 항목이면 그 항목을 삭제한다.
        checked_coupon_document_id_list.forEach {
            if (it == selectedDocumentId) {
                checked_coupon_document_id_list.remove(it)
                check_coupon++
            }
        }

        // 이전에 선택했던 쿠폰과 같지 않으면 선택한 쿠폰 리스트 초기화 후 선택한 쿠폰 추가
        if (paymentViewModel.checked_position.value != selectedPosition){
            checked_coupon_document_id_list.clear()

            // 쿠폰 리스트 중 선택된 position 을 ViewModel 에 설정
            paymentViewModel.checked_position.value = selectedPosition
            checked_coupon_document_id_list.add(selectedDocumentId!!)
        }
        else {
            if (check_coupon == 0) {
                checked_coupon_document_id_list.add(selectedDocumentId!!)
            }
        }

        Log.d("1234", "selectedPosition : ${selectedPosition}")
        Log.d("1234", "selectedDocumentId : ${selectedDocumentId}")
    }

    // 유저 아이디 세팅 메소드
    fun getting_user_id() {
        user_id = arguments?.getString("user_id").toString()
        user_phone_number = arguments?.getString("user_phone_number").toString()
        user_address = arguments?.getString("user_address").toString()
        Log.d("testId", user_id)
    }

    // textInputLayout의 값을 세팅하고 업데이트 메소드
    fun setting_textInputLayout() {

        paymentViewModel.til_payment_name_text.value = user_id
        paymentViewModel.til_payment_phone_number_text.value = user_phone_number
        paymentViewModel.til_payment_address_text.value = user_address
        // 초기 배송지의 이름 값을 세팅
        paymentViewModel.til_payment_name_text.observe(viewLifecycleOwner) {
            fragmentPaymentBinding.tilPaymentName.editText?.setText(it) // 값을 설정
        }
        // 초기 배송지의 전화번호 값을 세팅
        paymentViewModel.til_payment_phone_number_text.observe(viewLifecycleOwner) {
            fragmentPaymentBinding.tilPaymentPhoneNumber.editText?.setText(it)
        }
        // 초기 배송지의 주소 값을 세팅
        paymentViewModel.til_payment_address_text.observe(viewLifecycleOwner) {
            fragmentPaymentBinding.tilPaymentAddress.editText?.setText(it)
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
                payment_product_data_list.add(it["product_data"] as Product)
            }

            payment_product_data_list.forEach {
                Log.d("test555", "payment_product_data_list : ${it}")
            }

            fragmentPaymentBinding.apply {
                rvPaymentOrderProductList.adapter = PaymentProductAdapter(
                    payment_product_list,
                    payment_product_data_list,
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
                Log.d("5", "checked_coupon_document_id_list : ${checked_coupon_document_id_list}")
                PaymentRepository.getting_coupon_by_select_coupon_document_id(checked_coupon_document_id_list)
            }
            val coupon_list = work1.await()
            // Log.d("666", "coupon_document_id_list : ${select_coupon_list}")

            coupon_list.forEach {
                select_coupon_list.add(it["coupon_data"] as Coupon)
            }

            // Log.d("5", "coupon_document_id_list : ${select_coupon_list}")
            fragmentPaymentBinding.rvPaymentSelectCouponList.adapter?.notifyDataSetChanged()
        }
    }

    fun setting_recyclerview_select_coupon() {
        fragmentPaymentBinding.apply {
            rvPaymentSelectCouponList.adapter = SelectCouponAdapter(
                select_coupon_list,
                this@PaymentFragment,
                viewLifecycleOwner
            )
            rvPaymentSelectCouponList.layoutManager = LinearLayoutManager(context)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

}