package com.softeer.team6four.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentRegisterEmailPasswordBinding

class RegisterEmailPasswordFragment : Fragment() {
    private var _binding: FragmentRegisterEmailPasswordBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterEmailPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNext.setOnClickListener { findNavController().navigate(R.id.action_registerEmailPasswordFragment_to_registerNicknameFragment) }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}