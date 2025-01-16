package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nemodream.bangkkujaengi.customer.ui.adapter.SocialFollowingAllAdapter
import com.nemodream.bangkkujaengi.databinding.FragmentSocialFollowingAllBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialFollowingAllFragment : Fragment() {

    private var _binding: FragmentSocialFollowingAllBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SocialFollowingAllViewModel by viewModels()

    // 팔로잉 프로필 RecyclerView의 어댑터
    private val profileAdapter: SocialFollowingAllAdapter by lazy {
        SocialFollowingAllAdapter(this)
    }

    // 프래그먼트의 뷰를 생성하는 메서드
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSocialFollowingAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    // 뷰가 생성된 후 초기화 작업을 수행하는 메서드
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews() // RecyclerView 초기화
        observeViewModel() // ViewModel 데이터 관찰 설정
        viewModel.loadFollowingMembers() // 팔로잉 멤버 데이터를 로드
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
    private fun setupRecyclerViews() {
        // 팔로잉 프로필 RecyclerView 초기화
        binding.rvSocialFollowingAll.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) // 수직 스크롤 설정
            adapter = profileAdapter
        }
    }

    // ViewModel 데이터를 관찰하고 UI를 업데이트하는 메서드
    private fun observeViewModel() {
        // 팔로잉 멤버 리스트 관찰
        viewModel.followingMembers.observe(viewLifecycleOwner) { members ->
            profileAdapter.updateList(members) // 멤버 리스트를 어댑터에 업데이트
        }
    }
}
