package com.nemodream.bangkkujaengi.admin.ui.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.admin.ui.adapter.AdminProductImageAdapter
import com.nemodream.bangkkujaengi.admin.ui.adapter.OnImageCancelClickListener
import com.nemodream.bangkkujaengi.admin.ui.viewmodel.AdminAddProductViewModel
import com.nemodream.bangkkujaengi.customer.data.model.CategoryType
import com.nemodream.bangkkujaengi.databinding.FragmentAdminAddProductBinding
import com.nemodream.bangkkujaengi.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminAddProductFragment : Fragment(), OnImageCancelClickListener {
    private var _binding: FragmentAdminAddProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdminAddProductViewModel by viewModels()
    private val imageAdapter = AdminProductImageAdapter(this)

    private lateinit var appContext: Context

    /*
    * 사진 선택 권한 요청 콜백
    * */
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openPhotoPicker()
        }
    }

    /*
    * 사진 선택 콜백
    * */
    private val pickMultipleMedia = registerForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(MAX_IMAGE_COUNT)
    ) { uris ->
        if (uris.isNotEmpty()) {
            viewModel.addImages(uris)
        }
    }

    /*
    * context를 프로퍼티에 저장
    * */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupListeners()
        setupTextChangeListeners()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    * 선택된 이미지 취소 버튼 클릭
    * 클릭하면 선택된 이미지는 리스트에서 제거한다.
    * */
    override fun onCancelClick(uri: Uri) {
        viewModel.removeImage(uri)
        validateFields()
    }

    /*
    * UI 초기 설정
    * - RecyclerView 어댑터 설정
    * - 할인 판매가 입력 필드 비활성화
    * - 이미지 삭제 콜백 설정
    * */
    private fun setupUI() {
        with(binding) {
            rvAdminProductAddImage.adapter = imageAdapter
            tfAdminProductAddDiscountPrice.editText?.isEnabled = false

            // Dropdown 설정
            val categories = CategoryType.entries.map { it.getTabTitle() }
            val categoryAdapter = ArrayAdapter(
                requireContext(),
                R.layout.item_dropdown_category,
                categories
            )
            (tfAdminProductAddCategory.editText as? AutoCompleteTextView)?.apply {
                setAdapter(categoryAdapter)
                setOnItemClickListener { _, _, position, _ ->
                    val selectedCategory = CategoryType.entries[position]
                    viewModel.setCategory(selectedCategory)
                    validateFields()
                }
            }
        }
    }

    /*
    * 버튼 클릭 리스너 설정
    * - 이미지 추가 영역 클릭
    * - 툴바 내비게이션 클릭
    * - 상품 등록 버튼 클릭
    * */
    private fun setupListeners() {
        with(binding) {
            viewProductAddImageArea.setOnClickListener {
                checkAndRequestPermission()
            }

            toolbarAdminProductAdd.setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }

            btnProductAddSubmit.setOnClickListener {
                // TODO: Firebase Storage 업로드 및 API 호출
            }
        }
    }

    /*
    * 입력 필드 변경 리스너 설정
    * - 원가 변경 시 할인가 계산
    * - 할인율 변경 시 할인가 계산
    * - 모든 필드 변경 시 유효성 검증
    * */
    private fun setupTextChangeListeners() {
        with(binding) {
            tfAdminProductAddPrice.editText?.doAfterTextChanged { price ->
                viewModel.calculateDiscountPrice(
                    price.toString(),
                    tfAdminProductAddDiscountRate.editText?.text.toString()
                )
                validateFields()
            }

            tfAdminProductAddDiscountRate.editText?.doAfterTextChanged { rate ->
                viewModel.calculateDiscountPrice(
                    tfAdminProductAddPrice.editText?.text.toString(),
                    rate.toString()
                )
                validateFields()
            }

            tfAdminProductAddCount.editText?.doAfterTextChanged { validateFields() }
        }
    }

    /*
    * ViewModel 상태 변경 관찰
    * - 이미지 목록 변경 시 UI 업데이트
    * - 등록 버튼 활성화 상태 변경
    * - 할인 판매가 변경 시 UI 업데이트
    * */
    private fun observeViewModel() {
        viewModel.imageUris.observe(viewLifecycleOwner) { uris ->
            imageAdapter.submitList(uris)
            updateImageLabel(uris.size)
            validateFields()
        }

        viewModel.isSubmitEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.btnProductAddSubmit.isEnabled = isEnabled
        }

        viewModel.discountPrice.observe(viewLifecycleOwner) { price ->
            binding.tfAdminProductAddDiscountPrice.editText?.setText(price)
        }
    }

    /*
    * 입력 필드 유효성 검증
    * - 모든 필드의 값을 ViewModel에 전달하여 검증
    * */
    private fun validateFields() {
        with(binding) {
            viewModel.validateFields(
                tfAdminProductAddPrice.editText?.text.toString(),
                tfAdminProductAddDiscountRate.editText?.text.toString(),
                tfAdminProductAddCount.editText?.text.toString()
            )
        }
    }

    /*
    * 이미지 권한 확인 및 요청
    * - API 레벨에 따른 적절한 권한 확인
    * - 권한이 없는 경우 권한 요청
    * */
    private fun checkAndRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }
            ) == PackageManager.PERMISSION_GRANTED -> {
                openPhotoPicker()
            }

            else -> {
                requestPermissionLauncher.launch(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        Manifest.permission.READ_MEDIA_IMAGES
                    } else {
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    }
                )
            }
        }
    }

    /*
    * 이미지 선택기 실행
    * - 이미지 개수 제한 확인
    * - 시스템 이미지 선택기 실행
    * */
    private fun openPhotoPicker() {
        if ((viewModel.imageUris.value?.size ?: 0) >= MAX_IMAGE_COUNT) {
            requireContext().showToast(IMAGE_LIMIT_MESSAGE)
            return
        }
        pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    /*
    * 이미지 라벨 업데이트
    * - 현재 선택된 이미지 개수 표시
    * - 최대 선택 가능 개수 표시
    * */
    private fun updateImageLabel(size: Int) {
        binding.tvProductAddImageLabel.text =
            String.format(IMAGE_LABEL_FORMAT, size, MAX_IMAGE_COUNT)
    }

    companion object {
        private const val MAX_IMAGE_COUNT = 5
        private const val IMAGE_LABEL_FORMAT = "이미지 (%d / %d)"
        private const val IMAGE_LIMIT_MESSAGE = "이미지는 최대 ${MAX_IMAGE_COUNT}개까지만 선택 가능합니다"
    }
}