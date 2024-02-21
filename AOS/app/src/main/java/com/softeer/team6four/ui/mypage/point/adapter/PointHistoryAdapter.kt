package com.softeer.team6four.ui.mypage.point.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.softeer.team6four.data.remote.payment.model.PointHistoryDetailModel
import com.softeer.team6four.databinding.PointHistoryItemBinding

class PointHistoryAdapter()
    : RecyclerView.Adapter<PointHistoryAdapter.ViewHolder>() {

    private var pointHistoryList: List<PointHistoryDetailModel> = emptyList()

    fun setPointHistoryList(pointHistoryList: List<PointHistoryDetailModel>) {
        this.pointHistoryList = pointHistoryList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PointHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pointHistoryList[position])
    }

    override fun getItemCount(): Int = pointHistoryList.size

    class ViewHolder(private val binding: PointHistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(detailModel: PointHistoryDetailModel) {
            binding.tvPointChargeDate.text = detailModel.createdDate
            binding.tvChargerTitle.text = detailModel.pointTitle
            binding.tvChargerPrice.text = detailModel.amount.toString()
        }
    }
}
