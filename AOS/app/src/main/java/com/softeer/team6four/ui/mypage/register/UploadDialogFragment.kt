package com.softeer.team6four.ui.mypage.register

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentUploadDialogBinding

class UploadDialogFragment : DialogFragment() {
    private var _binding: FragmentUploadDialogBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentUploadDialogBinding.inflate(layoutInflater)
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
        return builder.create()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}