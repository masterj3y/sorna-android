package io.github.masterj3y.sorna.feature.categories

import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.extension.inflate
import io.github.masterj3y.sorna.core.platform.DiffCallBack
import io.github.masterj3y.sorna.databinding.ItemCategoryBinding

class CategoriesAdapter(private val listener: OnItemClickedListener) : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    private val differ = AsyncListDiffer(this, DiffCallBack<Category>())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            CategoriesViewHolder(
                    ItemCategoryBinding.bind(parent.inflate(R.layout.item_category))
            )

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) =
            holder.render(differ.currentList[position])

    override fun getItemCount() = differ.currentList.size

    fun submitList(newList: List<Category>) =
            differ.submitList(newList)

    inner class CategoriesViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun render(item: Category) = with(binding) {
            category = item
            root.setOnClickListener { listener.onItemClicked(getItem())}
        }

        private fun getItem(): Category = differ.currentList[adapterPosition]
    }

    fun interface OnItemClickedListener {
        fun onItemClicked(category: Category)
    }
}

@BindingAdapter("android:bindCategoriesList")
fun bindCategoriesList(view: RecyclerView, list: List<Category>?) {
    list?.let {
        (view.adapter as? CategoriesAdapter)?.submitList(it)
    }
}