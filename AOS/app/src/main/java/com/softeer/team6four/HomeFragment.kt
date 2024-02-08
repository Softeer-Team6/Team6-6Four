package com.softeer.team6four

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.softeer.team6four.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            btnMenu.setOnClickListener { drawerLayout.open() }
            navigationView.setNavigationItemSelectedListener { menuItem ->
                menuItem.setChecked(false)
                drawerLayout.close()
                when (menuItem.itemId) {
                    R.id.my_charger_item -> findNavController().navigate(R.id.action_homeFragment_to_myChargerFragment)
                    R.id.my_point_item -> findNavController().navigate(R.id.action_homeFragment_to_myPointFragment)
                    R.id.my_reservation_item -> findNavController().navigate(R.id.action_homeFragment_to_myReservationFragment)
                    R.id.register_charger_item -> findNavController().navigate(R.id.action_homeFragment_to_registerChargerFragment)
                }
                true
            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}