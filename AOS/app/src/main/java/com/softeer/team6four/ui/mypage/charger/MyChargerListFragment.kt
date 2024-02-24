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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentMyChargerListBinding
import com.softeer.team6four.ui.mypage.charger.adapter.MyChargerListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyChargerListFragment : Fragment() {
    private var _binding : FragmentMyChargerListBinding? = null
    private val myChargerListViewModel: MyChargerListViewModel by activityViewModels()
    private val myChargerViewModel: MyChargerViewModel by activityViewModels()
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyChargerListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackButton()
        //Test Code
        binding.ibFilter.setOnClickListener { myChargerListViewModel.updateFilterState() }
        binding.tvCurrentFilter.setOnClickListener { myChargerListViewModel.updateFilterState() }

        with(binding) {
            viewModel = myChargerListViewModel
            lifecycleOwner = viewLifecycleOwner

            var count = 0;

            val adapter = MyChargerListAdapter { idAndNickname ->
                myChargerViewModel.updateChargerId(idAndNickname.first)
                myChargerViewModel.updateChargerNickname(idAndNickname.second)
                findNavController().navigate(R.id.action_myChargerListFragment_to_myChargerFragment)
            }
            binding.rvMyChargerList.adapter = adapter

            val layoutManager = LinearLayoutManager(context)
            binding.rvMyChargerList.layoutManager = layoutManager

            adapter.clearMyChargerList()
            myChargerListViewModel.fetchMyChargerList(myChargerListViewModel.filterState.value)

            binding.rvMyChargerList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = recyclerView.adapter!!.itemCount-1

                    if (!binding.rvMyChargerList.canScrollVertically(1)
                        && lastVisibleItemPosition == itemTotalCount
                        && !myChargerListViewModel.isLoading.value) {
                        count++
                        myChargerListViewModel.fetchMyChargerList(
                            myChargerListViewModel.filterState.value,
                            adapter.getLastChargerIdAndReservationCount().first,
                            adapter.getLastChargerIdAndReservationCount().second
                        )
                    }
                }
            })

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    myChargerListViewModel.filterState.collect { filterState ->
                        adapter.clearMyChargerList()
                        myChargerListViewModel.fetchMyChargerList(filterState)
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                    myChargerListViewModel.myChargerList.collect { myChargerList ->
                        if (myChargerList.isEmpty() && count == 0) {
                            binding.rvMyChargerList.visibility = View.GONE
                            binding.ivEmptyState.visibility = View.VISIBLE
                            binding.tvEmptyPointHistory.visibility = View.VISIBLE
                        } else {
                            adapter.setMyChargerList(myChargerList)
                            if (myChargerList.isNotEmpty() && myChargerList.last().carbobId != 0) {
                                adapter.removeLoadingFooter()
                            }
                            binding.rvMyChargerList.visibility = View.VISIBLE
                            binding.ivEmptyState.visibility = View.GONE
                            binding.tvEmptyPointHistory.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun setBackButton() {
        binding.ibBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
