package io.github.masterj3y.sorna.feature.ad.saved_ads

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.extension.addDividerItemDecoration
import io.github.masterj3y.sorna.core.platform.BaseFragment
import io.github.masterj3y.sorna.databinding.FragmentSavedAdsBinding
import io.github.masterj3y.sorna.feature.ad.Ad
import io.github.masterj3y.sorna.feature.ad.ads.AdsAdapter
import io.github.masterj3y.sorna.feature.ad.details.AdDetailsActivity

@AndroidEntryPoint
class SavedAdsFragment : BaseFragment<FragmentSavedAdsBinding>(R.layout.fragment_saved_ads) {

    private val model: SavedAdsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            adapter = AdsAdapter(::onItemClicked)
            viewModel = model.apply { getSavedAds() }
            recyclerView.addDividerItemDecoration()
            refreshLayout.setOnRefreshListener { model.getSavedAds() }
            retry.setOnClickListener { model.getSavedAds() }
        }
    }

    private fun onItemClicked(item: Ad) =
            AdDetailsActivity.start(requireContext(), item.id)
}