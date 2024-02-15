package com.softeer.team6four.ui.mypage.charger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.softeer.team6four.databinding.FragmentEditBottomSheetBinding

class EditBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentEditBottomSheetBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            tvEdit.setOnClickListener { dismiss() }
            tvDelete.setOnClickListener { dismiss() }
            tvCancel.setOnClickListener { dismiss() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "EditBottomSheetFragment"
    }
}