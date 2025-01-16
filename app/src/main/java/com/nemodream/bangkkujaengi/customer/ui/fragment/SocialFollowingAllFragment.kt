package com.nemodream.bangkkujaengi.customer.ui.fragment

import SocialFollowingAllAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.nemodream.bangkkujaengi.databinding.FragmentSocialFollowingAllBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialFollowingAllFragment : Fragment() {

    private var _binding: FragmentSocialFollowingAllBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SocialFollowingAllViewModel by viewModels()

    // 팔로잉 프로필 RecyclerView의 어댑터
    private val socialFollowingAllAdapter: SocialFollowingAllAdapter by lazy {
        SocialFollowingAllAdapter()
    }

    // 프래그먼트의 뷰를 생성하는 메서드
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSocialFollowingAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    // 뷰가 생성된 후 초기화 작업을 수행하는 메서드
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        viewModel.loadFollowingAllMembers()
    }

    // 프래그먼트가 파괴될 때 Binding 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): SocialFollowingAllFragment {
            return SocialFollowingAllFragment()
        }
    }

    // RecyclerView를 초기화하는 메서드
    private fun setupRecyclerView() {
        with(binding.rvSocialFollowingAll) {
            adapter = socialFollowingAllAdapter
        }
    }

    // ViewModel 데이터를 관찰하고 UI를 업데이트하는 메서드
    private fun observeViewModel() {
        // 팔로잉 전체 목록을 뷰모델에서 관찰
        viewModel.followingMembers.observe(viewLifecycleOwner, Observer { posts ->
            socialFollowingAllAdapter.submitList(posts)
        })
    }
}
