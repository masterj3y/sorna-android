package io.github.masterj3y.sorna.core.platform

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ncapdevi.fragnav.FragNavController
import io.github.masterj3y.sorna.R

abstract class BaseFragment<B : ViewDataBinding>(@LayoutRes private val layoutRes: Int) :
        Fragment(layoutRes) {

    lateinit var binding: B

    private lateinit var fragmentNavigation: FragmentNavigation
    private val fragNavController: FragNavController
        get() = fragmentNavigation.getFragNavigationController()

    private lateinit var nightModeCallback: NightModeCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is FragmentNavigation) {
            fragmentNavigation = context
        } else throw IllegalArgumentException("Activity must be an instance of BaseFragment.FragmentNavigation")

        if (context is NightModeCallback)
            nightModeCallback = context
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, layoutRes, container, false)
        return binding.root
    }

    fun pushFragment(fragment: Fragment) =
            fragNavController.pushFragment(fragment)

    fun switchTab(tab: Int) = fragmentNavigation.switchTab(tab)

    fun toggleNightMode(isEnabled: Boolean) = nightModeCallback.toggleNightMode(isEnabled)

    interface FragmentNavigation {
        fun getFragNavigationController(): FragNavController
        fun switchTab(tab: Int)
    }

    interface NightModeCallback {
        fun toggleNightMode(isEnabled: Boolean)
    }

}