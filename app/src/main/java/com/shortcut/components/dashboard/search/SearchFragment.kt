package com.shortcut.components.dashboard.search

import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.shortcut.callbacks.OnRecyclerItemClickCallback
import com.shortcut.components.dashboard.adapters.ComicsListAdapter
import com.shortcut.components.dashboard.base.BaseViewModelFragment
import com.shortcut.models.ComicView
import com.shortcut.utils.extensions.hideKeyboard
import com.shortcut.xkcd.R
import com.shortcut.xkcd.databinding.FragmentSearchBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel

class SearchFragment : BaseViewModelFragment<SearchViewModel, FragmentSearchBinding>(),
    OnRecyclerItemClickCallback<ComicView> {

    private var searchQuery: String? = null
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchBinding
        get() = FragmentSearchBinding::inflate


    override fun getInjectViewModel(): SearchViewModel {
        return getViewModel()
    }

    override fun initObservers() {
        super.initObservers()

        viewModel.searchState.observe(viewLifecycleOwner, {
            if (it == null) return@observe
            when (it) {
                is SearchViewState.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = it.isLoading
                }
                is SearchViewState.Error -> {
                    hideLoading()
                    showError(it.msg)
                }
                SearchViewState.InvalidSearchData -> {
                    hideLoading()
                    showError(R.string.search_fragment_invalid_search_data)
                }
                is SearchViewState.SuccessByNum -> {
                    hideLoading()
                    val list = mutableListOf(it.comicView)
                    initRecyclerView(list)
                }
                is SearchViewState.SuccessByText -> {
                    hideLoading()
                    it.comicViewList?.let { list ->
                        initRecyclerView(list)
                    }
                }
                SearchViewState.NoSearchResult -> {
                    hideLoading()
                    showError(R.string.search_fragment_no_search_result)
                    binding.rvSearchResult.adapter = ComicsListAdapter(mutableListOf(), this)
                }
            }
        })
    }

    private fun initRecyclerView(list: MutableList<ComicView>) {
        val adp = ComicsListAdapter(list, this)
        binding.rvSearchResult.adapter = adp
    }

    override fun initViews() {
        binding.tvSearchMsg.isVisible = true
        setHasOptionsMenu(true)
        setupSearchBar()
        binding.swipeRefreshLayout.isEnabled = false
    }

    override fun showLoading() {
        binding.swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun setupSearchBar() {

        binding.svUsers.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideKeyboard()
                binding.tvSearchMsg.isVisible = false
                searchQuery = query
                viewModel.searchForComics(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchQuery = newText
                return false
            }
        })


        binding.svUsers.isSubmitButtonEnabled = true
    }

    override fun onRetry() {
        viewModel.searchForComics(searchQuery)
    }

    override fun onRecyclerItemClicked(position: Int, view: View, data: ComicView) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchToComicDetailsFragment(
                data.num
            )
        )
    }


}