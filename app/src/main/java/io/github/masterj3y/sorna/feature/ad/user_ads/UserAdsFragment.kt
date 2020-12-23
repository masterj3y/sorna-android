package io.github.masterj3y.sorna.feature.ad.user_ads

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.platform.BaseFragment
import io.github.masterj3y.sorna.databinding.FragmentUserAdsBinding
import io.github.masterj3y.sorna.feature.ad.Ad
import io.github.masterj3y.sorna.feature.ad.ads.AdsAdapter
import io.github.masterj3y.sorna.feature.ad.details.AdDetailsActivity

@AndroidEntryPoint
class UserAdsFragment : BaseFragment<FragmentUserAdsBinding>(R.layout.fragment_user_ads) {

    private val model: UserAdsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            adapter = AdsAdapter(::onItemClicked)
            viewModel = model.apply { getUserAds() }
            refreshLayout.setOnRefreshListener { model.getUserAds() }
        }
    }

    private fun onItemClicked(item: Ad) =
            AdDetailsActivity.start(requireContext(), item.id)
}