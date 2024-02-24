package com.softeer.team6four.ui.mypage.register

import android.Manifest
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentRegisterDescriptionBinding

class RegisterDescriptionFragment : Fragment() {
    private var _binding: FragmentRegisterDescriptionBinding? = null
    private val registerViewModel: RegisterViewModel by activityViewModels()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestLauncher: ActivityResultLauncher<String>
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLauncher()
        with(binding) {
            viewModel = registerViewModel
            lifecycleOwner = viewLifecycleOwner
            btnNext.setOnClickListener {
                registerViewModel.uploadImage()
                findNavController().navigate(R.id.action_registerDescriptionFragment_to_registerTimeFragment)
            }
            ivChargerImage.setOnClickListener { openGallery() }
        }
    }

    private fun setLauncher() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
                val data = activityResult.data
                data?.let {
                    val selectedImageUri = data.data
                    if (selectedImageUri != null) {
                        val realPath = getRealPathFromUri(selectedImageUri)
                        registerViewModel.updateImgUrl(selectedImageUri.toString())
                        realPath?.let { path ->
                            registerViewModel.updateRealPath(path)
                        }
                        Log.d("realPath", realPath.toString())
                    }
                }
            }
        requestLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                showAlertDialog()
            }
        }
    }

    private fun showAlertDialog() {
        if (shouldShowRequestPermissionRationale(getImagePermission())) {
            AlertDialog.Builder(requireContext())
                .setMessage(resources.getString(R.string.alert_dialog_title))
                .create()
                .show()
        } else {
            requestLauncher.launch(getImagePermission())
        }
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private fun getImagePermission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    private fun getRealPathFromUri(uri: Uri): String? {
        var realPath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? =
            requireContext().contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex: Int = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            realPath = it.getString(columnIndex)
        }
        return realPath
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}