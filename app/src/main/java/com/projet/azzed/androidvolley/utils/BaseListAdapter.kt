package com.projet.azzed.androidvolley.utils
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup


/**
 * Created by Yvan RAJAONARIVONY on 17/07/2018
 * Copyright (c) 2018 Nouvonivo
 */
abstract class BaseListAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val placeHolderViewType = 0

    var items = mutableListOf<T>()

    var placeHolderView: View? = null

    abstract fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    abstract fun onBindChildViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == placeHolderViewType) {
            ViewHolder(placeHolderView!!)
        } else {
            onCreateChildViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (items.size > 0) {
            onBindChildViewHolder(holder, position)
        }
    }

    override fun getItemCount(): Int {
        val count = items.size
        if (count == 0 && placeHolderView != null) {
            return 1
        }
        return count
    }

    override fun getItemViewType(position: Int): Int {
        return if (items.size == 0 && placeHolderView != null) placeHolderViewType
        else 1
    }

    open fun addAll(newItems: List<T>?) {
        val count = itemCount
        if (newItems != null) {
            items.addAll(newItems)
            notifyItemRangeInserted(count, newItems.size)
        }
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun remove(item: T) {
        val index = items.indexOf(item)
        items.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    protected class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun withSuffix(count: Long): String {
        if (count < 1000) return "" + count
        val exp = (Math.log(count.toDouble()) / Math.log(1000.0)).toInt()
        return String.format("%.1f %c",
                count / Math.pow(1000.0, exp.toDouble()),
                "kMGTPE"[exp - 1])
    }
}