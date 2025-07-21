package com.dzulfaqar.quranku.core.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    areItemsTheSame: (old: T, new: T) -> Boolean
) : ListAdapter<T, VH>(GenericDiffUtil(areItemsTheSame)) {

    @get:LayoutRes
    protected abstract val layoutId: Int
    protected abstract fun viewHolder(view: View): VH
    protected abstract fun bindItems(holder: VH, item: T, position: Int)

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item: T = getItem(position)
        bindItems(holder, item, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return viewHolder(view)
    }
}

class GenericDiffUtil<T : Any>(
    private val areItemsTheSameCallback: (old: T, new: T) -> Boolean
) : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        areItemsTheSameCallback(oldItem, newItem)

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = newItem == oldItem
}
