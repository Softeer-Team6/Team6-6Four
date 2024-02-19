package com.softeer.team6four.ui.mypage.charger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentMyChargerBinding

class MyChargerFragment : Fragment() {
    private var _binding: FragmentMyChargerBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyChargerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnChipStateChanged()
        setMoreButton()
        setBackButton()
    }

    private fun setOnChipStateChanged() {
        binding.chipGroupTap.setOnCheckedStateChangeListener { _, checkedIds ->
            when (checkedIds[0]) {
                R.id.chip_confirm_reservation -> {
                    binding.myChargerFragContainer.getFragment<NavHostFragment>()
                        .findNavController()
                        .navigate(R.id.action_myChargerDetailFragment_to_myChargerReservationFragment)
                }

                R.id.chip_detail_description -> {
                    binding.myChargerFragContainer.getFragment<NavHostFragment>()
                        .findNavController()
                        .navigate(R.id.action_myChargerReservationFragment_to_myChargerDetailFragment)
                }

            }
        }
    }

    private fun setMoreButton() {
        binding.ivMore.setOnClickListener {
            if (binding.myChargerFragContainer.getFragment<NavHostFragment>().findNavController()
                    .currentDestination?.id == R.id.myChargerDetailFragment
            ) {
                EditBottomSheetFragment().show(childFragmentManager, EditBottomSheetFragment.TAG)
            }
        }
    }

    private fun setBackButton() {
        binding.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}