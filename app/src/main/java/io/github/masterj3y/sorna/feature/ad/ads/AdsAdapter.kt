package io.github.masterj3y.sorna.feature.ad.ads

import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import io.github.masterj3y.sorna.BuildConfig
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.extension.inflate
import io.github.masterj3y.sorna.core.extension.loadFromUrl
import io.github.masterj3y.sorna.core.platform.DiffCallBack
import io.github.masterj3y.sorna.databinding.ItemAdBinding
import io.github.masterj3y.sorna.feature.ad.Ad
import io.github.masterj3y.sorna.feature.ad.AdPicture

class AdsAdapter(private val listener: OnItemClickedListener) :
    RecyclerView.Adapter<AdsAdapter.AdsViewHolder>() {

    private val differ = AsyncListDiffer(this, DiffCallBack<Ad>())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AdsViewHolder(
            ItemAdBinding.bind(parent.inflate(R.layout.item_ad))
        )

    override fun onBindViewHolder(holder: AdsViewHolder, position: Int) =
        holder.render(differ.currentList[position])

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(newList: List<Ad>) {
        differ.submitList(newList)
    }

    inner class AdsViewHolder(private val binding: ItemAdBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun render(item: Ad) = with(binding) {
            ad = item
            root.setOnClickListener { listener.onItemClicked(getItem()) }
        }

        private fun getItem() = differ.currentList[adapterPosition]
    }

    fun interface OnItemClickedListener {
        fun onItemClicked(item: Ad)
    }
}

@BindingAdapter("android:loadAdFirstPic")
fun loadAdFirstPic(view: ImageView, pics: List<AdPicture>?) = with(view) {
    pics?.let {
        if (it.isNotEmpty()) {
            val picUrl = it[0].picUrl.absolutePicUrl()
            loadFromUrl(picUrl)
            println(picUrl)
        }
    }
}

fun String.absolutePicUrl() = BuildConfig.BASE_URL + replace("\\", "/")

@BindingAdapter("android:bindAdsList")
fun bindAdsList(view: RecyclerView, list: List<Ad>?) {
    list?.let {
        (view.adapter as? AdsAdapter)?.submitList(it)
    }
}
