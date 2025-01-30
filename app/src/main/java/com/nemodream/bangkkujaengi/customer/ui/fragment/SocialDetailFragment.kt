package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.databinding.FragmentSocialDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialDetailFragment : Fragment() {

    private var _binding: FragmentSocialDetailBinding? = null
    private val binding get() = _binding!!

    // private val viewModel: SocialFollowingAllViewModel by viewModels()

    // 팔로잉 프로필 RecyclerView의 어댑터
    // private val socialFollowingAllAdapter = SocialFollowingAllAdapter()


    // 프래그먼트의 뷰를 생성하는 메서드
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSocialDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    // 뷰가 생성된 후 초기화 작업을 수행하는 메서드
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    // 프래그먼트가 파괴될 때 Binding 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 리스너 설정
    private fun setupListeners() {
        with(binding) {
            toolbarSocial.setNavigationOnClickListener {
                findNavController().popBackStack(R.id.navigation_social, false)
            }
        }
    }
}

