package com.softeer.team6four.ui.mypage.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentRegisterCompleteBinding

class RegisterCompleteFragment : Fragment() {
    private var _binding: FragmentRegisterCompleteBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterCompleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}