package com.shortcut.components.dashboard.details

import android.icu.text.DateFormat
import android.icu.util.Calendar
import android.view.*
import androidx.navigation.fragment.navArgs
import com.shortcut.components.dashboard.base.BaseViewModelFragment
import com.shortcut.models.ComicView
import com.shortcut.utils.Constants
import com.shortcut.utils.extensions.openCustomTabBrowser
import com.shortcut.utils.extensions.share
import com.shortcut.xkcd.R
import com.shortcut.xkcd.databinding.FragmentDetailsBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf


class ComicDetailsFragment :
    BaseViewModelFragment<ComicDetailsViewModel, FragmentDetailsBinding>(),
    View.OnClickListener {

    private lateinit var selectedComic: ComicView
    private val navArgs: ComicDetailsFragmentArgs by navArgs()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDetailsBinding
        get() = FragmentDetailsBinding::inflate


    override fun getInjectViewModel(): ComicDetailsViewModel {
        return getViewModel { parametersOf(navArgs.comicNum) }
    }

    override fun initViews() {
        binding.fabBrowser.setOnClickListener(this)
        binding.fabShare.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
        binding.switchFav.setOnCheckedChangeListener { _, isChecked ->
            changeFavorite(isChecked)
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.comicMLiveData.observe(viewLifecycleOwner, { comic ->
            if (comic == null) return@observe
            selectedComic = comic
            with(binding) {
                binding.comic = selectedComic
                val title = getString(R.string.details_fragment_comic_title, comic.title, comic.num)
                tvTitle.text = title
                tvDate.text = getDateFormatted(comic)
                setIsFavoriteIcon(comic.isFavorite)
                tvDetails.text = if (comic.transcript.isBlank()) comic.alt else comic.transcript
            }
        })
    }

    private fun openExplanation() {
        if (!this::selectedComic.isInitialized) return
        val url = "${Constants.EXPLAIN_BASE_URL}${selectedComic.num}"
        requireContext().openCustomTabBrowser(url)
    }

    private fun shareComic() {
        requireContext().share(selectedComic.title, Constants.getShareUrl(selectedComic.num))
    }

    private fun changeFavorite(isChecked: Boolean) {
        selectedComic.isFavorite = isChecked
        setIsFavoriteIcon(selectedComic.isFavorite)
        viewModel.setFavorite(selectedComic.isFavorite)
    }

    private fun setIsFavoriteIcon(isFav: Boolean) {
        binding.switchFav.isChecked = isFav
    }

    private fun getDateFormatted(comic: ComicView): CharSequence {
        val calendar = Calendar.getInstance()
        calendar.set(comic.year, comic.month, comic.day)
        val dateFormat: DateFormat = DateFormat.getDateInstance()
        return dateFormat.format(calendar.time)
    }

    override fun onRetry() {
        //No action required
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.fab_browser -> {
                openExplanation()
            }
            R.id.fab_share -> {
                shareComic()
            }
            R.id.btn_back -> {
                requireActivity().onBackPressed()
            }
        }
    }

}