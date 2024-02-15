package com.softeer.team6four.ui.mypage.charger

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentDeleteDialogBinding

class DeleteDialogFragment : DialogFragment() {
    private var _binding: FragmentDeleteDialogBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentDeleteDialogBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setBackground(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.background_upload_dialog
                )
            )
        builder.setView(binding.root)

        binding.btnDeleteDialogCancel.setOnClickListener { dismiss() }
        binding.btnDeleteDialogDelete.setOnClickListener { dismiss() }
        return builder.create()
    }

    companion object {
        const val TAG = "DeleteDialogFragment"
    }
}