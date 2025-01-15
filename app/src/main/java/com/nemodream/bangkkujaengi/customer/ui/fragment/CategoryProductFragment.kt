package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.customer.data.model.CategoryType
import com.nemodream.bangkkujaengi.customer.data.model.Product
import com.nemodream.bangkkujaengi.customer.data.model.SortType
import com.nemodream.bangkkujaengi.customer.data.model.SubCategoryType
import com.nemodream.bangkkujaengi.customer.ui.adapter.ProductClickListener
import com.nemodream.bangkkujaengi.customer.ui.adapter.ProductGridAdapter
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.CategoryProductViewModel
import com.nemodream.bangkkujaengi.databinding.FragmentCategoryProductBinding
import com.nemodream.bangkkujaengi.utils.popBackStack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryProductFragment: Fragment(), ProductClickListener {
    private var _binding: FragmentCategoryProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CategoryProductViewModel by viewModels()

    private val adapter: ProductGridAdapter by lazy { ProductGridAdapter(this) }
    private var categoryType: CategoryType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 전달받은 categoryType을 초기화
        categoryType = CategoryType.valueOf(
            arguments?.getString(KEY_CATEGORY_TYPE) ?: CategoryType.FURNITURE.name
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupListeners()
        setupTabs()
        observeViewModel()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onProductClick(product: Product) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.parent_container, ProductDetailFragment.newInstance(product.productId))
            .addToBackStack(null)
            .commit()
    }

    private fun setupUI() {
        binding.rvCategoryProductList.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.products.observe(viewLifecycleOwner) { products ->
            adapter.submitList(products)
        }
    }

    /*
    * 리스너 모음 함수
    * */
    private fun setupListeners() {
        with(binding) {
            toolbarCategoryProduct.setNavigationOnClickListener {
                popBackStack()
            }

            tabCategory.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        setupSubCategoryChips(CategoryType.entries[it.position])
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }

        // 각 메뉴 버튼에 따라서 화면 이동하기
        binding.toolbarCategoryProduct.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_search -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.parent_container, SearchFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }
                else -> false
            }
        }

        binding.chipPromotionSort.setOnClickListener {
            showSortPopup(it)
        }
    }

    /*
    * 카테고리별로 탭을 구성하고
    * 전달 받은 categoryType에 해당하는 탭을 선택한다.
    * */
    private fun setupTabs() {
        CategoryType.entries.forEach { type ->
            binding.tabCategory.addTab(binding.tabCategory.newTab().setText(type.getTabTitle()))
        }

        val initialPosition = categoryType?.ordinal ?: 0

        viewLifecycleOwner.lifecycleScope.launch {
            delay(DELAY_TIME) // 초기 탭 선택 시 자연스러운 애니메이션을 위해 딜레이를 준다.
            binding.tabCategory.selectTab(binding.tabCategory.getTabAt(initialPosition), true)
        }
    }

    fun setupSubCategoryChips(categoryType: CategoryType) {
        binding.chipGroupSubCategory.removeAllViews()
        viewModel.updateCategory(categoryType)  // 카테고리 업데이트

        SubCategoryType.getSubCategories(categoryType).forEach { subCategory ->
            val chip = Chip(requireContext()).apply {
                text = subCategory.title
                tag = subCategory  // SubCategoryType을 tag로 저장

                chipCornerRadius = resources.getDimension(R.dimen.chip_corner_radius)
                setChipBackgroundColorResource(R.color.white)
                chipStrokeWidth = resources.getDimension(R.dimen.chip_stroke_width)
                setChipStrokeColorResource(R.color.black)
                typeface = Typeface.DEFAULT_BOLD

                setOnClickListener {
                    updateChipSelection(this)
                    viewModel.updateSubCategory(tag as SubCategoryType)  // 선택된 서브카테고리 업데이트
                }
            }
            binding.chipGroupSubCategory.addView(chip)
        }

        if (binding.chipGroupSubCategory.childCount > 0) {
            updateChipSelection(binding.chipGroupSubCategory.getChildAt(0) as Chip)
        }
    }

    private fun updateChipSelection(selectedChip: Chip) {
        // ChipGroup의 모든 Chip 순회하며 스타일 업데이트
        with(binding) {
            (0 until chipGroupSubCategory.childCount).forEach { index ->
                when (val chip = chipGroupSubCategory.getChildAt(index) as Chip) {
                    selectedChip -> {
                        // 선택된 Chip 스타일
                        chip.setChipBackgroundColorResource(R.color.black)
                        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    }
                    else -> {
                        // 선택되지 않은 Chip 스타일
                        chip.setChipBackgroundColorResource(R.color.white)
                        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    }
                }
            }
        }

    }

    private fun showSortPopup(view: View) {
        PopupMenu(requireContext(), view).apply {
            menuInflater.inflate(R.menu.product_sort_menu, menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.sort_purchase -> {
                        viewModel.updateSort(SortType.PURCHASE)
                        true
                    }
                    R.id.sort_review -> {
                        viewModel.updateSort(SortType.REVIEW)
                        true
                    }
                    R.id.sort_price_high -> {
                        viewModel.updateSort(SortType.PRICE_HIGH)
                        true
                    }
                    R.id.sort_price_low -> {
                        viewModel.updateSort(SortType.PRICE_LOW)
                        true
                    }
                    R.id.sort_discount -> {
                        viewModel.updateSort(SortType.DISCOUNT)
                        true
                    }
                    R.id.sort_views -> {
                        viewModel.updateSort(SortType.VIEWS)
                        true
                    }
                    else -> false
                }
            }
        }.show()
    }

    companion object {
        private const val KEY_CATEGORY_TYPE = "category_type"
        private const val DELAY_TIME = 100L

        fun newInstance(type: CategoryType): CategoryProductFragment {
            return CategoryProductFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_CATEGORY_TYPE, type.name)
                }
            }
        }
    }

}