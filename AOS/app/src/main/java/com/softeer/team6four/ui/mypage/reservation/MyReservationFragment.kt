package com.softeer.team6four.ui.mypage.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentMyReservationBinding
import com.softeer.team6four.ui.mypage.reservation.adapter.ReservationHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyReservationFragment : Fragment() {
    private var _binding: FragmentMyReservationBinding? = null
    private val myReservationViewModel: MyReservationViewModel by viewModels()
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyReservationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewModel = myReservationViewModel
            lifecycleOwner = viewLifecycleOwner

            var count = 0;

            val adapter = ReservationHistoryAdapter()
            binding.rvReservationList.adapter = adapter

            val layoutManager = LinearLayoutManager(context)
            binding.rvReservationList.layoutManager = layoutManager

            var sortType = "WAIT"
            myReservationViewModel.fetchMyReservationHistory(sortType)
            binding.rvReservationList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = recyclerView.adapter!!.itemCount-1

                    if (!binding.rvReservationList.canScrollVertically(1)
                        && lastVisibleItemPosition == itemTotalCount
                        && !myReservationViewModel.isLoading.value) {
                        count++
                        myReservationViewModel.fetchMyReservationHistory(sortType, adapter.getLastReservationHistoryId())
                    }
                }
            })

            reservationStateChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
                val checkedId = checkedIds[0]
                sortType = when (checkedId) {
                    R.id.btn_reservation_wait -> "WAIT"
                    R.id.btn_reservation_approve -> "APPROVE"
                    R.id.btn_reservation_reject -> "REJECT"
                    else -> "WAIT"
                }
                adapter.clearReservationHistoryList()
                myReservationViewModel.fetchMyReservationHistory(sortType)

                binding.rvReservationList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        val lastVisibleItemPosition =
                            (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                        val itemTotalCount = recyclerView.adapter!!.itemCount-1

                        if (!binding.rvReservationList.canScrollVertically(1)
                            && lastVisibleItemPosition == itemTotalCount
                            && !myReservationViewModel.isLoading.value) {
                            count++
                            myReservationViewModel.fetchMyReservationHistory(sortType, adapter.getLastReservationHistoryId())
                        }
                    }
                })
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    myReservationViewModel.myReservationHistory.collect { reservationHistoryList ->
                        if (reservationHistoryList.isEmpty() && count == 0) {
                            binding.rvReservationList.visibility = View.GONE
                            binding.ivEmptyState.visibility = View.VISIBLE
                            binding.tvEmptyPointHistory.visibility = View.VISIBLE
                        } else {
                            adapter.setReservationHistoryList(reservationHistoryList)
                            if (reservationHistoryList.isNotEmpty() && reservationHistoryList.last().reservationId != 0) {
                                adapter.removeLoadingFooter()
                            }
                            binding.rvReservationList.visibility = View.VISIBLE
                            binding.ivEmptyState.visibility = View.GONE
                            binding.tvEmptyPointHistory.visibility = View.GONE
                        }
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