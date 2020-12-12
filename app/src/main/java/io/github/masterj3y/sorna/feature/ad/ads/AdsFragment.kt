package io.github.masterj3y.sorna.feature.ad.ads

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.platform.BaseFragment
import io.github.masterj3y.sorna.databinding.FragmentAdsBinding
import io.github.masterj3y.sorna.feature.ad.Ad
import io.github.masterj3y.sorna.feature.ad.details.AdDetailsActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class AdsFragment : BaseFragment<FragmentAdsBinding>(R.layout.fragment_ads) {

    private val model: AdsViewModel by viewModels()

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            adapter = AdsAdapter(::onItemClicked)
            viewModel = model.apply { getAds() }
            refreshLayout.setOnRefreshListener { model.getAds() }
        }

        model.ads.observe(viewLifecycleOwner) {
            println(it)
        }
    }

    private fun onItemClicked(item: Ad) =
        AdDetailsActivity.start(requireContext(), item.id)
}