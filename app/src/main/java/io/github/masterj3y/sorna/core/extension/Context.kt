package io.github.masterj3y.sorna.core.extension

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

fun Context.openPhoto(fileName: String) {
    val type = "image/*"
    val intent = Intent(Intent.ACTION_VIEW)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val file = File(fileName)
        val uri =
            FileProvider.getUriForFile(
                this, this.packageName + ".provider",
                file
            )
        intent.setDataAndType(uri, type)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    } else {
        intent.setDataAndType(Uri.parse("file://$fileName"), type)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(intent)
}

fun Context.openVideo(fileName: String) {
    val type = "video/*"
    val intent = Intent(Intent.ACTION_VIEW)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val file = File(fileName)
        val uri =
            FileProvider.getUriForFile(
                this, this.packageName + ".provider",
                file
            )
        intent.setDataAndType(uri, type)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    } else {
        intent.setDataAndType(Uri.parse("file://$fileName"), type)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(intent)
}

fun Float.dpToPx(): Float = this * Resources.getSystem().displayMetrics.density