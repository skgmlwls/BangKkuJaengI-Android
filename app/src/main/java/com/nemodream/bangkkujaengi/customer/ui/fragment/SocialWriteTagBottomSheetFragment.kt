package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nemodream.bangkkujaengi.databinding.FragmentSocialWriteTagBottomSheetBinding

class SocialWriteTagBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSocialWriteTagBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var photoPosition: Int = -1
    private var xCoord: Float = 0f
    private var yCoord: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            photoPosition = it.getInt("photoPosition", -1)
            xCoord = it.getFloat("xCoord", 0f)
            yCoord = it.getFloat("yCoord", 0f)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSocialWriteTagBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.tvPhotoPosition.text = "Photo Position: $photoPosition"
        binding.tvCoordinates.text = "Coordinates: ($xCoord, $yCoord)"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
