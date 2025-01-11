package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nemodream.bangkkujaengi.customer.data.model.CategoryType
import com.nemodream.bangkkujaengi.databinding.FragmentCategoryProductBinding
import com.nemodream.bangkkujaengi.utils.popBackStack

class CategoryProductFragment: Fragment() {
    private var _binding: FragmentCategoryProductBinding? = null
    private val binding get() = _binding!!

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
        setupListeners()
        setupTabs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    * 리스너 모음 함수
    * */
    private fun setupListeners() {
        with(binding) {
            toolbarHome.setNavigationOnClickListener {
                popBackStack()
            }
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

        // 전달받은 categoryType에 해당하는 탭 선택
        val initialPosition = categoryType?.ordinal ?: 0
        binding.tabCategory.getTabAt(initialPosition)?.select()
    }

    companion object {
        private const val KEY_CATEGORY_TYPE = "category_type"

        fun newInstance(type: CategoryType): CategoryProductFragment {
            return CategoryProductFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_CATEGORY_TYPE, type.name)
                }
            }
        }
    }
}