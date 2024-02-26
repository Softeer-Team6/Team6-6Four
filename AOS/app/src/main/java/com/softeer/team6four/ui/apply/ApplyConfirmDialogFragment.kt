package com.softeer.team6four.ui.apply

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentApplyConfirmDialogBinding


class ApplyConfirmDialogFragment(private val navigationCallback: () -> Unit) : DialogFragment() {
    private var _binding: FragmentApplyConfirmDialogBinding? = null
    private val applyViewModel: ApplyViewModel by activityViewModels()
    private val binding
        get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentApplyConfirmDialogBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setBackground(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.background_upload_dialog
                )
            )
        builder.setView(binding.root)
        binding.tvApplyConfirmTime.text =
            getString(
                R.string.apply_confirm_time,
                String.format("%2d", applyViewModel.startTime.value),
                String.format("%2d", applyViewModel.endTime.value)
            )
            
        binding.btnApplyConfirmApply.setOnClickListener {
            applyViewModel.applyReservation()
            dismiss()
            navigationCallback.invoke()
        }
        binding.btnApplyConfirmBack.setOnClickListener { dismiss() }
        return builder.create()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "ApplyConfirmDialogFragment"
    }

}