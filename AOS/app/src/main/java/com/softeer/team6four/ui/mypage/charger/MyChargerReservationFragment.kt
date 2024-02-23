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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softeer.team6four.databinding.FragmentMyChargerReservationBinding
import com.softeer.team6four.ui.mypage.charger.adapter.ChargerReservationAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyChargerReservationFragment : Fragment(), ReservationUpdateCallback {
    private var _binding: FragmentMyChargerReservationBinding? = null
    private val myChargerReservationViewModel: MyChargerReservationViewModel by activityViewModels()
    private val approveReservationDialogViewModel: ApproveReservationDialogViewModel by activityViewModels()
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

        with(binding) {
            viewModel = myChargerReservationViewModel
            lifecycleOwner = viewLifecycleOwner

            var count = 0

            val adapter = ChargerReservationAdapter { reservationDetail ->
                approveReservationDialogViewModel.updateReservationId(reservationDetail.reservationId)
                approveReservationDialogViewModel.updateReservationDetail(
                    reservationDetail.guestNickname,
                    reservationDetail.rentalDate,
                    reservationDetail.rentalTime,
                    reservationDetail.totalFee
                )
                ApproveReservationDialogFragment().show(
                    childFragmentManager,
                    ApproveReservationDialogFragment.TAG
                )
            }
            binding.rvChargerReservation.adapter = adapter

            val layoutManager = LinearLayoutManager(context)
            binding.rvChargerReservation.layoutManager = layoutManager

            adapter.clearChargerReservationList()
            myChargerReservationViewModel.fetchMyChargerReservationList(myChargerReservationViewModel.chargerId.value)

            binding.rvChargerReservation.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = recyclerView.adapter!!.itemCount-1

                    if (!binding.rvChargerReservation.canScrollVertically(1)
                        && lastVisibleItemPosition == itemTotalCount
                        && myChargerReservationViewModel.isLoading.value.not())
                    {
                        count++
                        myChargerReservationViewModel.fetchMyChargerReservationList(
                            myChargerReservationViewModel.chargerId.value,
                            myChargerReservationViewModel.myChargerReservationList.value.lastOrNull()?.reservationId
                        )
                    }
                }
            })

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        myChargerReservationViewModel.myChargerReservationList.collect { chargerReservationList ->
                            if (chargerReservationList.isEmpty() && count == 0) {
                                binding.rvChargerReservation.visibility = View.GONE
                                binding.ivEmptyState.visibility = View.VISIBLE
                                binding.tvEmptyPointHistory.visibility = View.VISIBLE
                            } else {
                                adapter.setChargerReservationList(chargerReservationList)
                                if (chargerReservationList.isNotEmpty() && chargerReservationList.last().reservationId != 0) {
                                    adapter.removeLoadingFooter()
                                }
                                binding.rvChargerReservation.visibility = View.VISIBLE
                                binding.ivEmptyState.visibility = View.GONE
                                binding.tvEmptyPointHistory.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onReservationUpdated(updated: Boolean, reservationId: Int?) {
        if (updated && reservationId != null) {
            val adapter = binding.rvChargerReservation.adapter as ChargerReservationAdapter
            adapter.clearChargerReservationList()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
