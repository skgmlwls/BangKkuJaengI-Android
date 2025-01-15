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
        val member = followingMembers[position]
        val isSelected = member.id == selectedMemberId
        holder.bind(member, isSelected)

        holder.itemView.setOnClickListener {
            onProfileClick(member) // 클릭 이벤트 처리
        }
    }

    override fun getItemCount(): Int = followingMembers.size

    fun updateList(newList: List<Member>) {
        followingMembers.clear()
        followingMembers.addAll(newList)
        notifyDataSetChanged()
    }

    fun setSelectedMemberId(memberId: String) {
        selectedMemberId = memberId
        notifyDataSetChanged()
    }

    inner class FollowingProfileViewHolder(
        private val binding: ItemFollowingProfileBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(member: Member, isSelected: Boolean) {
            // 프로필 이미지 로드
            member.memberProfileImage?.let { binding.ivFollowingProfileImage.loadImage(it) }

            // 닉네임 설정 및 표시 여부
            binding.tvFollowingProfileNickname.text = member.memberNickName
            binding.tvFollowingProfileNickname.visibility = if (isSelected) View.VISIBLE else View.INVISIBLE

            // 선택 상태에 따른 스타일 적용
            if (isSelected) {
                binding.ivFollowingProfileImage.setBackgroundResource(R.drawable.background_selected_profile)
            } else {
                binding.ivFollowingProfileImage.setBackgroundResource(android.R.color.transparent)
            }
        }
    }
}
