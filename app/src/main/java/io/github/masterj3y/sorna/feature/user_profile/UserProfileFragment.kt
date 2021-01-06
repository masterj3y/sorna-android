package io.github.masterj3y.sorna.feature.user_profile

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.platform.BaseFragment
import io.github.masterj3y.sorna.core.utils.AppSession
import io.github.masterj3y.sorna.core.utils.LocaleHelper.ENGLISH
import io.github.masterj3y.sorna.core.utils.LocaleHelper.PERSIAN
import io.github.masterj3y.sorna.databinding.FragmentUserProfileBinding
import io.github.masterj3y.sorna.feature.ad.saved_ads.SavedAdsFragment
import io.github.masterj3y.sorna.feature.ad.user_ads.UserAdsFragment
import io.github.masterj3y.sorna.feature.dialog.action.ActionDialog
import io.github.masterj3y.sorna.feature.dialog.action.ActionDialogItem
import io.github.masterj3y.sorna.feature.dialog.action.actionDialog
import io.github.masterj3y.sorna.feature.main.MainActivity
import javax.inject.Inject

@AndroidEntryPoint
class UserProfileFragment :
    BaseFragment<FragmentUserProfileBinding>(R.layout.fragment_user_profile) {

    @Inject
    lateinit var appSession: AppSession

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            userProfile = appSession.userProfile
            userAds.setOnClickListener { openUserAds() }
            savedAds.setOnClickListener { openSavedAds() }
            nightModeLayout.setOnClickListener { nightMode.toggle() }
            nightMode.setOnCheckedChangeListener { _, isChecked -> switchNightMode(isChecked) }
            nightMode.isChecked = appSession.nightModeEnabled
            changeAppLanguage.setOnClickListener { showLanguagesDialog() }
            logout.setOnClickListener { appSession.logout(requireActivity()) }
        }
    }

    private fun switchNightMode(isNightMode: Boolean) {
        switchTab(MainActivity.TAB_ADS)
        toggleNightMode(isNightMode)
    }

    private fun showLanguagesDialog() = actionDialog {
        title(R.string.app_language)
        items = listOf(
            ActionDialogItem(text = getString(R.string.app_language_persian)) {
                changeLanguage(
                    this,
                    PERSIAN
                )
            },
            ActionDialogItem(text = getString(R.string.app_language_english)) {
                changeLanguage(
                    this,
                    ENGLISH
                )
            }
        )
    }.show()

    private fun changeLanguage(dialog: ActionDialog, languageCode: String) {
        appSession.appLanguage = languageCode
        dialog.dismiss()
        switchTab(MainActivity.TAB_ADS)
        requireActivity().recreate()
    }

    private fun openUserAds() = pushFragment(UserAdsFragment())
    private fun openSavedAds() = pushFragment(SavedAdsFragment())
}