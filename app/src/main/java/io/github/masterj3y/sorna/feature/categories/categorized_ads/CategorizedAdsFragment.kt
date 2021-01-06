package io.github.masterj3y.sorna.feature.categories.categorized_ads

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.skydoves.bundler.bundle
import com.skydoves.bundler.intentOf
import dagger.hilt.android.AndroidEntryPoint
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.extension.addDividerItemDecoration
import io.github.masterj3y.sorna.core.platform.BaseFragment
import io.github.masterj3y.sorna.databinding.FragmentCategorizedAdsBinding
import io.github.masterj3y.sorna.feature.ad.Ad
import io.github.masterj3y.sorna.feature.ad.ads.AdsAdapter
import io.github.masterj3y.sorna.feature.ad.details.AdDetailsActivity

@AndroidEntryPoint
class CategorizedAdsFragment : BaseFragment<FragmentCategorizedAdsBinding>(R.layout.fragment_categorized_ads) {

    private val categoryId: String by bundle(CATEGORY_ID_KEY, "")
    private val categoryTitle: String by bundle(CATEGORY_TITLE_KEY, "")

    private val model: CategorizedAdsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            toolbarTitle.text = categoryTitle
            adapter = AdsAdapter(::onItemClicked)
            viewModel = model.apply { getCategorizedAds(categoryId) }
            recyclerView.addDividerItemDecoration()
            refreshLayout.setOnRefreshListener { model.getCategorizedAds(categoryId) }
            retry.setOnClickListener { model.getCategorizedAds(categoryId) }
        }
    }

    private fun onItemClicked(item: Ad) =
            AdDetailsActivity.start(requireContext(), item.id)

    companion object {
        private const val CATEGORY_ID_KEY = "key:category-id"
        private const val CATEGORY_TITLE_KEY = "key:category-title"

        fun instance(categoryId: String, categoryTitle: String): Fragment = CategorizedAdsFragment().apply {
            arguments = intentOf {
                +(CATEGORY_ID_KEY to categoryId)
                +(CATEGORY_TITLE_KEY to categoryTitle)
            }.extras
        }
    }
}