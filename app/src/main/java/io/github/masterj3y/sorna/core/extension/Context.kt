package io.github.masterj3y.sorna.core.extension

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Context.call(phoneNumber: String) {
    val intent = com.skydoves.bundler.intentOf {

        setAction(Intent.ACTION_DIAL)
        setData(Uri.parse("tel:$phoneNumber"))
    }
    startActivity(intent)
}

fun Context.toast(@StringRes message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(@StringRes message: Int) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Float.dpToPx(): Float = this * Resources.getSystem().displayMetrics.density