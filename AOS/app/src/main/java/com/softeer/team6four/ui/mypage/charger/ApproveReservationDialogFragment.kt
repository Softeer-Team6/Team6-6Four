package com.softeer.team6four.ui.mypage.charger

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentApproveReservationDialogBinding

class ApproveReservationDialogFragment : DialogFragment() {
    private var _binding: FragmentApproveReservationDialogBinding? = null
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
            .setBackgroundInsetTop(41)
            .setBackgroundInsetBottom(41)
        builder.setView(binding.root)

        binding.btnRejectReservation.setOnClickListener { requireDialog().dismiss() }
        binding.btnApproveReservation.setOnClickListener { requireDialog().dismiss() }

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