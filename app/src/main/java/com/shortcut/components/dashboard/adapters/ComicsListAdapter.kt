package com.shortcut.components.dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shortcut.callbacks.OnRecyclerItemClickCallback
import com.shortcut.models.ComicView
import com.shortcut.utils.extensions.setOnSingleClickListener
import com.shortcut.xkcd.databinding.ItemComicListBinding

class ComicsListAdapter(
    var comics: MutableList<ComicView>,
    val callback: OnRecyclerItemClickCallback<ComicView>
) : RecyclerView.Adapter<ComicsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemComicListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = comics.size

    inner class ViewHolder(
        private val binding: ItemComicListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            with(binding) {
                comics[position].let { comicView ->
                    comic = comicView
                    itemView.setOnSingleClickListener {
                        callback.onRecyclerItemClicked(position, itemView, comicView)
                    }
                }

            }
        }
    }


}