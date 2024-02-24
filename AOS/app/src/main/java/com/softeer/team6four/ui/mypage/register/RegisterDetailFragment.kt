package com.softeer.team6four.ui.mypage.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentRegisterDetailBinding

class RegisterDetailFragment : Fragment() {
    private var _binding: FragmentRegisterDetailBinding? = null
    private val registerViewModel: RegisterViewModel by activityViewModels()
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNext.setOnClickListener { findNavController().navigate(R.id.action_registerDetailFragment_to_registerPriceFragment) }

        with(binding) {
            viewModel = registerViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        getSelectedChargerTypeChip()
        getSelectedInstallTypeChip()
        getSelectedSpeedTypeChip()
        getSelectedLocationTypeChip()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getSelectedChargerTypeChip() {
        binding.chipGroupChargerAdapterType.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.size == 0) {
                registerViewModel.updateChargerType("")
                return@setOnCheckedStateChangeListener
            }
            registerViewModel.updateChargerType(
                when (checkedIds[0]) {
                    R.id.chip_house -> "HOUSE"
                    R.id.chip_villa -> "VILLA"
                    R.id.chip_apartment -> "APARTMENT"
                    else -> "ETC"
                }
            )

        }
    }

    private fun getSelectedLocationTypeChip() {
        binding.chipGroupChargerLocationType.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.size == 0) {
                registerViewModel.updateLocationType("")
                return@setOnCheckedStateChangeListener
            }
            registerViewModel.updateLocationType(
                when (checkedIds[0]) {
                    R.id.chip_slow_speed -> "SLOW"
                    R.id.chip_adapter_ac -> "AC3"
                    R.id.chip_destination -> "DESTINATION"
                    else -> "ETC"
                }
            )
        }
    }

    private fun getSelectedSpeedTypeChip() {
        binding.chipGroupChargerSpeed.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.size == 0) {
                registerViewModel.updateSpeedType("")
                return@setOnCheckedStateChangeListener
            }
            registerViewModel.updateSpeedType(
                when (checkedIds[0]) {
                    R.id.chip_speed_3kwh -> "KWH3"
                    R.id.chip_speed_5kWh -> "KWH5"
                    R.id.chip_speed_7kwh -> "KWH7"
                    else -> "KWH11"
                }
            )
        }
    }

    private fun getSelectedInstallTypeChip() {
        binding.chipGroupChargerInstallType.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.size == 0) {
                registerViewModel.updateInstallType("")
                return@setOnCheckedStateChangeListener
            }
            registerViewModel.updateInstallType(
                when (checkedIds[0]) {
                    R.id.chip_outside -> "OUTDOOR"
                    R.id.chip_inside -> "INDOOR"
                    R.id.chip_canopy -> "CANOPY"
                    else -> "ETC"
                }
            )
        }
    }

}