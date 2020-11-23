package io.github.masterj3y.sorna.core.extension

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.annotation.LayoutRes
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun itemAnimator() = SlideInUpAnimator(OvershootInterpolator(1f)).apply {
    addDuration = 300
    removeDuration = 300
    moveDuration = 300
    changeDuration = 300
}