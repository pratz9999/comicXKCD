package com.shortcut.components.dashboard.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.shortcut.callbacks.OnRecyclerItemClickCallback
import com.shortcut.components.dashboard.adapters.ComicsListAdapter
import com.shortcut.components.dashboard.base.BaseViewModelFragment
import com.shortcut.models.ComicView
import com.shortcut.xkcd.databinding.FragmentFavoriteBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel

class FavoriteFragment : BaseViewModelFragment<FavoriteViewModel, FragmentFavoriteBinding>(),
    OnRecyclerItemClickCallback<ComicView> {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFavoriteBinding
        get() = FragmentFavoriteBinding::inflate


    override fun getInjectViewModel(): FavoriteViewModel {
        return getViewModel()
    }

    override fun initViews() {
        setHasOptionsMenu(true)
    }

    override fun initObservers() {
        super.initObservers()

        viewModel.favoriteListMLiveData.observe(viewLifecycleOwner, {
            binding.tvFavMsg.isVisible = it.isEmpty()
            val adp = ComicsListAdapter(it, this)
            binding.rvFavorites.adapter = adp
        })
    }

    override fun onRetry() {
    }

    override fun onRecyclerItemClicked(position: Int, view: View, data: ComicView) {
        findNavController().navigate(
            FavoriteFragmentDirections.actionFavoriteToComicDetailsFragment(
                data.num
            )
        )
    }

}