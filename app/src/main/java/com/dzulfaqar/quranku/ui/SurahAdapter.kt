package com.dzulfaqar.quranku.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dzulfaqar.quranku.R
import com.dzulfaqar.quranku.databinding.ItemSurahBinding
import com.dzulfaqar.quranku.core.utils.BaseAdapter
import com.dzulfaqar.quranku.model.SurahModel

class SurahAdapter : BaseAdapter<SurahModel, SurahAdapter.ListViewHolder>(
    areItemsTheSame = { old, new -> old.id == new.id }
) {
    var onItemClick: ((SurahModel) -> Unit)? = null

    override val layoutId: Int = R.layout.item_surah
    override fun viewHolder(view: View) = ListViewHolder(view)
    override fun bindItems(holder: ListViewHolder, item: SurahModel, position: Int) =
        holder.bind(item)

    inner class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemSurahBinding by viewBinding()

        fun bind(data: SurahModel) {
            with(binding) {
                surahNumber.text = data.id.toString()
                surahName.text = data.name
                arabic.text = data.arabic
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(getItem(bindingAdapterPosition))
            }
        }
    }
}
