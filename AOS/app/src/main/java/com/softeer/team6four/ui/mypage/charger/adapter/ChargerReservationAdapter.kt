package com.softeer.team6four.ui.mypage.charger.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.softeer.team6four.data.remote.reservation.model.ChargerReservationInfoModel
import com.softeer.team6four.databinding.MyChargerReservationItemBinding
import com.softeer.team6four.databinding.ProgressBarItemBinding

data class ReservationDetail(
    val reservationId: Int = 0,
    val guestNickname: String = "",
    val rentalDate: String = "",
    val rentalTime: String = "",
    val totalFee: Int = 0
)

class ChargerReservationAdapter(private val navigationCallback: (reservationDetail : ReservationDetail)  -> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private val chargerReservationInfoList = ArrayList<ChargerReservationInfoModel>()

    inner class ChargerReservationViewHolder(private val binding: MyChargerReservationItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(detailModel: ChargerReservationInfoModel) {
            binding.tvReservationApplicantNickname.text = detailModel.guestNickname
            binding.tvReservationDatetime.text = detailModel.rentalDate + " | " + detailModel.rentalTime
            binding.tvReservationPrice.text = detailModel.totalFee.toString() + "원"

            binding.root.setOnClickListener {
                navigationCallback(ReservationDetail(detailModel.reservationId, detailModel.guestNickname, detailModel.rentalDate, detailModel.rentalTime, detailModel.totalFee))
            }
        }
    }

    inner class LoadingViewHolder(private val binding: ProgressBarItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun getItemViewType(position: Int): Int {
        return when (chargerReservationInfoList[position].reservationId)
        {
            0 -> VIEW_TYPE_LOADING
            else -> VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MyChargerReservationItemBinding.inflate(layoutInflater, parent, false)
                ChargerReservationViewHolder(binding)
            }
            else -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProgressBarItemBinding.inflate(layoutInflater, parent, false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChargerReservationViewHolder -> {
                holder.bind(chargerReservationInfoList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return chargerReservationInfoList.size
    }

    fun setChargerReservationList(chargerReservationList: List<ChargerReservationInfoModel>) {
        if (chargerReservationInfoList.isNotEmpty() && chargerReservationInfoList.last().reservationId == 0) {
            chargerReservationInfoList.removeAt(chargerReservationInfoList.size - 1)
        }
        chargerReservationInfoList.addAll(chargerReservationList)
        notifyDataSetChanged()
    }

    fun removeLoadingFooter() {
        if (chargerReservationInfoList.isNotEmpty() && chargerReservationInfoList.last().reservationId == 0) {
            chargerReservationInfoList.removeAt(chargerReservationInfoList.size - 1)
            notifyItemRemoved(chargerReservationInfoList.size)
        }
    }

    fun clearChargerReservationList() {
        chargerReservationInfoList.clear()
        notifyDataSetChanged()
    }
}
