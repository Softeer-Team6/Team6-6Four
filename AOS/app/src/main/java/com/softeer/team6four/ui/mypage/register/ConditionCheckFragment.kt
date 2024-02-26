package com.softeer.team6four.ui.mypage.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentConditionCheckBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConditionCheckFragment : Fragment() {
    private var _binding: FragmentConditionCheckBinding? = null
    private val registerViewModel: RegisterViewModel by activityViewModels()
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConditionCheckBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewModel = registerViewModel
            lifecycleOwner = viewLifecycleOwner
            btnNext.setOnClickListener { findNavController().navigate(R.id.action_conditionCheckFragment_to_registerLocationFragment) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}