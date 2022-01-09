package com.shortcut.components.dashboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shortcut.components.dashboard.listener.ComicItemListener
import com.shortcut.xkcd.databinding.ItemComicViewBinding
import com.shortcut.models.ComicView
import com.shortcut.utils.extensions.setOnSingleClickListener
import com.shortcut.xkcd.R
import java.util.*
import kotlin.concurrent.schedule

class ComicsPagerAdapter(private var values: MutableList<ComicView>) :
    RecyclerView.Adapter<ComicsPagerAdapter.ComicViewHolder>() {

    var comicItemListener: ComicItemListener? = null

    fun addComic(comicView: ComicView) {
        values.add(comicView)
        notifyItemInserted(values.lastIndex)
    }

    fun updateLastComic(comicView: ComicView) {
        values[values.lastIndex] = comicView
        notifyItemChanged(values.lastIndex)
    }

    fun getItemPosition(position: Int): ComicView? {
        return if (position < values.size) values[position] else null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val inflate =
            ItemComicViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ComicViewHolder(inflate.root)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = values.size

    inner class ComicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemComicViewBinding.bind(view)

        fun bind(position: Int) {
            with(binding) {
                values[position].let { comicView ->
                    ivComic.setImageDrawable(
                        ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.default_ph_24
                        )
                    )

                    tvComicTitle.text = comicView.title

                    if (comicView.isLoading) {
                        showLoading(comicView)
                        return
                    }

                    hideLoading()
                    comicView.imgUrl?.let { url ->
                        Glide.with(itemView.context).load(url)
                            .placeholder(R.drawable.default_ph_24)
                            .into(ivComic)
                    }

                    ivDetails.setOnSingleClickListener {
                        comicItemListener?.onDetailsClicked(comicView)
                    }

                    comicItemListener?.onComicLoaded(comicView.num)
                }

            }
        }

        private fun ItemComicViewBinding.hideLoading() {
            swipeRefreshLayout.isRefreshing = false
            swipeRefreshLayout.isEnabled = false
        }

        private fun ItemComicViewBinding.showLoading(comicView: ComicView) {
            swipeRefreshLayout.isEnabled = true
            swipeRefreshLayout.isRefreshing = true
            Timer().schedule(3000) {
                swipeRefreshLayout.isRefreshing = false
            }

            swipeRefreshLayout.setOnRefreshListener {
                comicItemListener?.onRefresh(comicView)
                Timer().schedule(3000) {
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }


}