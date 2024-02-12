package com.softeer.team6four.ui.mypage.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentRegisterDescriptionBinding
import com.softeer.team6four.databinding.FragmentRegisterDetailBinding

class RegisterDescriptionFragment : Fragment() {
    private var _binding: FragmentRegisterDescriptionBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}