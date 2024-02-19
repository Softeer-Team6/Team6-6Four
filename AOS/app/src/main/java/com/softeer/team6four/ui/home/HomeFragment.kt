package com.softeer.team6four.ui.home

import android.Manifest
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), OnMapReadyCallback {
    private lateinit var requestLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private val inputMethodManager by lazy {
        requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    }
    private val homeViewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setPermissionLauncher()
        requestLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
        locationSource = FusedLocationSource(this, LOCATION_REQUEST_CODE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnMenu.setOnClickListener { drawerLayout.open() }
            navigationView.setNavigationItemSelectedListener { menuItem ->
                menuItem.setChecked(false)
                drawerLayout.close()
                when (menuItem.itemId) {
                    R.id.my_charger_list_item -> findNavController().navigate(R.id.action_homeFragment_to_myChargerFragment)
                    R.id.my_point_item -> findNavController().navigate(R.id.action_homeFragment_to_myPointFragment)
                    R.id.my_reservation_item -> findNavController().navigate(R.id.action_homeFragment_to_myReservationFragment)
                    R.id.register_charger_item -> findNavController().navigate(R.id.action_homeFragment_to_registerChargerFragment)
                }
                true
            }
            btnShowChargerList.setOnClickListener {
                BottomSheetFragment().show(
                    parentFragmentManager,
                    "BottomSheet"
                )
            }
            setSearchAction()
            mapView.getMapAsync(this@HomeFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        with(naverMap) {
            locationSource = this@HomeFragment.locationSource
            locationTrackingMode = LocationTrackingMode.Follow
            viewLifecycleOwner.lifecycleScope.launch {
                val searchMarker = createSearchMarker(locationOverlay.position)
                setBtnCurrentLocation(this@with, searchMarker, locationOverlay)
                setMapOnClickListener(this@with, searchMarker)

                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    homeViewModel.searchCoordinate.collect { latLng ->
                        if (latLng.latitude != 0.toDouble() && latLng.longitude != 0.toDouble()) {
                            naverMap.moveCamera(
                                CameraUpdate.scrollTo(latLng)
                                    .animate(CameraAnimation.Linear)
                            )
                            searchMarker.position = latLng
                        }
                    }
                }
            }
        }
    }

    private fun setMapOnClickListener(naverMap: NaverMap, searchMarker: Marker) {
        naverMap.setOnMapClickListener { _, latLng ->
            searchMarker.position = latLng
            naverMap.moveCamera(
                CameraUpdate.scrollTo(latLng)
                    .animate(CameraAnimation.Linear)
            )
        }
    }

    private fun createSearchMarker(latLng: LatLng): Marker {
        return Marker().apply {
            icon = OverlayImage.fromResource(R.drawable.icon_search_marker)
            position = latLng
            map = naverMap
        }
    }

    private fun setBtnCurrentLocation(
        naverMap: NaverMap,
        searchMarker: Marker,
        locationOverlay: LocationOverlay
    ) {
        binding.btnCurrentLocation.setOnClickListener {
            naverMap.moveCamera(
                CameraUpdate.scrollTo(locationOverlay.position)
                    .animate(CameraAnimation.Linear)
            )
            searchMarker.position = locationOverlay.position
        }
    }

    private fun setPermissionLauncher() {
        requestLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == false
                    && permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == false
                ) {
                    //Test Code
                    Toast.makeText(requireContext(), "위치 권한 설정이 필요합니다", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun setSearchAction() {
        with(binding) {
            etSearchLocation.setOnKeyListener { _, keyCode, event ->
                when {
                    event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER -> {
                        homeViewModel.getCoordinate(binding.etSearchLocation.text.toString())
                        inputMethodManager.hideSoftInputFromWindow(
                            binding.etSearchLocation.windowToken,
                            0
                        )
                        binding.etSearchLocation.clearFocus()
                        true
                    }

                    else -> false
                }
            }
            searchbarLayout.setEndIconOnClickListener {
                inputMethodManager.hideSoftInputFromWindow(binding.etSearchLocation.windowToken, 0)
                binding.etSearchLocation.clearFocus()
                homeViewModel.getCoordinate(binding.etSearchLocation.text.toString())
            }
        }
    }

    companion object {
        private const val LOCATION_REQUEST_CODE = 1000
    }
}