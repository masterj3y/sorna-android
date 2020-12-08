package io.github.masterj3y.sorna.core.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import io.github.masterj3y.sorna.R
import java.util.*


private const val SECOND_MILLIS = 1000
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS

@BindingAdapter("android:textTimeToMoment")
fun textTimeToMoment(view: TextView, timestamp: Long?) = with(view) {

    var time = timestamp ?: 0

    if (time < 1000000000000L) {
        time *= 1000
    }

    val now = Date().time

    val diff = now - time

    text = when {
        diff < MINUTE_MILLIS ->context.getString(R.string.time_to_moment_now)
        diff < 2 * MINUTE_MILLIS -> context.getString(R.string.time_to_moment_one_minute_ago)
        diff < 50 * MINUTE_MILLIS -> context.getString(
            R.string.time_to_moment_some_minutes_ago,
            diff / MINUTE_MILLIS
        )
        diff < 90 * MINUTE_MILLIS -> context.getString(R.string.time_to_moment_one_hour_ago)
        diff < 24 * HOUR_MILLIS -> context.getString(
            R.string.time_to_moment_some_hours_ago,
            diff / HOUR_MILLIS
        )
        diff < 48 * HOUR_MILLIS -> context.getString(R.string.time_to_moment_one_hour_ago)
        else -> context.getString(R.string.time_to_moment_some_days_ago, diff / DAY_MILLIS)
    }
}