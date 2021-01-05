package io.github.masterj3y.sorna.feature.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.extension.addDividerItemDecoration
import io.github.masterj3y.sorna.core.platform.BaseFragment
import io.github.masterj3y.sorna.databinding.FragmentCategoriesBinding
import io.github.masterj3y.sorna.feature.categories.categorized_ads.CategorizedAdsFragment

@AndroidEntryPoint
class CategoriesFragment : BaseFragment<FragmentCategoriesBinding>(R.layout.fragment_categories) {

    private val model: CategoriesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            adapter = CategoriesAdapter(::onItemClickedListener)
            viewModel = model.apply { getCategories() }
            recyclerView.addDividerItemDecoration()
            refreshLayout.setOnRefreshListener { model.getCategories() }
            retry.setOnClickListener { model.getCategories() }
        }
    }

    private fun onItemClickedListener(item: Category) =
            pushFragment(CategorizedAdsFragment.instance(item.id, item.title))
}