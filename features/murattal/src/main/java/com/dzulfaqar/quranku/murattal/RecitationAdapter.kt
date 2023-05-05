package com.dzulfaqar.quranku.murattal

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dzulfaqar.quranku.R
import com.dzulfaqar.quranku.databinding.ItemRecitationBinding
import com.dzulfaqar.quranku.core.utils.BaseAdapter
import com.dzulfaqar.quranku.model.ReciterModel

class RecitationAdapter(
    private val callbackList: (ReciterModel) -> Unit
) : BaseAdapter<ReciterModel, RecitationAdapter.ListViewHolder>(
    areItemsTheSame = { old, new -> old.id == new.id }
) {
    override val layoutId: Int = R.layout.item_recitation
    override fun viewHolder(view: View) = ListViewHolder(view)
    override fun bindItems(holder: ListViewHolder, item: ReciterModel, position: Int) =
        holder.bind(item)

    inner class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemRecitationBinding by viewBinding()

        fun bind(data: ReciterModel) {
            with(binding) {
                if (data.style != null) {
                    recitationName.text = String.format("%s - %s", data.name, data.style)
                } else {
                    recitationName.text = String.format("%s", data.name)
                }

                root.setOnClickListener {
                    callbackList(data)
                }
            }
        }
    }
}
