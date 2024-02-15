package com.softeer.team6four.ui.mypage.charger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.softeer.team6four.databinding.FragmentMyChargerReservationBinding

class MyChargerReservationFragment : Fragment() {
    private var _binding: FragmentMyChargerReservationBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyChargerReservationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setOnClickListener { //Test Code
            ApproveReservationDialogFragment().show(
                childFragmentManager,
                ApproveReservationDialogFragment.TAG
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}