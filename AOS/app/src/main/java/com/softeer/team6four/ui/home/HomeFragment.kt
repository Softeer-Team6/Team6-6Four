package com.softeer.team6four.ui.home

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.softeer.team6four.R
import com.softeer.team6four.databinding.FragmentHomeBinding


class HomeFragment : Fragment(), OnMapReadyCallback {
    private lateinit var requestLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
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
            locationTrackingMode = LocationTrackingMode.NoFollow

            val searchMarker = Marker().apply {
                icon = OverlayImage.fromResource(R.drawable.icon_search_marker)
            }
            (locationSource as FusedLocationSource).activate { location ->
                location?.let {
                    searchMarker.position = LatLng(location.latitude, location.longitude)
                    searchMarker.map = this@HomeFragment.naverMap
                }
            }

            binding.btnCurrentLocation.setOnClickListener {
                naverMap.moveCamera(
                    CameraUpdate.scrollTo(locationOverlay.position).animate(CameraAnimation.Linear)
                )
                searchMarker.position = locationOverlay.position
            }

            this.setOnMapClickListener { _, latLng ->
                searchMarker.position = latLng
            }
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


    companion object {
        private const val LOCATION_REQUEST_CODE = 1000
    }
}