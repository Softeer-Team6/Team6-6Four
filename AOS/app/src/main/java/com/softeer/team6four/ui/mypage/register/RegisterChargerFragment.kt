package com.softeer.team6four.ui.mypage.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentRegisterChargerBinding


class RegisterChargerFragment : Fragment() {
    private var _binding: FragmentRegisterChargerBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterChargerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            ibBack.setOnClickListener {
                if(registerFragContainer.getFragment<NavHostFragment>().findNavController()
                    .currentDestination?.id == R.id.registerOnboardingFragment) {
                    findNavController().popBackStack()
                }
                else childFragmentManager.popBackStack()
            }
            ibCancel.setOnClickListener {
                findNavController().popBackStack()
            }
            registerFragContainer.getFragment<NavHostFragment>().findNavController()
                .addOnDestinationChangedListener { _, destination, _ ->
                    if (destination.id == R.id.registerCompleteFragment) {
                        ibBack.visibility = View.GONE
                    }
                }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}