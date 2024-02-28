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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softeer.team6four.data.Resource
import com.softeer.team6four.databinding.FragmentMyPointBinding
import com.softeer.team6four.ui.mypage.point.adapter.PointHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPointFragment : Fragment() {
    private var _binding: FragmentMyPointBinding? = null
    private val myPointViewModel: MyPointViewModel by viewModels()
    private lateinit var historyAdapter: PointHistoryAdapter
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
            myPointViewModel.updateRefreshState(true)
            myPointViewModel.updateIsFinishState(false)
            myPointViewModel.chargePoint()
        }

        with(binding) {
            viewModel = myPointViewModel
            lifecycleOwner = viewLifecycleOwner

            myPointViewModel.fetchMyTotalPoint()
            myPointViewModel.fetchPointHistory()

            ibBack.setOnClickListener {
                findNavController().popBackStack()
            }

            historyAdapter = PointHistoryAdapter()
            binding.rvPointHistory.adapter = historyAdapter

            setListScrollCallback()
            setUpdateList()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setListScrollCallback() {
        binding.rvPointHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = historyAdapter.itemCount - 1


                if (!binding.rvPointHistory.canScrollVertically(1)
                    && lastVisibleItemPosition == itemTotalCount
                    && !myPointViewModel.isFinish.value
                    && myPointViewModel.pointHistory.value is Resource.Success
                ) {
                    if (itemTotalCount == 0) {
                        myPointViewModel.fetchPointHistory()
                    } else {
                        myPointViewModel.fetchPointHistory(historyAdapter.getLastPointHistoryId())
                    }
                }
            }
        })
    }

    private fun setUpdateList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                myPointViewModel.pointHistory.collect { pointHistoryList ->
                    if (pointHistoryList is Resource.Loading) {
                        historyAdapter.setProgressbar(myPointViewModel.refreshState.value)
                        myPointViewModel.updateRefreshState(false)
                    } else if (pointHistoryList is Resource.Success) {
                        historyAdapter.removeLoadingFooter()
                        historyAdapter.setPointHistoryList(pointHistoryList.data.content)
                        myPointViewModel.updateIsFinishState(!pointHistoryList.data.hasNext)
                        if (historyAdapter.itemCount == 0) {
                            binding.rvPointHistory.visibility = View.VISIBLE
                            binding.ivEmptyState.visibility = View.VISIBLE
                            binding.tvEmptyPointHistory.visibility = View.VISIBLE
                        } else {
                            binding.rvPointHistory.visibility = View.VISIBLE
                            binding.ivEmptyState.visibility = View.INVISIBLE
                            binding.tvEmptyPointHistory.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        }
    }
}