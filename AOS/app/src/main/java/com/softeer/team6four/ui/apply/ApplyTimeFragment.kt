package com.softeer.team6four.ui.apply

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentApplyTimeBinding

class ApplyTimeFragment : Fragment() {
    private var _binding: FragmentApplyTimeBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApplyTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnApplyReservation.setOnClickListener {
                ApplyConfirmDialogFragment() { findNavController().navigate(R.id.action_applyTimeFragment_to_applyCompleteFragment) }.show(
                    parentFragmentManager, ApplyConfirmDialogFragment.TAG
                )
            }
            ibBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}