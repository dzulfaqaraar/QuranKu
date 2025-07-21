package com.dzulfaqar.quranku.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dzulfaqar.quranku.R
import com.dzulfaqar.quranku.databinding.ItemJuzBinding
import com.dzulfaqar.quranku.core.utils.BaseAdapter
import com.dzulfaqar.quranku.model.JuzModel

class JuzAdapter : BaseAdapter<JuzModel, JuzAdapter.ListViewHolder>(
    areItemsTheSame = { old, new -> old.number == new.number }
) {
    var onItemClick: ((JuzModel) -> Unit)? = null

    override val layoutId: Int = R.layout.item_juz
    override fun viewHolder(view: View) = ListViewHolder(view)
    override fun bindItems(holder: ListViewHolder, item: JuzModel, position: Int) =
        holder.bind(item)

    inner class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemJuzBinding = ItemJuzBinding.bind(view)

        fun bind(data: JuzModel) {
            with(binding) {
                juzNumber.text = data.number.toString()
                juzName.text = String.format("Juz %s", data.number)
                juzSummary.text =
                    String.format("%s Surah, %s Ayat", data.totalSurat, data.totalAyat)
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(getItem(adapterPosition))
            }
        }
    }
}
