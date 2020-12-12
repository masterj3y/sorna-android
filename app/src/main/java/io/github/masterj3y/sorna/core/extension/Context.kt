package io.github.masterj3y.sorna.core.extension

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri

fun Context.call(phoneNumber: String) {
    val intent = com.skydoves.bundler.intentOf {

        setAction(Intent.ACTION_DIAL)
        setData(Uri.parse("tel:$phoneNumber"))
    }
    startActivity(intent)
}

fun Float.dpToPx(): Float = this * Resources.getSystem().displayMetrics.density