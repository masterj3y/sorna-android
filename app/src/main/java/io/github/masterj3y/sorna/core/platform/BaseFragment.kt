package io.github.masterj3y.sorna.core.platform

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ncapdevi.fragnav.FragNavController
import io.github.masterj3y.sorna.R

abstract class BaseFragment<B : ViewDataBinding>(@LayoutRes private val layoutRes: Int) :
    Fragment(layoutRes) {

    lateinit var binding: B

    private var permissionCallBack: (granted: Boolean) -> Unit = {}

    private lateinit var fragmentNavigation: FragmentNavigation
    private val fragNavController: FragNavController by lazy {
        fragmentNavigation.getFragNavigationController()
    }

    private lateinit var nightModeCallback: NightModeCallback

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION) {
            permissionCallBack.invoke(
                grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            )
        }
    }

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

    fun pushFragment(fragment: Fragment) {
        fragNavController.pushFragment(fragment)
    }

    fun grantedStoragePermission(callBack: (granted: Boolean) -> Unit = {}) {
        this.permissionCallBack = callBack
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                AlertDialog.Builder(context)
                    .setTitle(getString(R.string.permission))
                    .setMessage(getString(R.string.needStoragePermission))
                    .setPositiveButton(getString(R.string.grantPermission)) { _, _ ->
                        requestPermissions(
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            STORAGE_PERMISSION
                        )
                    }
                    .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                        permissionCallBack.invoke(false)
                    }
                    .show()
                return
            }
        }
        permissionCallBack.invoke(true)
    }

    fun toggleNightMode(isEnabled: Boolean) = nightModeCallback.toggleNightMode(isEnabled)

    interface FragmentNavigation {
        fun getFragNavigationController(): FragNavController
    }

    interface NightModeCallback {
        fun toggleNightMode(isEnabled: Boolean)
    }

    companion object {
        private const val STORAGE_PERMISSION = 55
    }

}