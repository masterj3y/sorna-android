package io.github.masterj3y.sorna.feature.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.platform.BaseFragment
import io.github.masterj3y.sorna.databinding.FragmentCategoriesBinding
import io.github.masterj3y.sorna.feature.categories.categorized_ads.CategorizedAdsFragment

@AndroidEntryPoint
class CategoriesFragment : BaseFragment<FragmentCategoriesBinding>(R.layout.fragment_categories) {

    private val model: CategoriesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            adapter = CategoriesAdapter(::onItemClickedListener)
            viewModel = model
            recyclerView.addItemDecoration(itemDecoration)
        }
    }

    private fun onItemClickedListener(item: Category) =
            pushFragment(CategorizedAdsFragment.instance(item.id))
}