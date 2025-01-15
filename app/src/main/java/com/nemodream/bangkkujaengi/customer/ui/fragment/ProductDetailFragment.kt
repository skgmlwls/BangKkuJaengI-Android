package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.tabs.TabLayoutMediator
import com.nemodream.bangkkujaengi.customer.data.model.CategoryType
import com.nemodream.bangkkujaengi.customer.ui.adapter.ProductDetailBannerAdapter
import com.nemodream.bangkkujaengi.customer.ui.adapter.ProductDetailImageAdapter
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.ProductDetailViewModel
import com.nemodream.bangkkujaengi.databinding.FragmentProductDetailBinding
import com.nemodream.bangkkujaengi.utils.loadImage
import com.nemodream.bangkkujaengi.utils.popBackStack
import com.nemodream.bangkkujaengi.utils.toCommaString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailFragment: Fragment() {
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductDetailViewModel by viewModels()

    private val adapter: ProductDetailBannerAdapter by lazy { ProductDetailBannerAdapter() }
    private val imageAdapter: ProductDetailImageAdapter by lazy { ProductDetailImageAdapter() }

    // 상태바 색상 변경을 위한 window 객체 초기화
    private val window: Window by lazy { activity?.window ?: throw IllegalStateException("Activity is null") }

    // 번들 객체를 통해 받은 아이디 프로퍼티로 저장
    private val productId: String by lazy { arguments?.getString("PRODUCT_ID") ?: "" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toggleStatusBarColor()
        viewModel.loadProduct(productId)
        observeViewModel()
        setupListeners()
        binding.rvProductDetailContentImageList.adapter = imageAdapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        toggleStatusBarColor()
        _binding = null
    }

    /*
    * 상태바 색상 변경
    * */
    private fun toggleStatusBarColor() {
        val currentColor = window.statusBarColor
        window.statusBarColor = when (currentColor) {
            Color.TRANSPARENT -> Color.WHITE
            Color.WHITE -> Color.TRANSPARENT
            else -> Color.WHITE // 기본값 설정
        }
    }

    // LiveData 관찰
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.product.observe(viewLifecycleOwner) {
                    adapter.submitList(it.images)
                    setupUI()
                }
            }
        }
    }

    private fun setupUI() {
        val product = viewModel.product.value ?: return
        setupProductBannerUI()
        with(binding) {
            tvProductDetailTitle.text = product.productName
            tvProductDetailPrice.text = "${product.price.toCommaString()}원"
            tvProductDetailDiscountPrice.text = "${product.saledPrice.toCommaString()}원"
            ivProductDetailContentImage.loadImage(product.images.first())
            tvProductDetailContentDescription.text = product.description

            tvProductDetailIsBest.visibility = if (product.isBest) View.VISIBLE else View.GONE
            tvProductDetailCategory.text = CategoryType.fromString(product.category.name).getTabTitle()

            // product.images에서 2번째 값부터 리스트에 넣는다
            val subList = product.images.subList(1, product.images.size)
            imageAdapter.submitList(subList)
        }
    }

    // 상품 이미지 배너 UI 설정
    private fun setupProductBannerUI() {
        with(binding) {
            viewPagerProductImage.adapter = adapter
            // TabLayout과 ViewPager2 연동
            TabLayoutMediator(viewpagerDetailIndicator, viewPagerProductImage) { _, _ -> }
                .attach()
        }
    }

    /*
    * 리스너 설정
    * */
    private fun setupListeners() {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                popBackStack()
            }

            btnProductOrder.setOnClickListener {
                // BottomSheet 표시
                val bottomSheet = ProductOrderBottomSheetFragment.newInstance(productId)
                bottomSheet.show(childFragmentManager, bottomSheet.tag)
            }
        }
    }

    /*
    * newInstance: 프래그먼트 인스턴스 생성
    * productId를 Bundle객체로 전달 받는다.
    * */
    companion object {
        fun newInstance(productId: String): ProductDetailFragment {
            return ProductDetailFragment().apply {
                arguments = Bundle().apply {
                    putString("PRODUCT_ID", productId)
                }
            }
        }
    }
}