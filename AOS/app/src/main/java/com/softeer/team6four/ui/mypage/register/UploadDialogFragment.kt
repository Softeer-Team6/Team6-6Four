package com.softeer.team6four.ui.mypage.register

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentUploadDialogBinding

class UploadDialogFragment(private val navigationCallback: () -> Unit) : DialogFragment() {
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
        builder.setView(binding.root)

        binding.btnUpload.setOnClickListener {
            requireDialog().dismiss()
            navigationCallback.invoke()
        }
        binding.btnCancel.setOnClickListener { requireDialog().dismiss() }
        return builder.create()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "UploadDialogFragment"
    }
}