package com.softeer.team6four.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.softeer.team6four.R
import com.softeer.team6four.databinding.BottomSheetDialogBinding

class BottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: BottomSheetDialogBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvChargerNumber.setOnClickListener { //Test Code
            dismiss()
            requireParentFragment().findNavController()
                .navigate(R.id.action_homeFragment_to_applyDetailFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}