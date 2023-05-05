package com.dzulfaqar.quranku.ui

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dzulfaqar.quranku.R
import com.dzulfaqar.quranku.databinding.ItemAyatBinding
import com.dzulfaqar.quranku.core.utils.BaseAdapter
import com.dzulfaqar.quranku.model.AyatModel

class AyatAdapter(
    private val callbackBookmark: (AyatModel, Boolean) -> Unit
) : BaseAdapter<AyatModel, AyatAdapter.ListViewHolder>(
    areItemsTheSame = { old, new -> old.id == new.id }
) {
    override val layoutId: Int = R.layout.item_ayat
    override fun viewHolder(view: View) = ListViewHolder(view)
    override fun bindItems(holder: ListViewHolder, item: AyatModel, position: Int) =
        holder.bind(item)

    fun getSwipedData(swipedPosition: Int): AyatModel = getItem(swipedPosition)

    inner class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemAyatBinding by viewBinding()

        fun bind(data: AyatModel) {
            with(binding) {
                surahNumber.text = data.ayatNumber.toString()
                surahName.text = data.surahName
                arabic.text = data.arabic

                bookmark.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        if (data.isBookmark) R.drawable.ic_bookmark
                        else R.drawable.ic_bookmark_border
                    )
                )

                bookmark.setOnClickListener {
                    if (data.isBookmark) callbackBookmark(data, false)
                    else callbackBookmark(data, true)
                }
            }
        }
    }
}
