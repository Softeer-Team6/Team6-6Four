package com.softeer.team6four.ui.mypage.point.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.softeer.team6four.data.remote.payment.model.PointHistoryDetailModel
import com.softeer.team6four.databinding.PointHistoryItemBinding
import com.softeer.team6four.databinding.ProgressBarItemBinding

class PointHistoryAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private val pointHistoryList = ArrayList<PointHistoryDetailModel>()

    class PointHistoryViewHolder(private val binding: PointHistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(detailModel: PointHistoryDetailModel) {
            binding.tvPointChargeDate.text = detailModel.createdDate
            binding.tvChargerTitle.text = detailModel.pointTitle
            binding.tvChargerPrice.text = detailModel.amount
        }
    }

    class LoadingViewHolder(private val binding: ProgressBarItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun getItemViewType(position: Int): Int {
        Log.i("test", "getItemViewType: ${pointHistoryList[position].paymentId}")

        return when (pointHistoryList[position].paymentId)
        {
            0 -> VIEW_TYPE_LOADING
            else -> VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PointHistoryItemBinding.inflate(layoutInflater, parent, false)
                PointHistoryViewHolder(binding)
            }
            else -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProgressBarItemBinding.inflate(layoutInflater, parent, false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PointHistoryViewHolder) {
            holder.bind(pointHistoryList[position])
        } else {

        }
    }

    override fun getItemCount(): Int = pointHistoryList.size

    fun setPointHistoryList(pointHistoryList: List<PointHistoryDetailModel>) {
        if (this.pointHistoryList.isNotEmpty() && this.pointHistoryList.last().paymentId == 0) {
            this.pointHistoryList.removeAt(this.pointHistoryList.size - 1)
        }
        this.pointHistoryList.addAll(pointHistoryList)
        notifyDataSetChanged()
    }

    fun removeLoadingFooter() {
        if (this.pointHistoryList.isNotEmpty() && this.pointHistoryList.last().paymentId == 0) {
            this.pointHistoryList.removeAt(this.pointHistoryList.size - 1)
            notifyItemRemoved(this.pointHistoryList.size)
        }
    }

    fun getLastPointHistoryId(): Long? {
        return pointHistoryList.lastOrNull()?.paymentId?.toLong()
    }

}
