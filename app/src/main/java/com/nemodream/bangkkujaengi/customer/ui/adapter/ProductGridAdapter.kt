package com.nemodream.bangkkujaengi.customer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nemodream.bangkkujaengi.customer.data.model.Product
import com.nemodream.bangkkujaengi.databinding.ItemProductBinding
import com.nemodream.bangkkujaengi.utils.loadImage
import com.nemodream.bangkkujaengi.utils.toCommaString

class ProductGridAdapter(
    private val productClickListener: ProductClickListener,
) : ListAdapter<Product, ProductGridAdapter.ProductGridViewHolder>(ProductGridDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductGridViewHolder {
        return ProductGridViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            productClickListener = { position -> productClickListener.onProductClick(getItem(position)) }
        )
    }

    override fun onBindViewHolder(holder: ProductGridViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductGridViewHolder(
        private val binding: ItemProductBinding,
        productClickListener: (position: Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                productClickListener(adapterPosition)
            }
        }

        fun bind(product: Product) {
            with(binding) {
                ivProduct.loadImage(product.images.first())
                tvProductName.text = product.productName
                tvProductPrice.text = "${product.price.toCommaString()}원"
            }
        }
    }
}

class ProductGridDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.productId == newItem.productId
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}