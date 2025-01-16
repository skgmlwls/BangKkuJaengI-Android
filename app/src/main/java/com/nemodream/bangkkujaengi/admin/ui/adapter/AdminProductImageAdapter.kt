package com.nemodream.bangkkujaengi.admin.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.databinding.ItemAdminProductAddImageListBinding

class AdminProductImageAdapter(
    private val listener: OnImageCancelClickListener,
) : ListAdapter<Uri, AdminProductImageAdapter.AdminProductImageViewHolder>(AddProductImageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminProductImageViewHolder {
        return AdminProductImageViewHolder(
            ItemAdminProductAddImageListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onCancelClick = { position -> listener.onCancelClick(getItem(position)) }
        )
    }

    override fun onBindViewHolder(holder: AdminProductImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AdminProductImageViewHolder(
        private val binding: ItemAdminProductAddImageListBinding,
        private val onCancelClick: (position: Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnProductDetailImageCancel.setOnClickListener {
                onCancelClick(adapterPosition)
            }
        }

        fun bind(uri: Uri) {
            binding.ivProductDetailContentImage.setImageURI(uri)
        }
    }
}

class AddProductImageDiffCallback : DiffUtil.ItemCallback<Uri>() {
    override fun areItemsTheSame(oldItem: Uri, newItem: Uri) = oldItem == newItem
    override fun areContentsTheSame(oldItem: Uri, newItem: Uri) = oldItem == newItem
}

interface OnImageCancelClickListener {
    fun onCancelClick(uri: Uri)
}