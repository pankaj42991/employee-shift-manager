package com.pktech.newapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pktech.newapp.databinding.ItemDashboardCardBinding

class DashboardAdapter(
    private val items: List<DashboardItem>
) : RecyclerView.Adapter<DashboardAdapter.VH>() {

    inner class VH(val binding: ItemDashboardCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemDashboardCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.binding.tvTitle.text = item.title
        holder.binding.imgIcon.setImageResource(item.icon)
    }

    override fun getItemCount() = items.size
}
