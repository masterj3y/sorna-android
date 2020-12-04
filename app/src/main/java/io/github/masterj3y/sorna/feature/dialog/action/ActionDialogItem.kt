package io.github.masterj3y.sorna.feature.dialog.action

import androidx.annotation.DrawableRes

data class ActionDialogItem(
    @DrawableRes
    val icon: Int? = null,
    val text: String,
    val onClick: () -> Unit = {}
)
