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

class SearchResultAdapter(
    private val productClickListener: ProductClickListener,
) : ListAdapter<Product, SearchResultAdapter.SearchResultViewHolder>(
    SearchResultDiffCallBack()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return SearchResultViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ),
            onProductClickListener = { position -> productClickListener.onProductClick(getItem(position)) }
        )
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SearchResultViewHolder(
        private val binding: ItemProductBinding,
        private val onProductClickListener: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onProductClickListener(adapterPosition)
            }
        }

        fun bind(product: Product) {
            with(binding) {
                ivProduct.loadImage(product.images.first())
                tvProductName.text = product.productName
                tvProductPrice.text = "${product.price.toCommaString()} 원"
            }
        }
    }
}

class SearchResultDiffCallBack : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.productId == newItem.productId
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

}