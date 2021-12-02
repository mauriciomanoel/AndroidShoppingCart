package com.mauricio.vizcodeassignment.utils.bidings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

object ImageViewBinding {

    @JvmStatic
    @BindingAdapter("src_url")
    fun loadFromURL(image: ImageView, source: String?) {
        if (source == null) return
        Glide.with(image.context)
            .load(source)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(image)
    }
}