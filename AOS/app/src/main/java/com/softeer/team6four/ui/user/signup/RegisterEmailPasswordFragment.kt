package com.softeer.team6four.ui.user.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentRegisterEmailPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterEmailPasswordFragment : Fragment() {
    private var _binding: FragmentRegisterEmailPasswordBinding? = null
    private val signUpViewModel: SignUpViewModel by activityViewModels()
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterEmailPasswordBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewModel = signUpViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        binding.btnNext.setOnClickListener { findNavController().navigate(R.id.action_registerEmailPasswordFragment_to_registerNicknameFragment) }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}