package com.softeer.team6four.ui.mypage.register

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentUploadDialogBinding

class UploadDialogFragment(private val navigationCallback: () -> Unit) : DialogFragment() {
    private var _binding: FragmentUploadDialogBinding? = null
    private val registerViewModel: RegisterViewModel by activityViewModels()

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

        with(binding) {
            btnUpload.setOnClickListener {
                registerViewModel.registerCharger()
                requireDialog().dismiss()
                navigationCallback.invoke()
            }
        }
        chargerInfoBinding()
        return builder.create()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun chargerInfoBinding() {
        with(binding) {
            Log.d("startDateTime", registerViewModel.startDateTime.value.toString())
            btnCancel.setOnClickListener { requireDialog().dismiss() }
            tvChargerNickname.text = registerViewModel.chargerNickname.value
            tvChargerLocation.text = registerViewModel.addressText.value
            tvChargerPrice.text = getString(R.string.price_format, registerViewModel.price.value)
            tvChargerSpeed.text = registerViewModel.speedTypeText.value
            tvChargerType.text = registerViewModel.chargerTypeText.value
            tvChargerInstallType.text = registerViewModel.installTypeText.value
            tvStartTime.text = getString(
                R.string.time_string_format,
                String.format("%02d", registerViewModel.startDateTime.value)
            )
            tvEndTime.text = getString(
                R.string.time_string_format,
                String.format("%02d", registerViewModel.endDateTime.value)
            )
            ivChargerUploadImage.load(
                registerViewModel.imageUrl.value.ifEmpty {
                    R.drawable.charger_default_image
                }
            )
        }
    }

    companion object {
        const val TAG = "UploadDialogFragment"
    }
}