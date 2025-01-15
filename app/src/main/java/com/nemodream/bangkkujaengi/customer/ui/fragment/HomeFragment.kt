package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.customer.data.model.Banner
import com.nemodream.bangkkujaengi.customer.data.model.CategoryType
import com.nemodream.bangkkujaengi.customer.data.model.Product
import com.nemodream.bangkkujaengi.customer.ui.adapter.HomeBannerAdapter
import com.nemodream.bangkkujaengi.customer.ui.adapter.OnBannerItemClickListener
import com.nemodream.bangkkujaengi.customer.ui.adapter.PromotionAdapter
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.HomeViewModel
import com.nemodream.bangkkujaengi.databinding.FragmentHomeBinding
import com.nemodream.bangkkujaengi.utils.replaceParentFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), OnBannerItemClickListener, (Product) -> Unit {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private val bannerAdapter: HomeBannerAdapter by lazy { HomeBannerAdapter(this) }
    private val promotionAdapter: PromotionAdapter by lazy { PromotionAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setupLayout()
        setupListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    /*
    * 배너 클릭 리스너
    * */
    override fun onItemClick(banner: Banner) {
        replaceParentFragment(ProductDetailFragment.newInstance(banner.productId), "HomeFragment")
    }

    /*
    * HomeViewModel에서 데이터 불러와
    * 변경이 감지되면 데이터 갱신
    * */
    private fun observeViewModel() {
        viewModel.bannerItems.observe(viewLifecycleOwner) { bannerList ->
            bannerAdapter.submitList(bannerList)
        }

        viewModel.promotionItems.observe(viewLifecycleOwner) { items ->
            promotionAdapter.submitList(items)
        }
    }

    /*
    * 화면 UI 배치 관련 함수 모음
    * */
    private fun setupLayout() {
        viewModel.loadBannerItems()
        viewModel.loadPromotions()
        setupHomeBannerUI()
        binding.rvPromotion.adapter = promotionAdapter
    }

    /*
    * 홈 배너 화면
    * */
    private fun setupHomeBannerUI() {
        with(binding) {
            viewPagerCarousel.adapter = bannerAdapter
            // TabLayout과 ViewPager2 연동
            TabLayoutMediator(tabIndicator, viewPagerCarousel) { _, _ -> }
                .attach()
        }

    }

    /*
    * 리스너 모음 함수
    * */
    private fun setupListeners() {
        with(binding) {
            categoryFurniture.root.setOnClickListener {
                replaceParentFragment(
                    CategoryProductFragment.newInstance(CategoryType.FURNITURE),
                    CategoryType.FURNITURE.name
                )
            }

            categoryLighting.root.setOnClickListener {
                replaceParentFragment(
                    CategoryProductFragment.newInstance(CategoryType.LIGHTING),
                    CategoryType.LIGHTING.name
                )
            }

            categoryFabric.root.setOnClickListener {
                replaceParentFragment(
                    CategoryProductFragment.newInstance(CategoryType.FABRIC),
                    CategoryType.FABRIC.name
                )
            }

            categoryDeco.root.setOnClickListener {
                replaceParentFragment(
                    CategoryProductFragment.newInstance(CategoryType.DECO),
                    CategoryType.DECO.name
                )
            }

            categoryBroadcast.root.setOnClickListener {
                replaceParentFragment(
                    CategoryProductFragment.newInstance(CategoryType.BROADCAST),
                    CategoryType.BROADCAST.name
                )
            }

            // 각 메뉴 버튼에 따라서 화면 이동하기
            toolbarHome.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_search -> {
                        replaceParentFragment(SearchFragment(), "HomeFragment")
                        true
                    }

                    R.id.menu_cart -> {
                        replaceParentFragment(ShoppingCartFragment(), "HomeFragment")
                        true
                    }
                    else -> false
                }
            }
        }
    }

    override fun invoke(p1: Product) {
        TODO("Not yet implemented")
    }
}