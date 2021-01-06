package io.github.masterj3y.sorna.core.platform

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.github.masterj3y.sorna.core.platform.BaseFragment.NightModeCallback
import io.github.masterj3y.sorna.core.utils.AppSession
import io.github.masterj3y.sorna.core.utils.LocaleHelper
import io.github.masterj3y.sorna.core.utils.LocaleHelper.PERSIAN
import javax.inject.Inject

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity(), NightModeCallback {

    @Inject
    lateinit var appSession: AppSession

    lateinit var binding: B

    abstract fun layoutRes(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LocaleHelper.setLocale(this, appSession.appLanguage)

        val layoutDirection =
            if (appSession.appLanguage == PERSIAN) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
        window.decorView.layoutDirection = layoutDirection

        toggleNightMode(appSession.nightModeEnabled)

        binding = DataBindingUtil.setContentView(this, layoutRes())
    }

    override fun toggleNightMode(isEnabled: Boolean) {
        appSession.nightModeEnabled = isEnabled
        val nightMode =
            if (isEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}