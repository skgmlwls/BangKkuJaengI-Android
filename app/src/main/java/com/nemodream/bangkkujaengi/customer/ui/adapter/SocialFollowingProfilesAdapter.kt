package com.nemodream.bangkkujaengi.customer.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.customer.data.model.Member
import com.nemodream.bangkkujaengi.databinding.ItemFollowingProfileBinding
import com.nemodream.bangkkujaengi.utils.loadImage

class SocialFollowingProfilesAdapter(
    private val followingMembers: MutableList<Member>, // 팔로잉 멤버 목록 데이터
    private val onProfileClick: (Member) -> Unit // 클릭 이벤트 처리 콜백
) : RecyclerView.Adapter<SocialFollowingProfilesAdapter.FollowingProfileViewHolder>() {

    private var selectedMemberId: String? = null // 현재 선택된 멤버의 ID

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingProfileViewHolder {
        val binding = ItemFollowingProfileBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FollowingProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowingProfileViewHolder, position: Int) {
        val member = followingMembers[position]
        val isSelected = member.id == selectedMemberId // 현재 멤버가 선택된 상태인지 확인
        holder.bind(member, isSelected)

        // 프로필 클릭 이벤트 처리
        holder.itemView.setOnClickListener {
            onProfileClick(member)
        }
    }

    override fun getItemCount(): Int = followingMembers.size // 전체 멤버 수 반환

    // 리스트 데이터 업데이트
    fun updateList(newList: List<Member>) {
        followingMembers.clear()
        followingMembers.addAll(newList)
        notifyDataSetChanged() // 변경된 데이터 반영
    }

    // 선택된 멤버 ID 설정
    fun setSelectedMemberId(memberId: String) {
        selectedMemberId = memberId
        notifyDataSetChanged() // 변경된 선택 상태 반영
    }

    // ViewHolder: 프로필 아이템 뷰 관리
    inner class FollowingProfileViewHolder(
        private val binding: ItemFollowingProfileBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        // 멤버 데이터를 뷰에 바인딩
        fun bind(member: Member, isSelected: Boolean) {
            // 프로필 이미지 로드
            member.memberProfileImage?.let { binding.ivFollowingProfileImage.loadImage(it) }

            // 닉네임 설정 및 선택 상태에 따라 표시
            binding.tvFollowingProfileNickname.text = member.memberNickName
            binding.tvFollowingProfileNickname.visibility = if (isSelected) View.VISIBLE else View.INVISIBLE

            // 선택 상태에 따라 테두리 설정
            if (isSelected) {
                binding.ivFollowingProfileImage.strokeColor = itemView.context.getColorStateList(R.color.black)
                binding.ivFollowingProfileImage.strokeWidth = 7f // 원하는 두께
            } else {
                binding.ivFollowingProfileImage.strokeColor = null
                binding.ivFollowingProfileImage.strokeWidth = 0f
            }
        }
    }
}
