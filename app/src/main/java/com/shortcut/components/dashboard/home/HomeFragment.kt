package com.shortcut.components.dashboard.home

import android.util.Log
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.shortcut.data.repository.Resource
import com.shortcut.xkcd.R
import com.shortcut.xkcd.databinding.FragmentHomeBinding
import com.shortcut.models.ComicView
import com.shortcut.components.dashboard.listener.ComicItemListener
import com.shortcut.components.dashboard.adapters.ComicsPagerAdapter
import com.shortcut.components.dashboard.base.BaseViewModelFragment
import org.koin.androidx.viewmodel.ext.android.getViewModel

class HomeFragment : BaseViewModelFragment<HomeViewModel, FragmentHomeBinding>(),
    ComicItemListener {

    companion object {
        const val TAG = "Home"
    }

    private var totalComics: Int = 0
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate


    override fun getInjectViewModel(): HomeViewModel {
        return getViewModel()
    }

    private val comicsAdapter by lazy {
        ComicsPagerAdapter(mutableListOf(ComicView(-1, isLoading = true)))
    }

    override fun initViews() {
        setHasOptionsMenu(false)
        comicsAdapter.comicItemListener = this
        setupViewPager()

        binding.ivSwipeBack.setOnClickListener {
            if (binding.pager.currentItem > 0) {
                binding.pager.setCurrentItem(binding.pager.currentItem - 1, true)
            }
        }

        binding.ivSwipeNext.setOnClickListener {
            if (binding.pager.currentItem < totalComics - 1) {
                binding.pager.setCurrentItem(binding.pager.currentItem + 1, true)
            }
        }

        viewModel.getCurrentComic()
    }

    private fun setupViewPager() {
        with(binding.pager) {
            adapter = comicsAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    comicsAdapter.getItemPosition(position)?.num?.let { comicNum ->
                        onComicLoaded(comicNum)

                        //If last comic then hide next button
                        binding.ivSwipeNext.visibility = if (viewModel.isLastComic(comicNum)) {
                            View.INVISIBLE
                        } else {
                            View.VISIBLE
                        }

                        //If first comic then hide Back button
                        binding.ivSwipeBack.visibility = if (viewModel.isFirstComic(comicNum)) {
                            View.INVISIBLE
                        } else {
                            View.VISIBLE
                        }

                        //if the current is last then load the next comic
                        if (position == comicsAdapter.itemCount - 1) {
                            viewModel.getNextComic()
                        }
                    }
                }
            })
        }
    }

    override fun initObservers() {
        super.initObservers()

        viewModel.currentComicState.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.LOADING -> Log.i(TAG, "Loading: ${it.isLoading}")

                is Resource.ERROR -> {
                    showError(it.exception?.msg ?: getString(R.string.general_error_msg))
                }

                is Resource.SUCCESS -> {
                    it.data?.let { comic ->
                        comicsAdapter.updateLastComic(comic)
                        comicsAdapter.addComic(ComicView(comic.num - 1, isLoading = true))
                        totalComics = comic.num
                    }
                }

            }
        })

        viewModel.nextComicState.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.LOADING -> Log.i(TAG, "Loading: ${it.isLoading}")

                is Resource.ERROR -> {
                    showError(it.exception?.msg ?: getString(R.string.general_error_msg))
                }

                is Resource.SUCCESS -> {
                    it.data?.let { comic ->
                        comicsAdapter.updateLastComic(comic)
                        comicsAdapter.addComic(ComicView(comic.num - 1, isLoading = true))
                    }
                }

            }
        })
    }

    override fun onComicLoaded(num: Int) {
        if (totalComics <= 0) return
        binding.tvPageNumber.text = getString(R.string.home_fragment_bottom_comic_num, num, totalComics)
    }

    override fun onDetailsClicked(comicView: ComicView) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeToComicDetailsFragment(
                comicView.num
            )
        )
    }

    override fun onRefresh(comicView: ComicView) {
        viewModel.retry()
    }

    override fun onRetry() {
        viewModel.retry()
    }

}