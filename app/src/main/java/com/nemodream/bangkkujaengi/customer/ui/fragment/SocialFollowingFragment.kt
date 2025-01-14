package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.customer.data.model.Post
import com.nemodream.bangkkujaengi.customer.ui.adapter.OnPostItemClickListener
import com.nemodream.bangkkujaengi.databinding.FragmentSocialFollowingBinding
import kotlin.getValue

class SocialFollowingFragment : Fragment(), OnPostItemClickListener {
    private var _binding: FragmentSocialFollowingBinding? = null
    private val binding get() = _binding!!

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
     * 게시글 클릭 이벤트 처리
     */
    override fun onPostItemClick(post: Post) {
        // 게시글 클릭 시 수행할 작업을 여기에 작성
        // 게시글 상세 페이지로 이동
    }
}