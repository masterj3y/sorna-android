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
import io.github.masterj3y.sorna.databinding.ActivityMainBinding
import io.github.masterj3y.sorna.feature.ad.ads.AdsFragment
import io.github.masterj3y.sorna.feature.ad.create.CreateNewAdFragment
import io.github.masterj3y.sorna.feature.ad.search.SearchAdsFragment
import io.github.masterj3y.sorna.feature.auth.AuthActivity
import io.github.masterj3y.sorna.feature.categories.CategoriesFragment
import io.github.masterj3y.sorna.feature.user_profile.UserProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), BaseFragment.FragmentNavigation {

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
                CategoriesFragment(),
                CreateNewAdFragment(),
                SearchAdsFragment(),
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
                R.id.mainTabCategories -> selectTab(TAB_CATEGORIES)
                R.id.mainTabCreateNewAd -> selectTab(TAB_CREATE_NEW_AD)
                R.id.mainTabSearchAds -> selectTab(TAB_SEARCH_ADS)
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

    override fun switchTab(tab: Int) {
        bottomNavigation.selectedItemId = requireNotNull(tabs[tab])
    }

    companion object {

        const val TAB_USER_PROFILE = FragNavController.TAB1
        const val TAB_CATEGORIES = FragNavController.TAB2
        const val TAB_CREATE_NEW_AD = FragNavController.TAB3
        const val TAB_SEARCH_ADS = FragNavController.TAB4
        const val TAB_ADS = FragNavController.TAB5

        private val tabs: HashMap<Int, Int> = hashMapOf(
                TAB_USER_PROFILE to R.id.mainTabUserProfile,
                TAB_CATEGORIES to R.id.mainTabCategories,
                TAB_CREATE_NEW_AD to R.id.mainTabCreateNewAd,
                TAB_SEARCH_ADS to R.id.mainTabSearchAds,
                TAB_ADS to R.id.mainTabAds
        )

        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}