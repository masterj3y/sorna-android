package io.github.masterj3y.sorna.feature.ad.user_ads

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.extension.addDividerItemDecoration
import io.github.masterj3y.sorna.core.platform.BaseFragment
import io.github.masterj3y.sorna.core.utils.AppSession
import io.github.masterj3y.sorna.databinding.FragmentUserAdsBinding
import io.github.masterj3y.sorna.feature.ad.Ad
import io.github.masterj3y.sorna.feature.ad.ads.AdsAdapter
import io.github.masterj3y.sorna.feature.ad.details.AdDetailsActivity
import io.github.masterj3y.sorna.feature.dialog.action.ActionDialog
import io.github.masterj3y.sorna.feature.dialog.action.ActionDialogItem
import io.github.masterj3y.sorna.feature.dialog.action.actionDialog
import io.github.masterj3y.sorna.feature.main.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
class UserAdsFragment : BaseFragment<FragmentUserAdsBinding>(R.layout.fragment_user_ads) {

    @Inject
    lateinit var appSession: AppSession

    private val model: UserAdsViewModel by viewModels()

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            adapter = AdsAdapter(::onItemClicked)
            viewModel = model.apply { getUserAds() }
            recyclerView.addDividerItemDecoration()
            refreshLayout.setOnRefreshListener { model.getUserAds() }
            createNewAd.setOnClickListener { switchTab(MainActivity.TAB_CREATE_NEW_AD) }
            retry.setOnClickListener { model.getUserAds() }
        }
    }

    @ExperimentalCoroutinesApi
    private fun onItemClicked(item: Ad) = actionDialog {
        title(item.title)
        items = dialogItems(item.id, this)
    }.show()

    @ExperimentalCoroutinesApi
    private fun dialogItems(adId: String, dialog: ActionDialog) = listOf(
            ActionDialogItem(
                    icon = R.drawable.ic_baseline_remove_red_eye_24,
                    text = getString(R.string.user_ads_show_details),
                    onClick = {
                        AdDetailsActivity.start(requireContext(), adId)
                        dialog.dismiss()
                    }
            ),
            ActionDialogItem(
                    icon = R.drawable.ic_baseline_delete_forever_24,
                    text = getString(R.string.user_ads_delete),
                    onClick = {
                        model.delete(adId)
                        dialog.dismiss()
                    }
            )
    )
}