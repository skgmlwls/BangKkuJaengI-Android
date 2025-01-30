package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.nemodream.bangkkujaengi.customer.data.model.Post
import com.nemodream.bangkkujaengi.customer.ui.adapter.OnPostItemClickListener
import com.nemodream.bangkkujaengi.customer.ui.adapter.SocialDiscoveryAdapter
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.SocialDiscoveryViewModel
import com.nemodream.bangkkujaengi.databinding.FragmentSocialDiscoveryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialDiscoveryFragment : Fragment(), OnPostItemClickListener {

    private var _binding: FragmentSocialDiscoveryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SocialDiscoveryViewModel by viewModels()

    private val socialDiscoveryAdapter: SocialDiscoveryAdapter by lazy {
        SocialDiscoveryAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSocialDiscoveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        viewModel.loadPosts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * RecyclerView 설정
     */
    private fun setupRecyclerView() {
        with(binding.rvSocialDiscovery) {
            layoutManager = GridLayoutManager(context, 2) // 한 행에 2개의 열
            adapter = socialDiscoveryAdapter
        }
    }

    /**
     * ViewModel에서 데이터를 관찰하고 UI를 업데이트
     */
    private fun observeViewModel() {
        // 게시글 목록을 뷰모델에서 관찰
        viewModel.posts.observe(viewLifecycleOwner, Observer { posts ->
            socialDiscoveryAdapter.submitList(posts)
        })
    }

    /**
     * 게시글 클릭 이벤트 처리
     */
    override fun onPostItemClick(post: Post) {
        Log.d("SocialDiscoveryFragment", "Post clicked: ${post.title}")
        val action = SocialFragmentDirections.actionSocialFragmentToSocialDetailFragment()
        findNavController().navigate(action)
    }
}
