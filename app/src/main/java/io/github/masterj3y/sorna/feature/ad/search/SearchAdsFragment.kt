package io.github.masterj3y.sorna.feature.ad.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.extension.addDividerItemDecoration
import io.github.masterj3y.sorna.core.platform.BaseFragment
import io.github.masterj3y.sorna.databinding.FragmentSearchAdsBinding
import io.github.masterj3y.sorna.feature.ad.Ad
import io.github.masterj3y.sorna.feature.ad.ads.AdsAdapter
import io.github.masterj3y.sorna.feature.ad.details.AdDetailsActivity

@AndroidEntryPoint
class SearchAdsFragment : BaseFragment<FragmentSearchAdsBinding>(R.layout.fragment_search_ads) {

    val model: SearchAdsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            adapter = AdsAdapter(::onItemClicked)
            viewModel = model
            recyclerView.addDividerItemDecoration()
            refreshLayout.setOnRefreshListener { refresh() }
        }
    }

    private fun refresh() = with(binding) {
        input.text.toString().let { model.search(it) }
    }

    private fun onItemClicked(item: Ad) =
            AdDetailsActivity.start(requireContext(), item.id)
}