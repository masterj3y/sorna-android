package io.github.masterj3y.sorna.core.extension

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import io.github.masterj3y.sorna.R

fun ImageView.loadFromUrl(url: String, vararg transformations: Transformation<Bitmap> = arrayOf()) =
    Glide.with(context)
        .load(url)
        .transform(*transformations)
        .centerCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)

fun ImageView.loadFromUri(uri: Uri) =
    Glide.with(context)
        .load(uri)
        .placeholder(R.drawable.ic_baseline_image_120)
        .centerCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)

fun ImageView.loadCircularFromUrl(
    url: String,
    vararg transformations: Transformation<Bitmap> = arrayOf()
) {
    Glide.with(context)
        .load(url)
        .transform(*transformations)
        .circleCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun ImageView.loadCircularFromUri(
    url: Uri,
    vararg transformations: Transformation<Bitmap> = arrayOf()
) {
    Glide.with(context)
        .load(url)
        .transform(*transformations)
        .circleCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun ImageView.loadUserProfile(
    url: Uri,
    vararg transformations: Transformation<Bitmap> = arrayOf()
) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.ic_user_profile_placeholder)
        .transform(*transformations)
        .circleCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}