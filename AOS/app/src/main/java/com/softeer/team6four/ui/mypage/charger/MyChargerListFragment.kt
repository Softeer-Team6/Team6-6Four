package com.softeer.team6four.ui.mypage.charger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentMyChargerListBinding

class MyChargerListFragment : Fragment() {
    private var _binding : FragmentMyChargerListBinding? = null
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
        //Test Code
        binding.tvMyChargerListTitle.setOnClickListener { findNavController().navigate(R.id.action_myChargerListFragment_to_myChargerFragment) }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}