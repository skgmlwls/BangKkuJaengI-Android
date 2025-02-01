package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nemodream.bangkkujaengi.customer.ui.adapter.ReviewWrittenListAdapter
import com.nemodream.bangkkujaengi.customer.ui.adapter.WrittenReview
import com.nemodream.bangkkujaengi.databinding.FragmentMyReviewWrittenListBinding

class MyReviewWrittenListFragment : Fragment() {

    private lateinit var binding: FragmentMyReviewWrittenListBinding
    private val reviewListAdapter by lazy { ReviewWrittenListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyReviewWrittenListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 리사이클러뷰 설정
        binding.recyclerReviewWritten.apply {
            adapter = reviewListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // 샘플 데이터 로드
        loadWrittenReviews()
    }

    // 작성한 리뷰 데이터 로드 (샘플)
    private fun loadWrittenReviews() {
        val sampleReviews = listOf(
            WrittenReview("상품 A", 4, "작성일: 2025.01.06", "작성한 리뷰 내용입니다."),
            WrittenReview("상품 B", 5, "작성일: 2024.12.12", "리뷰 작성 내용 샘플입니다.")
        )
        reviewListAdapter.submitList(sampleReviews)
    }
}
