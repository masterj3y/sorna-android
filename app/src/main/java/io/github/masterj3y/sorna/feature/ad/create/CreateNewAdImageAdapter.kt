package io.github.masterj3y.sorna.feature.ad.create

import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.extension.inflate
import io.github.masterj3y.sorna.databinding.ItemAdImagePickerBinding
import io.github.masterj3y.sorna.feature.ad.create.CreateNewAdFragment.Companion.AD_MAX_IMAGES_COUNT

class CreateNewAdImageAdapter(
        private val listener: OnItemClickedListener
) : RecyclerView.Adapter<CreateNewAdImageAdapter.CreateNewImageViewHolder>() {

    private var list: MutableList<Uri> = initList()

    private fun initList() = MutableList(AD_MAX_IMAGES_COUNT) { Uri.EMPTY }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            CreateNewImageViewHolder(
                    ItemAdImagePickerBinding.bind(parent.inflate(R.layout.item_ad_image_picker))
            )

    override fun onBindViewHolder(holder: CreateNewImageViewHolder, position: Int) =
            holder.render(list[position])

    override fun getItemCount() = list.size

    fun submitList(newList: List<Uri>) {
        list = initList()
        for (i in newList.indices)
            list[i] = newList[i]
        notifyDataSetChanged()
    }

    private fun removeItem(position: Int) {
        list.removeAt(position)
        list.add(Uri.EMPTY)
        notifyDataSetChanged()
    }

    inner class CreateNewImageViewHolder(private val binding: ItemAdImagePickerBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun render(item: Uri) = with(binding) {
            image = item
            root.setOnClickListener { listener.onImageClicked(adapterPosition) }
            delete.setOnClickListener {
                listener.onRemoveImageClicked(adapterPosition)
                removeItem(adapterPosition)
            }
        }
    }

    interface OnItemClickedListener {
        fun onImageClicked(position: Int)
        fun onRemoveImageClicked(position: Int)
    }
}