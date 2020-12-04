package io.github.masterj3y.sorna.feature.dialog.action

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.extension.visible
import io.github.masterj3y.sorna.databinding.DialogActionBinding
import io.github.masterj3y.sorna.feature.dialog.BaseBottomSheetDialog

@SuppressLint("InflateParams")
class ActionDialog(context: Context) : BaseBottomSheetDialog(context) {

    private val binding: DialogActionBinding by lazy {
        DataBindingUtil.inflate<DialogActionBinding>(
                LayoutInflater.from(context), R.layout.dialog_action, null, false
        )
    }

    override val dialogView: View by lazy { binding.root }

    var items: List<ActionDialogItem> = listOf()
        set(value) {
            field = value
            binding.recyclerView.adapter = ActionDialogAdapter(field)
        }

    fun title(text: String) = with(binding.title) {
        visible()
        this.text = text
    }

    fun title(@StringRes text: Int) = with(binding.title) {
        visible()
        this.setText(text)
    }
}

inline fun Context.actionDialog(func: ActionDialog.() -> Unit) =
        ActionDialog(this).apply { func() }

inline fun Fragment.actionDialog(func: ActionDialog.() -> Unit) =
        ActionDialog(requireContext()).apply { func() }