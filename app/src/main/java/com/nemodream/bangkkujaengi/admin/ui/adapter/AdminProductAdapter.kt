package com.nemodream.bangkkujaengi.admin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.customer.data.model.Product
import com.nemodream.bangkkujaengi.databinding.ItemAdminProductListBinding
import com.nemodream.bangkkujaengi.utils.loadImage

class AdminProductAdapter: ListAdapter<Product, AdminProductAdapter.AdminProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminProductViewHolder {
        return AdminProductViewHolder(
            ItemAdminProductListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
        )
    }

    override fun onBindViewHolder(holder: AdminProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AdminProductViewHolder(
        private val binding: ItemAdminProductListBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            with(binding) {
                ivAdminProductImage.loadImage(product.images.first())
                tvAdminProductTitle.text = product.productName
                tvAdminProductCount.text = "재고 ${product.productCount}"
            }
        }
    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.productId == newItem.productId
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}