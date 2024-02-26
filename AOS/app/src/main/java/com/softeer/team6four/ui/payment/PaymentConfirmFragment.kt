package com.softeer.team6four.ui.payment

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentPaymentConfirmBinding

class PaymentConfirmFragment() : DialogFragment() {
    private var _binding: FragmentPaymentConfirmBinding? = null
    private val reservationViewModel: PaymentViewModel by activityViewModels()
    private val binding
        get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentPaymentConfirmBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setBackground(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.background_upload_dialog
                )
            )
        builder.setView(binding.root)

        binding.btnDeleteDialogCancel.setOnClickListener { dismiss() }
        binding.btnDeleteDialogConfirm.setOnClickListener {
            reservationViewModel.fetchPaymentInfoModel()
            dismiss()
        }
        return builder.create()
    }

    companion object {
        const val TAG = "PaymentConfirmFragment"
    }
}