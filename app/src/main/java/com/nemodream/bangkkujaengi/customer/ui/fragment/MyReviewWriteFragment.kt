package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.databinding.FragmentMyReviewWriteBinding
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.MyReviewWriteViewModel
import com.nemodream.bangkkujaengi.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyReviewWriteFragment : Fragment() {

    private lateinit var binding: FragmentMyReviewWriteBinding
    private val viewModel: MyReviewWriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyReviewWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupStarRating()
        loadProductData()
        observeViewModel()
    }

    private fun setupToolbar() {
        binding.toolbarReviewWrite.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupStarRating() {
        val stars = listOf(
            binding.star1, binding.star2, binding.star3,
            binding.star4, binding.star5
        )

        stars.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                viewModel.selectRating(index + 1)
            }
        }

        // 별점 변화 관찰 및 UI 업데이트
        viewModel.rating.observe(viewLifecycleOwner) { rating ->
            stars.forEachIndexed { index, imageView ->
                if (index < rating) {
                    imageView.setImageResource(R.drawable.ic_star_fill)
                } else {
                    imageView.setImageResource(R.drawable.ic_star_outline)
                }
            }
        }
    }

    private fun loadProductData() {
        // arguments에서 전달받은 productId를 가져와서 ViewModel에 전달
        val productId = arguments?.getString("productId") ?: return
        viewModel.loadProductData(productId)
    }

    private fun observeViewModel() {
        // 상품명 업데이트
        viewModel.productName.observe(viewLifecycleOwner) { productName ->
            binding.tvReviewProductName.text = productName
        }

        // 상품 이미지 업데이트
        viewModel.productImageUrl.observe(viewLifecycleOwner) { imageUrl ->
            binding.imgReviewProduct.loadImage(imageUrl)  // 확장 함수 사용
        }
    }
}
