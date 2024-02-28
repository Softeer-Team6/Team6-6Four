package com.softeer.team6four.ui.mypage.reservation.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.softeer.team6four.R
import com.softeer.team6four.data.remote.reservation.model.ReservationInfoModel
import com.softeer.team6four.databinding.ProgressBarItemBinding
import com.softeer.team6four.databinding.ReservationHistoryItemBinding

class ReservationHistoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private val reservationHistoryList = ArrayList<ReservationInfoModel>()

    class ReservationHistoryViewHolder(private val binding: ReservationHistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(detailModel: ReservationInfoModel) {
            binding.tvReservationChargerNickname.text = detailModel.carbobNickname

            if (detailModel.stateType == "WAIT") {
                binding.chipReservationState.text = "대기중"
                binding.chipReservationState.chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(binding.root.context, R.color.gray_050))
                binding.chipReservationState.setTextColor(ColorStateList.valueOf(
                    ContextCompat.getColor(binding.root.context, R.color.gray_800)))
            } else if (detailModel.stateType == "APPROVE") {
                binding.chipReservationState.text = "승인"
                binding.chipReservationState.chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(binding.root.context, R.color.main_400))
                binding.chipReservationState.setTextColor(ColorStateList.valueOf(
                    ContextCompat.getColor(binding.root.context, R.color.white)))
            } else if (detailModel.stateType == "USED") {
                binding.chipReservationState.text = "사용완료"
                binding.chipReservationState.chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(binding.root.context, R.color.gray_050))
                binding.chipReservationState.setTextColor(ColorStateList.valueOf(
                    ContextCompat.getColor(binding.root.context, R.color.gray_800)))
            } else if (detailModel.stateType == "REJECT") {
                binding.chipReservationState.text = "거절"
                binding.chipReservationState.chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(binding.root.context, R.color.red))
                binding.chipReservationState.setTextColor(ColorStateList.valueOf(
                    ContextCompat.getColor(binding.root.context, R.color.white)))
            }

            binding.tvReservationTotalFee.text = detailModel.totalFee.toString() + "원"
            binding.tvReservationTime.text = detailModel.reservationTimeStr
            binding.tvChargerLocation.text = detailModel.address
            binding.ivChargerImage.load(detailModel.carbobImageUrl)

        }
    }

    class LoadingViewHolder(private val binding: ProgressBarItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun getItemViewType(position: Int): Int {
        return when (reservationHistoryList[position].reservationId)
        {
            0 -> VIEW_TYPE_LOADING
            else -> VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ReservationHistoryItemBinding.inflate(layoutInflater, parent, false)
                ReservationHistoryViewHolder(binding)
            }
            else -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProgressBarItemBinding.inflate(layoutInflater, parent, false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ReservationHistoryViewHolder) {
            holder.bind(reservationHistoryList[position])
        } else {

        }
    }

    override fun getItemCount(): Int = reservationHistoryList.size

    fun setReservationHistoryList(reservationInfoList: List<ReservationInfoModel>) {
        if (this.reservationHistoryList.isNotEmpty() && this.reservationHistoryList.last().reservationId == 0) {
            this.reservationHistoryList.removeAt(this.reservationHistoryList.size - 1)
        }
        this.reservationHistoryList.addAll(reservationInfoList)
        notifyDataSetChanged()
    }

    fun removeLoadingFooter() {
        if (this.reservationHistoryList.isNotEmpty() && this.reservationHistoryList.last().reservationId == 0) {
            this.reservationHistoryList.removeAt(this.reservationHistoryList.size - 1)
            notifyItemRemoved(this.reservationHistoryList.size)
        }
    }

    fun getLastReservationHistoryId(): Int? {
        return reservationHistoryList.lastOrNull()?.reservationId
    }

    fun clearReservationHistoryList() {
        reservationHistoryList.clear()
        notifyDataSetChanged()
    }
}