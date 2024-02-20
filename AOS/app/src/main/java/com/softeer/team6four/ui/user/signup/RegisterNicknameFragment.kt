package com.softeer.team6four.ui.user.signup

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
import com.softeer.team6four.databinding.FragmentRegisterNicknameBinding
import kotlinx.coroutines.launch

class RegisterNicknameFragment : Fragment() {
    private var _binding: FragmentRegisterNicknameBinding? = null
    private val signUpViewModel: SignUpViewModel by activityViewModels()
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterNicknameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewModel = signUpViewModel
            lifecycleOwner = viewLifecycleOwner

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    signUpViewModel.signUpState.collect { state ->
                        if (state) {
                            requireParentFragment().requireParentFragment().findNavController()
                                .popBackStack()
                        }
                    }
                }
            }
        }
    }
}