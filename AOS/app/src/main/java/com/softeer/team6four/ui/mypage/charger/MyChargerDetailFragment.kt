package com.softeer.team6four.ui.mypage.charger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.softeer.team6four.databinding.FragmentMyChargerDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyChargerDetailFragment : Fragment() {
    private var _binding: FragmentMyChargerDetailBinding? = null
    private val myChargerDetailViewModel: MyChargerDetailViewModel by activityViewModels()
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyChargerDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewModel = myChargerDetailViewModel
            lifecycleOwner = viewLifecycleOwner

            tvDistanceFromMyLocation.visibility = View.GONE

            myChargerDetailViewModel.fetchMyChargerDetail(myChargerDetailViewModel.chargerId.value)

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    myChargerDetailViewModel.myChargerDetail.collect { chargerDetail ->
                        tvMyChargerSpeed.text = chargerDetail.speedType + " kWh"
                        val times = chargerDetail.selfUseTime.split("~")
                        if (times.size >= 2) {
                            val (startTime, endTime) = times
                            tvMyChargerStartTime.text = startTime
                            tvMyChargerEndTime.text = endTime
                        }
                        tvMyChargerTotalIncome.text = chargerDetail.carbobTotalIncome.toString() + " 원"
                        ivChargerDefaultImage.load(chargerDetail.imageUrl)
                         ivQrCodeImage.load(chargerDetail.qrImageUrl)
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