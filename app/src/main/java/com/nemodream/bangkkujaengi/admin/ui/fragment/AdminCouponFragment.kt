package com.nemodream.bangkkujaengi.admin.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nemodream.bangkkujaengi.admin.ui.adapter.AdminCouponAdapter
import com.nemodream.bangkkujaengi.admin.ui.adapter.OnCouponDeleteListener
import com.nemodream.bangkkujaengi.admin.ui.viewmodel.AdminCouponViewModel
import com.nemodream.bangkkujaengi.customer.data.model.Coupon
import com.nemodream.bangkkujaengi.customer.ui.custom.CustomDialog
import com.nemodream.bangkkujaengi.admin.ui.viewmodel.AdminCouponViewModel
import com.nemodream.bangkkujaengi.databinding.FragmentAdminCouponBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminCouponFragment: Fragment(), OnCouponDeleteListener {
    private var _binding: FragmentAdminCouponBinding? = null
    private val binding get() = _binding!!

    private val couponAdapter: AdminCouponAdapter by lazy { AdminCouponAdapter(this) }
    private val viewModel: AdminCouponViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminCouponBinding.inflate(inflater, container, false)
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

    override fun onCouponDelete(coupon: Coupon) {
        CustomDialog(
            context = requireContext(),
            message = "정말 해당 쿠폰을 삭제하시겠습니까?",
            confirmText = "쿠폰 삭제",
            onConfirm = {
                viewModel.deleteCoupon(coupon)
            },
            onCancel = {}
        ).show()
    }

    private fun setupUI() {
        viewModel.loadCoupon()
        with(binding) {
            rvAdminCouponList.adapter = couponAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.couponList.observe(viewLifecycleOwner) {
            couponAdapter.submitList(it)
        }
    }

    private fun setupListeners() {
        with(binding) {
            toolbarAdminCoupon.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            btnCouponAdd.setOnClickListener {
                val action = AdminCouponFragmentDirections.actionAdminCouponFragmentToAdminAddCouponFragment()
                findNavController().navigate(action)
            }
        }
    }

}