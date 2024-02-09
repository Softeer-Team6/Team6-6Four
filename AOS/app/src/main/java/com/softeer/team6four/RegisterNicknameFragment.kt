package com.softeer.team6four

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.softeer.team6four.databinding.FragmentRegisterNicknameBinding

class RegisterNicknameFragment : Fragment() {
    private var _binding: FragmentRegisterNicknameBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterNicknameBinding.inflate(inflater, container, false)
        return binding.root
    }

}