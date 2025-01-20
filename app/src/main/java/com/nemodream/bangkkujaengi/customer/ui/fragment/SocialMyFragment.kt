package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.nemodream.bangkkujaengi.customer.data.model.Post
import com.nemodream.bangkkujaengi.customer.ui.adapter.OnPostItemClickListener
import com.nemodream.bangkkujaengi.customer.ui.adapter.SocialDiscoveryAdapter
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.SocialMyViewModel
import com.nemodream.bangkkujaengi.databinding.FragmentSocialMyBinding
import com.nemodream.bangkkujaengi.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialMyFragment : Fragment(), OnPostItemClickListener {

    private var _binding: FragmentSocialMyBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SocialMyViewModel by viewModels()

    private val socialMyAdapter: SocialDiscoveryAdapter by lazy {
        SocialDiscoveryAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSocialMyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 초기설정
        updateTabStyle(isMyPostsSelected = true)
        viewModel.loadMyWrittenPosts()

        setupRecyclerView()
        observeViewModel()
        viewModel.loadMyProfile() // 프로필 데이터 로드
        setupTabClickListeners() // 탭 클릭 리스너 설정
        setupProfileEdit() // 편집 모드 전환
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setupProfileEdit() {
        val nicknameEditText = binding.etMyProfileNickname // EditText로 변경된 닉네임 필드
        val nicknameTextView = binding.tvMyProfileNickname // 원래 TextView
        val editIcon = binding.ivSocialMyEdit
        val saveButton = binding.btnSocialMySaveEdit

        // 초기 상태: 닉네임 편집 불가능
        nicknameEditText.visibility = View.GONE // EditText 숨기기
        nicknameTextView.visibility = View.VISIBLE // TextView 보이기

        // 수정 버튼 클릭
        editIcon.setOnClickListener {
            nicknameEditText.setText(nicknameTextView.text) // TextView의 텍스트를 EditText로 복사
            nicknameTextView.visibility = View.GONE // TextView 숨기기
            nicknameEditText.visibility = View.VISIBLE // EditText 보이기
            nicknameEditText.requestFocus()  // 포커스 이동
            editIcon.visibility = View.GONE  // 수정 아이콘 숨김
            saveButton.visibility = View.VISIBLE // "수정완료" 버튼 표시
        }

        // "수정완료" 버튼 클릭
        saveButton.setOnClickListener {
            val editedNickname = nicknameEditText.text.toString() // 수정된 닉네임
            viewModel.updateNickname(editedNickname) // ViewModel에 저장 로직 호출

            nicknameTextView.text = editedNickname // TextView에 수정된 텍스트 표시
            nicknameEditText.visibility = View.GONE // EditText 숨기기
            nicknameTextView.visibility = View.VISIBLE // TextView 보이기
            editIcon.visibility = View.VISIBLE // 수정 아이콘 다시 표시
            saveButton.visibility = View.GONE  // "수정완료" 버튼 숨김
        }
    }


    // "내가쓴글" 및 "저장됨" 탭 클릭 이벤트 설정
    private fun setupTabClickListeners() {
        binding.tvMyPosts.setOnClickListener {
            viewModel.loadMyWrittenPosts()
            updateTabStyle(isMyPostsSelected = true)
        }
        binding.tvSavedPosts.setOnClickListener {
            viewModel.loadMySavedPosts()
            updateTabStyle(isMyPostsSelected = false)
        }
    }

    // 탭 스타일 업데이트
    private fun updateTabStyle(isMyPostsSelected: Boolean) {
        // "내가쓴글" 선택시
        binding.tvMyPosts.setTypeface(null, if (isMyPostsSelected) Typeface.BOLD else Typeface.NORMAL)
        // "저장됨" 선택시
        binding.tvSavedPosts.setTypeface(null, if (isMyPostsSelected) Typeface.NORMAL else Typeface.BOLD)
    }


    // 리사이클러뷰 설정
    private fun setupRecyclerView() {
        with(binding.rvMyPosts) {
            layoutManager = GridLayoutManager(context, 2) // 한 행에 2개의 열
            adapter = socialMyAdapter
        }
    }

    /**
     * ViewModel에서 데이터를 관찰하고 UI를 업데이트
     */
    private fun observeViewModel() {
        // 프로필 데이터 관찰
        viewModel.myProfile.observe(viewLifecycleOwner, Observer { profile ->
            profile?.let {
                binding.ivMyProfileImage.loadImage(it.memberProfileImage.toString()) // 프로필 이미지
                binding.tvMyProfileNickname.text = Editable.Factory.getInstance().newEditable(it.memberNickName) // 닉네임
                binding.tvMyProfileFollowInfo.text =
                    "팔로잉 ${it.followingCount}명   팔로워 ${it.followerCount}명" // 팔로우 정보
            }
        })

        // 게시글 데이터 관찰
        viewModel.posts.observe(viewLifecycleOwner, Observer { posts ->
            socialMyAdapter.submitList(posts)
        })
    }

    /**
     * 게시글 클릭 이벤트 처리
     */
    override fun onPostItemClick(post: Post) {
        // 게시글 클릭 시 수행할 작업을 여기에 작성
        // 게시글 상세 페이지로 이동
    }
}
