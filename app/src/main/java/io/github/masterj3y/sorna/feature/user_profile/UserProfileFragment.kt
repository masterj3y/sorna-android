package io.github.masterj3y.sorna.feature.user_profile

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.platform.BaseFragment
import io.github.masterj3y.sorna.core.utils.AppSession
import io.github.masterj3y.sorna.databinding.FragmentUserProfileBinding
import io.github.masterj3y.sorna.feature.ad.saved_ads.SavedAdsFragment
import io.github.masterj3y.sorna.feature.ad.user_ads.UserAdsFragment
import javax.inject.Inject

@AndroidEntryPoint
class UserProfileFragment : BaseFragment<FragmentUserProfileBinding>(R.layout.fragment_user_profile) {

    @Inject
    lateinit var appSession: AppSession

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            userProfile = appSession.userProfile
            userAds.setOnClickListener { openUserAds() }
            savedAds.setOnClickListener { openSavedAds() }
        }
    }

    private fun openUserAds() = pushFragment(UserAdsFragment())
    private fun openSavedAds() = pushFragment(SavedAdsFragment())
}