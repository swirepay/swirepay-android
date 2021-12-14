package com.swirepay.android_sdk.checkout.ui.adapter

import android.widget.TextView

import android.content.Context

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup

import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import com.swirepay.android_sdk.R
import com.swirepay.android_sdk.model.Banks


class ObjectAdapter(context: Context, originalList: List<Banks>) : BaseAdapter(), Filterable {
    private val context: Context = context
    private val originalList: List<Banks> = originalList
    private val suggestions: ArrayList<Banks> = ArrayList()
    private val filter: Filter = CustomFilter()
    override fun getFilter(): Filter {
        return filter
    }

    override fun getCount(): Int {
        return suggestions.size
    }

    override fun getItem(position: Int): Any {
        return suggestions[position].bankName
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView: View?
        val inflater = LayoutInflater.from(context)
        convertView = inflater.inflate(
            R.layout.custom_row,
            parent,
            false
        )
        val holder = ViewHolder()
        holder.autoText = convertView?.findViewById(R.id.itemName)
        holder.autoText!!.text = suggestions[position].bankName
        return convertView
    }

    private class ViewHolder {
        var autoText: TextView? = null
    }

    private inner class CustomFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            suggestions.clear()
            if (originalList != null && constraint != null) {
                for (i in originalList.indices) {
                    if (originalList[i].bankName.toLowerCase()
                            .contains(constraint)
                    ) {
                        suggestions.add(originalList[i])
                    }
                }
            }
            val results =
                FilterResults()
            results.values = suggestions
            results.count = suggestions.size
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            if (results.count > 0) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }
}