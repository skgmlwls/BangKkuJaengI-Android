package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.nemodream.bangkkujaengi.customer.ui.adapter.ReviewWriteListAdapter
import com.nemodream.bangkkujaengi.databinding.FragmentMyReviewWriteListBinding

class MyReviewWriteListFragment : Fragment() {

    private lateinit var binding: FragmentMyReviewWriteListBinding
    private val reviewListAdapter by lazy {
        ReviewWriteListAdapter { reviewId ->
            navigateToWriteReviewFragment(reviewId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyReviewWriteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 리사이클러뷰 설정
        binding.recyclerReviewWrite.apply {
            adapter = reviewListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // 샘플 데이터 로드
        loadWriteReviews()
    }

    // 리뷰 작성 화면으로 이동
    private fun navigateToWriteReviewFragment(reviewId: String) {
        val action = MyReviewFragmentDirections.actionMyReviewWriteListFragmentToMyReviewWriteFragment(reviewId)
        findNavController().navigate(action)
    }

    // 작성 가능한 리뷰 데이터 로드 (샘플)
    private fun loadWriteReviews() {
        val sampleReviews = listOf("상품 A", "상품 B", "상품 C")
        reviewListAdapter.submitList(sampleReviews)
    }
}
