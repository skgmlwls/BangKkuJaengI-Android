package com.nemodream.bangkkujaengi.admin.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nemodream.bangkkujaengi.admin.ui.adapter.AdminProductAdapter
import com.nemodream.bangkkujaengi.admin.ui.viewmodel.AdminProductViewModel
import com.nemodream.bangkkujaengi.databinding.FragmentAdminProductBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminProductFragment : Fragment() {
    private var _binding: FragmentAdminProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdminProductViewModel by viewModels()
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
        observeViewModel()
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
            toolbarProductProduct.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            btnProductAdd.setOnClickListener {
                val action = AdminProductFragmentDirections.actionAdminProductFragmentToAdminAddProductFragment()
                findNavController().navigate(action)
            }
        }

    }

    private fun observeViewModel() {
        viewModel.products.observe(viewLifecycleOwner) { products ->
            productAdapter.submitList(products)
        }
    }

}