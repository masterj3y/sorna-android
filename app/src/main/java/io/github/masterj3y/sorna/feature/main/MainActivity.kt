package io.github.masterj3y.sorna.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavSwitchController
import com.ncapdevi.fragnav.FragNavTransactionOptions
import com.ncapdevi.fragnav.tabhistory.UniqueTabHistoryStrategy
import dagger.hilt.android.AndroidEntryPoint
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.platform.BaseActivity
import io.github.masterj3y.sorna.core.platform.BaseFragment
import io.github.masterj3y.sorna.core.utils.AppSession
import io.github.masterj3y.sorna.databinding.ActivityMainBinding
import io.github.masterj3y.sorna.feature.ad.ads.AdsFragment
import io.github.masterj3y.sorna.feature.ad.create.CreateNewAdFragment
import io.github.masterj3y.sorna.feature.ad.user_ads.UserAdsFragment
import io.github.masterj3y.sorna.feature.auth.AuthActivity
import io.github.masterj3y.sorna.feature.categories.CategoriesFragment
import io.github.masterj3y.sorna.feature.user_profile.UserProfileFragment
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), BaseFragment.FragmentNavigation {

    @Inject
    lateinit var appSession: AppSession

    private val fragNavController: FragNavController by lazy {
        FragNavController(supportFragmentManager, R.id.tabContainer)
    }

    override fun layoutRes(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (appSession.isLoggedIn.not()) {
            startAuthentication()
            finishAffinity()
        }

        initFragNavController(savedInstanceState)
    }

    private fun initFragNavController(savedInstanceState: Bundle?) = with(binding) {
        val fragments = listOf(
                UserProfileFragment(),
                UserAdsFragment(),
                CreateNewAdFragment(),
                CategoriesFragment(),
                AdsFragment()
        )

        fragNavController.apply {
            createEager = true
            fragmentHideStrategy = FragNavController.HIDE
            rootFragments = fragments
            navigationStrategy = UniqueTabHistoryStrategy(object : FragNavSwitchController {
                override fun switchTab(index: Int, transactionOptions: FragNavTransactionOptions?) {
                    bottomNavigation.selectedItemId = tabs[index] ?: R.id.mainTabAds
                }
            })
        }

        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mainTabUserProfile -> selectTab(TAB_USER_PROFILE)
                R.id.mainTabUserAds -> selectTab(TAB_USER_ADS)
                R.id.mainTabCreateNewAd -> selectTab(TAB_CREATE_NEW_AD)
                R.id.mainTabCategories -> selectTab(TAB_CATEGORIES)
                R.id.mainTabAds -> selectTab(TAB_ADS)
                else -> false
            }
        }
        bottomNavigation.setOnNavigationItemReselectedListener { fragNavController.clearStack() }

        fragNavController.initialize(TAB_ADS, savedInstanceState)
        bottomNavigation.selectedItemId = tabs[TAB_ADS] ?: R.id.mainTabAds
    }

    private fun selectTab(tab: Int): Boolean {
        fragNavController.switchTab(tab)
        return true
    }

    private fun startAuthentication() = AuthActivity.start(this)

    override fun onBackPressed() {
        if (fragNavController.popFragment().not())
            super.onBackPressed()
    }

    override fun getFragNavigationController() = fragNavController

    companion object {

        private const val TAB_USER_PROFILE = FragNavController.TAB1
        private const val TAB_USER_ADS = FragNavController.TAB2
        private const val TAB_CREATE_NEW_AD = FragNavController.TAB3
        private const val TAB_CATEGORIES = FragNavController.TAB4
        private const val TAB_ADS = FragNavController.TAB5

        private val tabs: HashMap<Int, Int> = hashMapOf(
                TAB_USER_PROFILE to R.id.mainTabUserProfile,
                TAB_USER_ADS to R.id.mainTabUserAds,
                TAB_CREATE_NEW_AD to R.id.mainTabCreateNewAd,
                TAB_CATEGORIES to R.id.mainTabCategories,
                TAB_ADS to R.id.mainTabAds
        )

        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}