package com.softeer.team6four.ui.mypage.charger

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentApproveReservationDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ApproveReservationDialogFragment : DialogFragment() {
    private var _binding: FragmentApproveReservationDialogBinding? = null
    private val approveReservationDialogViewModel: ApproveReservationDialogViewModel by activityViewModels()
    private val myChargerReservationViewModel: MyChargerReservationViewModel by activityViewModels()
    var callback: ReservationUpdateCallback? = null
    private val binding
        get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentApproveReservationDialogBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setBackground(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.background_upload_dialog
                )
            )
        builder.setView(binding.root)

        lifecycleScope.launch {
            approveReservationDialogViewModel.reservationDetail.collect { reservationDetail ->
                binding.tvConfirmReservationBody.text = reservationDetail.guestNickname + "님이 카밥 대여 요청을 보냈어요!"
                binding.tvReservationDate.text = "대여 날짜 : " + reservationDetail.rentalDate
                binding.tvReservationTime.text = "대여 시간 : " + reservationDetail.returnTime
                binding.tvReservationPrice.text = "대여 비용 : " + reservationDetail.totalFee.toString() + "원"
            }
        }

        val reservationId = approveReservationDialogViewModel.reservationId.value

        binding.btnRejectReservation.setOnClickListener {
            approveReservationDialogViewModel.updateReservationState(reservationId, "REJECT")
            (parentFragment as? ReservationUpdateCallback)?.onReservationUpdated(true, reservationId)
            requireDialog().dismiss()
        }
        binding.btnApproveReservation.setOnClickListener {
            myChargerReservationViewModel.updateReservationState(reservationId, "APPROVE")
            (parentFragment as? ReservationUpdateCallback)?.onReservationUpdated(true, reservationId)
            requireDialog().dismiss()
        }

        return builder.create()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "ApproveReservationDialogFragment"
    }
}
