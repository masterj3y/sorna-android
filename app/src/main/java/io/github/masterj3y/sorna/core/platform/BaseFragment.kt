package io.github.masterj3y.sorna.core.platform

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ncapdevi.fragnav.FragNavController

abstract class BaseFragment<B : ViewDataBinding>(@LayoutRes private val layoutRes: Int) :
    Fragment(layoutRes) {

    lateinit var binding: B

    private lateinit var fragmentNavigation: FragmentNavigation
    private val fragNavController: FragNavController by lazy {
        fragmentNavigation.getFragNavigationController()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is FragmentNavigation) {
            fragmentNavigation = context
        } else throw IllegalArgumentException("Activity must be an instance of BaseFragment.FragmentNavigation")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, layoutRes, container, false)
        return binding.root
    }

    fun pushFragment(fragment: Fragment)=
        fragNavController.pushFragment(fragment)

    fun switchTab(tab: Int) = fragNavController.switchTab(tab)

    interface FragmentNavigation {
        fun getFragNavigationController(): FragNavController
    }

    companion object {
        private const val STORAGE_PERMISSION = 55
    }

}