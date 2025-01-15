package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import com.nemodream.bangkkujaengi.customer.data.model.Member
import com.nemodream.bangkkujaengi.customer.data.model.Post
import com.nemodream.bangkkujaengi.customer.ui.adapter.SocialFollowingProfilesAdapter
import com.nemodream.bangkkujaengi.customer.ui.adapter.SocialDiscoveryAdapter
import com.nemodream.bangkkujaengi.customer.ui.adapter.OnPostItemClickListener
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.SocialFollowingViewModel
import com.nemodream.bangkkujaengi.databinding.FragmentSocialFollowingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialFollowingFragment : Fragment(), OnPostItemClickListener {

    private var _binding: FragmentSocialFollowingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SocialFollowingViewModel by viewModels()

    private val profileAdapter: SocialFollowingProfilesAdapter by lazy {
        SocialFollowingProfilesAdapter(mutableListOf()) { member ->
            // 특정 프로필 클릭 이벤트 처리
            viewModel.loadMemberPosts(member)
        }
    }

    private val postAdapter: SocialDiscoveryAdapter by lazy {
        SocialDiscoveryAdapter(this) // OnPostItemClickListener를 구현한 현재 객체 전달
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSocialFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        observeViewModel()
        viewModel.loadFollowingMembers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 뷰페이저 설정을 위해 newInstance() 메서드 추가
    companion object {
        fun newInstance(): SocialFollowingFragment {
            return SocialFollowingFragment()
        }
    }

    /**
     * RecyclerView 설정
     */
    private fun setupRecyclerViews() {
        // 팔로잉 프로필 RecyclerView 설정
        binding.rvFollowingProfiles.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = profileAdapter
        }

        // 팔로잉 게시글 RecyclerView 설정
        binding.rvFollowingPosts.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = postAdapter
        }
    }

    /**
     * ViewModel 데이터 관찰
     */
    private fun observeViewModel() {
        // 팔로잉 프로필 데이터 관찰
        viewModel.followingMembers.observe(viewLifecycleOwner) { members ->
            profileAdapter.updateList(members) // 어댑터 업데이트
        }

        viewModel.selectedMember.observe(viewLifecycleOwner) { selectedMember ->
            selectedMember?.let {
                profileAdapter.setSelectedMember(it.id)
            }
        }

        // 선택한 프로필의 게시글 데이터 관찰
        viewModel.memberPosts.observe(viewLifecycleOwner) { posts ->
            postAdapter.submitList(posts) // 게시글 리스트 설정
        }
    }

    /**
     * 게시글 클릭 이벤트 처리
     */
    override fun onPostItemClick(post: Post) {
        // 클릭된 게시글 처리 (예: 상세 페이지로 이동)
        // 예: post.id를 사용하여 상세 페이지로 이동
    }
}
