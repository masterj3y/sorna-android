package io.github.masterj3y.sorna.feature.dialog.action

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.extension.inflate
import io.github.masterj3y.sorna.databinding.DialogActionItemBinding

class ActionDialogAdapter(private val list: List<ActionDialogItem>) :
    RecyclerView.Adapter<ActionDialogAdapter.ActionBottomSheetDialogViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActionBottomSheetDialogViewHolder =
        ActionBottomSheetDialogViewHolder(
            DialogActionItemBinding.bind(
                parent.inflate(
                    R.layout.dialog_action_item
                )
            )
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ActionBottomSheetDialogViewHolder, position: Int) =
        holder.bind(list[position])

    inner class ActionBottomSheetDialogViewHolder(private val binding: DialogActionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { list[adapterPosition].onClick() }
        }

        fun bind(item: ActionDialogItem) {
            binding.item = item
        }
    }
}
