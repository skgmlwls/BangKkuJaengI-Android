package com.nemodream.bangkkujaengi.admin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.databinding.ItemAdminProductAddColorBinding

class AdminProductColorAdapter :
    ListAdapter<String, AdminProductColorAdapter.AdminProductColorViewHolder>(
        ProductColorDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminProductColorViewHolder {
        return AdminProductColorViewHolder(
            ItemAdminProductAddColorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
        )
    }

    override fun onBindViewHolder(holder: AdminProductColorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AdminProductColorViewHolder(
        private val binding: ItemAdminProductAddColorBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(color: String) {
            with(binding) {
                tvAdminProductColor.text = color
            }
        }
    }
}

class ProductColorDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}