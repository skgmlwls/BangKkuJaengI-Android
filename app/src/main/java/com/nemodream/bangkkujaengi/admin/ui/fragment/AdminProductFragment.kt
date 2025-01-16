package com.nemodream.bangkkujaengi.admin.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.admin.ui.adapter.AdminProductAdapter
import com.nemodream.bangkkujaengi.container.AdminFragment
import com.nemodream.bangkkujaengi.databinding.FragmentAdminProductBinding

class AdminProductFragment : Fragment() {
    private var _binding: FragmentAdminProductBinding? = null
    private val binding get() = _binding!!

    private val productAdapter by lazy { AdminProductAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {
        with(binding) {
            rvAdminProductList.adapter = productAdapter
        }
    }

    private fun setupListeners() {
        with(binding) {
            toolbarAdminProduct.setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }

            btnProductAdd.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .add(R.id.admin_container, AdminAddProductFragment())
                    .addToBackStack("AdminProductFragment")
                    .commit()
            }
        }

    }

}