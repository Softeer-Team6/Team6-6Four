package com.softeer.team6four.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.softeer.team6four.data.remote.charger.model.BottomSheetChargerModel
import com.softeer.team6four.databinding.ChargerItemBinding

class BottomSheetListAdapter :
    ListAdapter<BottomSheetChargerModel, BottomSheetListAdapter.BottomSheetViewHolder>(diffUtil) {
    override fun onBindViewHolder(
        holder: BottomSheetViewHolder,
        position: Int
    ) {
        holder.bind(currentList[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetViewHolder {
        return BottomSheetViewHolder(
            ChargerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    class BottomSheetViewHolder(private val binding: ChargerItemBinding) :
        ViewHolder(binding.root) {
        fun bind(chargerModel: BottomSheetChargerModel) {
            binding.model = chargerModel
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<BottomSheetChargerModel>() {
            override fun areContentsTheSame(
                oldItem: BottomSheetChargerModel,
                newItem: BottomSheetChargerModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(
                oldItem: BottomSheetChargerModel,
                newItem: BottomSheetChargerModel
            ): Boolean {
                return oldItem.chargerId == newItem.chargerId
            }
        }
    }
}