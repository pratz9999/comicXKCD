package com.shortcut.binding

import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.shortcut.xkcd.R


@BindingAdapter("url")
fun loadImage(view: ImageView, url: String?) {
    Glide.with(view.context)
        .load(url)
        .apply(RequestOptions().centerInside())
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .placeholder(R.drawable.default_ph_24)
        .into(view)
}

@BindingAdapter("fav")
fun setFavoriteIcon(view: AppCompatImageView, isFav: Boolean) {
    if (isFav) {
        view.setImageResource(R.drawable.ic_favorite_24)
    } else view.setImageResource(R.drawable.ic_favorite_border_24)
}

