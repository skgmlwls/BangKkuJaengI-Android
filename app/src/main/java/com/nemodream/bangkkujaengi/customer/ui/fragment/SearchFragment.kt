package com.nemodream.bangkkujaengi.customer.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nemodream.bangkkujaengi.customer.data.model.SearchHistory
import com.nemodream.bangkkujaengi.customer.ui.adapter.OnItemClickListener
import com.nemodream.bangkkujaengi.customer.ui.adapter.SearchHistoryAdapter
import com.nemodream.bangkkujaengi.customer.ui.viewmodel.SearchViewModel
import com.nemodream.bangkkujaengi.databinding.FragmentSearchBinding
import com.nemodream.bangkkujaengi.utils.hideKeyboard
import com.nemodream.bangkkujaengi.utils.popBackStack
import com.nemodream.bangkkujaengi.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), OnItemClickListener {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var appContext: Context
    private val viewModel: SearchViewModel by viewModels()
    private val searchAdapter: SearchHistoryAdapter by lazy { SearchHistoryAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupListeners()
        observeViewModel()
    }

    private fun setupUI() {
        binding.rvSearchHistoryList.adapter = searchAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.searchHistory.observe(viewLifecycleOwner) { searchHistory ->
            when(searchHistory.isEmpty()) {
                true -> { // 최근 검색이 없을 때
                    binding.tvSearchHistoryLabel.visibility = View.GONE
                    binding.tvClearAll.visibility = View.GONE
                    binding.groupRecentSearches.visibility = View.GONE
                }
                false -> {
                    binding.groupRecentSearches.visibility = View.VISIBLE
                }
            }
            searchAdapter.submitList(searchHistory)
        }
    }

    private fun setupListeners() {
        with(binding) {
            btnArrowBack.setOnClickListener {
                popBackStack()
            }

            btnSearchTrack.setOnClickListener {
                performSearch()
                etSearchTrack.clearFocus()
                it.hideKeyboard()
            }

            tvClearAll.setOnClickListener {
                viewModel.clearAllSearches()
            }

            etSearchTrack.editText?.setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        performSearch()
                        view?.hideKeyboard()
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun performSearch() {
        val query = binding.etSearchTrack.editText?.text.toString().trim()
        if (query.isEmpty()) {
            appContext.showToast("검색어를 입력해주세요.")
            return
        }
        viewModel.addSearch(query)
    }

    override fun onItemClick(item: SearchHistory) {
        val query = item.query
        binding.etSearchTrack.editText?.setText(query)
        binding.etSearchTrack.editText?.setSelection(query.length)
        performSearch()
    }

    override fun onDeleteClick(search: SearchHistory) {
        viewModel.deleteSearch(search)
    }
}