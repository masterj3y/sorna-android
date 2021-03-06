package io.github.masterj3y.sorna.feature.ad.details

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.skydoves.bundler.bundle
import com.skydoves.bundler.intentOf
import dagger.hilt.android.AndroidEntryPoint
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.extension.invisible
import io.github.masterj3y.sorna.core.extension.visible
import io.github.masterj3y.sorna.core.platform.BaseActivity
import io.github.masterj3y.sorna.databinding.ActivityAdDetailsBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class AdDetailsActivity : BaseActivity<ActivityAdDetailsBinding>() {

    private val model: AdDetailsViewModel by viewModels()

    private val adId: String? by bundle(AD_ID_KEY)

    override fun layoutRes() = R.layout.activity_ad_details

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adId?.let { adId ->
            binding.apply {
                lifecycleOwner = this@AdDetailsActivity
                adapter = AdPicturesPagerAdapter()
                viewModel = model.apply { getAd(adId) }
                save.setOnClickListener { model.save(adId) }
                waste.setOnClickListener { model.waste(adId) }
                Handler(Looper.getMainLooper()).postDelayed({
                    loadingLayout.invisible()
                    call.visible()
                }, 700)
            }
        }
    }

    companion object {

        private const val AD_ID_KEY = "key:ad"

        fun start(context: Context, adId: String) {
            context.intentOf<AdDetailsActivity> {
                +(AD_ID_KEY to adId)
                startActivity(context)
            }
        }
    }
}