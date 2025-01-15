package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
            viewModel.selectMember(member)
        }
    }

    private val postAdapter: SocialDiscoveryAdapter by lazy {
        SocialDiscoveryAdapter(this)
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

    private fun setupRecyclerViews() {
        binding.rvFollowingProfiles.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = profileAdapter
        }

        binding.rvFollowingPosts.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = postAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.followingMembers.observe(viewLifecycleOwner) { members ->
            profileAdapter.updateList(members)
        }

        viewModel.selectedMember.observe(viewLifecycleOwner) { selectedMember ->
            selectedMember?.let {
                profileAdapter.setSelectedMemberId(it.id)
            }
        }

        viewModel.memberPosts.observe(viewLifecycleOwner) { posts ->
            postAdapter.submitList(posts)
        }
    }

    /**
     * 게시글 클릭 이벤트 처리
     */
    override fun onPostItemClick(post: Post) {
        // 클릭된 게시글 처리 (예: 상세 페이지로 이동)
    }
}
