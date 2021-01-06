package io.github.masterj3y.sorna.core.platform

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.github.masterj3y.sorna.core.platform.BaseFragment.*
import io.github.masterj3y.sorna.core.utils.AppSession
import javax.inject.Inject

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity(), NightModeCallback {

    @Inject
    lateinit var appSession: AppSession

    lateinit var binding: B

    abstract fun layoutRes(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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