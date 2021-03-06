package io.github.masterj3y.sorna.core.binding

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.masterj3y.sorna.core.extension.*
import java.text.DecimalFormat

@BindingAdapter("android:loadImageFromUrl")
fun loadImageFromUrl(view: ImageView, url: String?) {
    url?.let { view.loadFromUrl(it) }
}

@BindingAdapter("android:loadCircularImageFromUrl")
fun loadCircularImageFromUrl(view: ImageView, url: String?) {
    url?.let { view.loadCircularFromUrl(it) }
}

@BindingAdapter("android:loadCircularImageFromUri")
fun loadCircularImageFromUri(view: ImageView, uri: Uri?) {
    uri?.let { view.loadCircularFromUri(it) }
}

@BindingAdapter("android:loadUserProfile")
fun loadUserProfile(view: ImageView, uri: Uri?) {
    uri?.let { view.loadUserProfile(uri) }
}

@BindingAdapter("android:loadImageFromUri")
fun loadImageFromUri(view: ImageView, url: Uri?) {
    url?.let { view.loadFromUri(it) }
}

@BindingAdapter("android:bindDrawableStart")
fun loadImageFromResource(view: TextView, @DrawableRes drawableRes: Int?) {
    drawableRes?.let {
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(it, 0, 0, 0)
    }
}

@BindingAdapter("android:gone")
fun gone(view: View, gone: Boolean) {
    view.visibility = if (gone) View.GONE else View.VISIBLE
}

@BindingAdapter("android:invisible")
fun invisible(view: View, invisible: Boolean) {
    view.visibility = if (invisible) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter("android:adapter")
fun bindAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}

@BindingAdapter("android:adapter")
fun bindViewPagerAdapter(view: ViewPager2, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}

@BindingAdapter("android:attachToRecyclerView")
fun attachToRecyclerView(view: FloatingActionButton, recyclerView: RecyclerView) {
    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0 && view.isShown)
                view.hide()
            else
                view.show()
        }
    })
}

@BindingAdapter("android:refreshing")
fun swipeRefreshLayoutRefreshing(view: SwipeRefreshLayout, isRefreshing: Boolean?) {
    view.isRefreshing = isRefreshing ?: false
}

@BindingAdapter("android:textCurrencyFormat")
fun formatCurrency(view: TextView, amount: Long?) = with(view) {
    amount?.let {
        val formatter = DecimalFormat("###,###,###")
        text = formatter.format(it.toDouble())
    }
}