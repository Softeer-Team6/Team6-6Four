package com.softeer.team6four.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.softeer.team6four.R
import com.softeer.team6four.databinding.BottomSheetDialogBinding
import com.softeer.team6four.ui.apply.ApplyViewModel

class BottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: BottomSheetDialogBinding? = null
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val applyViewModel: ApplyViewModel by activityViewModels()
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
        with(binding) {
            viewModel = homeViewModel
            lifecycleOwner = viewLifecycleOwner
            tvChargerNumber.setOnClickListener { //Test Code
                dismiss()
                requireParentFragment().findNavController()
                    .navigate(R.id.action_homeFragment_to_applyDetailFragment)
            }
            rvBottomSheetChargerList.adapter =
                BottomSheetListAdapter { id ->
                    homeViewModel.updateSelectedCharger(id)
                    applyViewModel.updateSelectedChargerId(id)
                    homeViewModel.updateSelectedCharger()
                    dismiss()
                    findNavController().navigate(R.id.action_homeFragment_to_applyDetailFragment)
                }

            orderChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
                Log.d("checkedIds.size", checkedIds.size.toString())
                if (checkedIds.size == 0) {
                    homeViewModel.fetchBottomSheetChargerList()
                } else {
                    if (checkedIds[0] == R.id.order_by_speed) {
                        homeViewModel.fetchBottomSheetChargerList("SPEED")
                    } else {
                        homeViewModel.fetchBottomSheetChargerList("PRICE")
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}