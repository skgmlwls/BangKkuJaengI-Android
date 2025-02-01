package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.customer.data.model.Post
import com.nemodream.bangkkujaengi.customer.ui.adapter.SocialCarouselAdapter
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.SocialDiscoveryViewModel
import com.nemodream.bangkkujaengi.databinding.FragmentSocialDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class SocialDetailFragment : Fragment() {

    private var _binding: FragmentSocialDetailBinding? = null
    private val binding get() = _binding!!
    private var isFollowing = false

    // Fragment간 통신방법 : 뷰모델 공유
    private val viewModel: SocialDiscoveryViewModel by activityViewModels()

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
        Log.d("test909","소셜 디테일 프래그먼트 실행 완료")
        viewModel.selectedPost.observe(viewLifecycleOwner) { post ->
            setUpTextUI(post)
            setUpImageUI(post)
        }

//      Fragment간 통신방법 : Safe Args
//      SocialDetailFragment에서 데이터 받기
//        val post = arguments?.let { SocialDetailFragmentArgs.fromBundle(it).post }
//        post?.let { setUpUI(it) }
//        Log.d("test909", "소셜 디테일 프래그먼트 : ${post}")

//        Fragment Manager 통신 방법 : Fragment Manager 사용
//        setFragmentResult는 같은 FragmentManager 내에서만 동작하므로 Navigation으로 이동 시 사용 불가능
//        setFragmentResultListener("clickedPost") { clickedPost, bundle ->
//            val clickedId = bundle.getString("id")
//            val clickedNickname = bundle.getString("nickname")
//            val clickedAuthorProfilePicture = bundle.getString("authorProfilePicture")
//            val clickedTitle = bundle.getString("title")
//            val clickedContent = bundle.getString("content")
//            val clickedImageList = bundle.getStringArrayList("imageList")
//            val clickedSavedCount = bundle.getInt("savedCount")
//            val clickedCommentCount = bundle.getInt("commentCount")
//
//            // Post 객체 생성
//            val post = Post(
//                id = clickedId.orEmpty(),
//                nickname = clickedNickname.orEmpty(),
//                authorProfilePicture = clickedAuthorProfilePicture.orEmpty(),
//                title = clickedTitle.orEmpty(),
//                content = clickedContent.orEmpty(),
//                imageList = clickedImageList?.toList() ?: emptyList(),
//                productTagPinList = emptyList(),
//                savedCount = clickedSavedCount,
//                commentCount = clickedCommentCount
//            )
//
//            Log.d("test909", "소셜 디테일 프래그먼트 : ${post}")
//            setUpUI(post)
//        }
    }

    // 프래그먼트가 파괴될 때 Binding 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpTextUI(post: Post){
        with(binding){
            tvSocialDetailContent.text = post.content
            tvSocialDetailLikeCount.text = post.savedCount.toString()
            tvSocialDetailCommentCount.text = post.commentCount.toString()
            tvSocialDetailPostItemTitle.text = post.title
            tvSocialDetailContent.text = post.content
            tvSocialDetailPostItemNickname.text = post.nickname
        }
    }

    private fun setUpImageUI(post: Post){
        with(binding){

            val photos = post.imageList.map { imageUrl ->
                Uri.parse(imageUrl) // String -> Uri 변환
            }
            val adapter = SocialCarouselAdapter(photos) { position, x, y ->
                // 이미지 클릭 시 처리 로직 추가
                Log.d("SocialDetail", "Image clicked at position $position: ($x, $y)")
            }

            // ViewPager2에 어댑터 설정
            vpSocialDetailPictureCarousel.adapter = adapter

        }
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
            btnSocialDetailFollowingFollowing.setOnClickListener {
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
            binding.btnSocialDetailFollowingFollowing.text = "팔로잉"
            binding.btnSocialDetailFollowingFollowing.setTextColor(Color.parseColor("#58443B")) // 글자색
            binding.btnSocialDetailFollowingFollowing.setBackgroundTintList(
                ColorStateList.valueOf(Color.parseColor("#DFDFDF")) // 버튼 배경색
            )
            binding.btnSocialDetailFollowingFollowing.strokeColor =
                ColorStateList.valueOf(Color.parseColor("#818080")) // 테두리 색상
            binding.btnSocialDetailFollowingFollowing.strokeWidth = 1 // 테두리 두께 (px 단위)
            binding.btnSocialDetailFollowingFollowing.setPadding(0,0,0,0)
        } else {
            binding.btnSocialDetailFollowingFollowing.text = "팔로우"
            binding.btnSocialDetailFollowingFollowing.setTextColor(Color.parseColor("#FFFFFF")) // 글자색
            binding.btnSocialDetailFollowingFollowing.setBackgroundTintList(
                ColorStateList.valueOf(Color.parseColor("#332828")) // 버튼 배경색
            )
            binding.btnSocialDetailFollowingFollowing.strokeColor =
                ColorStateList.valueOf(Color.parseColor("#000000")) // 테두리 색상 (기본값, 변경 가능)
            binding.btnSocialDetailFollowingFollowing.strokeWidth = 1 // 테두리 두께 (px 단위)
            binding.btnSocialDetailFollowingFollowing.setPadding(0,0,0,0)
        }
    }
}

