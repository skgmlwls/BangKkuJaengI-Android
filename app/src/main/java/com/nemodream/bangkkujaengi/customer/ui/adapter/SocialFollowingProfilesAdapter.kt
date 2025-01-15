package com.nemodream.bangkkujaengi.customer.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.customer.data.model.Member
import com.nemodream.bangkkujaengi.databinding.ItemFollowingProfileBinding
import com.nemodream.bangkkujaengi.utils.loadImage

class SocialFollowingProfilesAdapter(
    private val followingMembers: MutableList<Member>,
    private val onProfileClick: (Member) -> Unit
) : RecyclerView.Adapter<SocialFollowingProfilesAdapter.FollowingProfileViewHolder>() {

    private var selectedMemberId: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingProfileViewHolder {
        val binding = ItemFollowingProfileBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FollowingProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowingProfileViewHolder, position: Int) {
        holder.bind(followingMembers[position], followingMembers[position].id == selectedMemberId)
    }

    override fun getItemCount(): Int = followingMembers.size

    /**
     * 프로필 목록 업데이트
     */
    fun updateList(newList: List<Member>) {
        followingMembers.clear()
        followingMembers.addAll(newList)
        notifyDataSetChanged()
    }

    fun setSelectedMember(memberId: String) {
        selectedMemberId = memberId
        notifyDataSetChanged()
    }

    inner class FollowingProfileViewHolder(
        private val binding: ItemFollowingProfileBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(member: Member, isSelected: Boolean) {
            // 프로필 이미지 로드
            member.memberProfileImage?.let { binding.ivFollowingProfileImage.loadImage(it) }

            // 닉네임 설정
            binding.tvFollowingProfileNickname.text = member.memberNickName


            // 프로필 클릭 이벤트 처리
            binding.root.setOnClickListener {
                onProfileClick(member)
            }

            // 프로필 선택 상태에 따른 스타일
            binding.root.isSelected = isSelected
        }
    }
}
