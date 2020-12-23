package io.github.masterj3y.sorna.feature.categories.categorized_ads

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.skydoves.bundler.bundle
import com.skydoves.bundler.intentOf
import dagger.hilt.android.AndroidEntryPoint
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.platform.BaseFragment
import io.github.masterj3y.sorna.databinding.FragmentCategorizedAdsBinding
import io.github.masterj3y.sorna.feature.ad.Ad
import io.github.masterj3y.sorna.feature.ad.ads.AdsAdapter
import io.github.masterj3y.sorna.feature.ad.details.AdDetailsActivity

@AndroidEntryPoint
class CategorizedAdsFragment : BaseFragment<FragmentCategorizedAdsBinding>(R.layout.fragment_categorized_ads) {

    private val categoryId: String by bundle(CATEGORY_KEY, "")

    private val model: CategorizedAdsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            adapter = AdsAdapter(::onItemClicked)
            viewModel = model.apply { getCategorizedAds(categoryId) }
            refreshLayout.setOnRefreshListener { model.getCategorizedAds(categoryId) }
        }
    }

    private fun onItemClicked(item: Ad) =
            AdDetailsActivity.start(requireContext(), item.id)

    companion object {
        private const val CATEGORY_KEY = "key-category"

        fun instance(categoryId: String): Fragment = CategorizedAdsFragment().apply {
            arguments = intentOf {
                +(CATEGORY_KEY to categoryId)
            }.extras
        }
    }
}