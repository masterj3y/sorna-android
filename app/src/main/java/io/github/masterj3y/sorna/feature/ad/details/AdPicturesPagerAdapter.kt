package io.github.masterj3y.sorna.feature.ad.details

import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.extension.inflate
import io.github.masterj3y.sorna.core.extension.loadFromUrl
import io.github.masterj3y.sorna.databinding.ItemAdPicturePagerBinding
import io.github.masterj3y.sorna.feature.ad.Ad
import io.github.masterj3y.sorna.feature.ad.AdPicture
import io.github.masterj3y.sorna.feature.ad.ads.AdsAdapter
import io.github.masterj3y.sorna.feature.ad.ads.absolutePicUrl

class AdPicturesPagerAdapter() :
        RecyclerView.Adapter<AdPicturesPagerAdapter.AdPicturesPagerViewHolder>() {

    private var list: List<AdPicture> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            AdPicturesPagerViewHolder(
                    ItemAdPicturePagerBinding.bind(parent.inflate(R.layout.item_ad_picture_pager))
            )

    override fun onBindViewHolder(holder: AdPicturesPagerViewHolder, position: Int) =
            holder.render(list[position])

    override fun getItemCount(): Int = list.size

    fun submitList(newList: List<AdPicture>) {
        this.list = newList
        notifyDataSetChanged()
    }

    inner class AdPicturesPagerViewHolder(private val binding: ItemAdPicturePagerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun render(item: AdPicture) = with(binding) {
            picture = item.copy(picUrl = item.picUrl.absolutePicUrl())
        }
    }
}

@BindingAdapter("android:bindAdPicturesList")
fun bindAdPicturesList(view: ViewPager2, list: List<AdPicture>?) = with(view) {
    list?.let {
        (view.adapter as? AdPicturesPagerAdapter)?.submitList(it)
    }
}