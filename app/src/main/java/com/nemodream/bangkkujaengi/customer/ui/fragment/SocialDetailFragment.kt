package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.customer.data.model.Member
import com.nemodream.bangkkujaengi.databinding.FragmentSocialDetailBinding
import com.nemodream.bangkkujaengi.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialDetailFragment : Fragment() {

    private var _binding: FragmentSocialDetailBinding? = null
    private val binding get() = _binding!!
    private var isFollowing = false

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
            // 초기 상태 설정 (isFollowing 여부에 따라)

            updateButtonState(isFollowing)

            // 버튼 클릭 리스너
            btnFollowingFollowing.setOnClickListener {
                isFollowing = !isFollowing
                updateButtonState(isFollowing)

//                if (isFollowing) {
//                    // 팔로우 목록에 멤버 추가 (나중에 구현 예정)
//                } else {
//                    // 팔로우 목록에서 멤버 제거 (나중에 구현 예정)
//                }
            }



        }
    }
    private fun updateButtonState(isFollowing: Boolean) {
        if (isFollowing) {
            binding.btnFollowingFollowing.text = "팔로잉"
            binding.btnFollowingFollowing.setTextColor(Color.parseColor("#58443B")) // 글자색
            binding.btnFollowingFollowing.setBackgroundTintList(
                ColorStateList.valueOf(Color.parseColor("#DFDFDF")) // 버튼 배경색
            )
            binding.btnFollowingFollowing.strokeColor =
                ColorStateList.valueOf(Color.parseColor("#818080")) // 테두리 색상
            binding.btnFollowingFollowing.strokeWidth = 1 // 테두리 두께 (px 단위)
            binding.btnFollowingFollowing.setPadding(0,0,0,0)
        } else {
            binding.btnFollowingFollowing.text = "팔로우"
            binding.btnFollowingFollowing.setTextColor(Color.parseColor("#FFFFFF")) // 글자색
            binding.btnFollowingFollowing.setBackgroundTintList(
                ColorStateList.valueOf(Color.parseColor("#332828")) // 버튼 배경색
            )
            binding.btnFollowingFollowing.strokeColor =
                ColorStateList.valueOf(Color.parseColor("#000000")) // 테두리 색상 (기본값, 변경 가능)
            binding.btnFollowingFollowing.strokeWidth = 1 // 테두리 두께 (px 단위)
            binding.btnFollowingFollowing.setPadding(0,0,0,0)
        }
    }
}

