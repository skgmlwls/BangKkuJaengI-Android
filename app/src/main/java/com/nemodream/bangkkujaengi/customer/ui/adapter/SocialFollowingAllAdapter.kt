package com.nemodream.bangkkujaengi.customer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.customer.data.model.Member
import com.nemodream.bangkkujaengi.customer.ui.fragment.SocialFollowingAllFragment
import com.nemodream.bangkkujaengi.databinding.RowSocialFollowingAllBinding
import com.nemodream.bangkkujaengi.utils.loadImage

class SocialFollowingAllAdapter(
    private val followingAllMembers: SocialFollowingAllFragment, // 팔로잉 멤버 목록 데이터
) : RecyclerView.Adapter<SocialFollowingAllAdapter.FollowingAllViewHolder>() {

    private var selectedMemberId: String? = null // 현재 선택된 멤버의 ID

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingAllViewHolder {
        val binding = RowSocialFollowingAllBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FollowingAllViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowingAllViewHolder, position: Int) {
        val member = followingAllMembers[position]
        holder.bind(member)
    }

    override fun getItemCount(): Int = followingAllMembers.size // 전체 멤버 수 반환

    // 리스트 데이터 업데이트
    fun updateList(newList: List<Member>) {
        followingAllMembers.clear()
        followingAllMembers.addAll(newList)
        notifyDataSetChanged() // 변경된 데이터 반영
    }

    // ViewHolder: 프로필 아이템 뷰 관리
    inner class FollowingAllViewHolder(private val binding: RowSocialFollowingAllBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        // 멤버 데이터를 뷰에 바인딩
        fun bind(member: Member) {
            // 프로필 이미지 설정
            member.memberProfileImage?.let { binding.ivProfileImage.loadImage(it) }

            // 닉네임 설정
            binding.tvProfileNickname.text = member.memberNickName

        }
    }
}
