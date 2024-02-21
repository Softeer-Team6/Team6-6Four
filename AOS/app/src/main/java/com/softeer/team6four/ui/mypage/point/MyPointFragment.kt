package com.softeer.team6four.ui.mypage.point

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.softeer.team6four.databinding.FragmentMyPointBinding
import com.softeer.team6four.ui.mypage.point.adapter.PointHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPointFragment : Fragment() {
    private var _binding: FragmentMyPointBinding? = null
    private val myPointViewModel: MyPointViewModel by viewModels()
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMyPointBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ibPointCharge.setOnClickListener {
            myPointViewModel.chargePoint()
        }

        with(binding) {
            myPointViewModel.fetchMyTotalPoint()
            viewModel = myPointViewModel
            lifecycleOwner = viewLifecycleOwner

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    myPointViewModel.myTotalPoint.collect {
                        tvTotalPointsValue.text = it.toString()
                    }
                }
            }

            val adapter = PointHistoryAdapter()
            binding.rvPointHistory.adapter = adapter

            myPointViewModel.fetchPointHistory()

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    myPointViewModel.pointHistory.collect { pointHistoryList ->
                        if (pointHistoryList.isEmpty()) {
                            binding.rvPointHistory.visibility = View.GONE
                            binding.ivEmptyState.visibility = View.VISIBLE
                            binding.tvEmptyPointHistory.visibility = View.VISIBLE
                        } else {
                            adapter.setPointHistoryList(pointHistoryList)
                            binding.rvPointHistory.visibility = View.VISIBLE
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