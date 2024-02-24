package com.softeer.team6four.ui.mypage.register

import android.content.Context
import android.location.Geocoder
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import com.softeer.team6four.databinding.FragmentRegisterLocationBinding
import kotlinx.coroutines.launch
import java.util.Locale


class RegisterLocationFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentRegisterLocationBinding? = null
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var geocoder: Geocoder
    private val inputMethodManager by lazy {
        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }
    private val registerViewModel: RegisterViewModel by activityViewModels()

    private val searchMarker = createSearchMarker()
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewModel = registerViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        setSearchAction()
        binding.mapView.getMapAsync(this)
        locationSource = FusedLocationSource(this, 0)
        binding.btnNext.setOnClickListener { findNavController().navigate(R.id.action_registerLocationFragment_to_registerDetailFragment) }
        geocoder = Geocoder(requireContext(), Locale.KOREA)
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                naverMap.locationSource = locationSource
                naverMap.locationTrackingMode = LocationTrackingMode.NoFollow
                searchMarker.position = naverMap.locationOverlay.position

                searchMarker.map = naverMap
                setMapOnClickListener(searchMarker)
                setBtnCurrentLocation(naverMap, naverMap.locationOverlay)

                registerViewModel.userLatLng.collect { latLng ->
                    latLng?.let {
                        naverMap.moveCamera(
                            CameraUpdate.scrollTo(latLng)
                                .animate(CameraAnimation.Linear)
                        )
                        searchMarker.position = latLng
                        getLocationAddress(latLng)
                    }
                }
            }
        }
    }

    private fun setMapOnClickListener(searchMarker: Marker) {
        naverMap.setOnMapClickListener { _, latLng ->
            searchMarker.position = latLng
            naverMap.moveCamera(
                CameraUpdate.scrollTo(latLng)
                    .animate(CameraAnimation.Linear)
            )
            registerViewModel.updateUserLng(latLng)
        }
    }

    private fun createSearchMarker(): Marker {
        return Marker().apply {
            icon = OverlayImage.fromResource(R.drawable.icon_search_marker)
        }
    }

    private fun setBtnCurrentLocation(
        naverMap: NaverMap,
        locationOverlay: LocationOverlay
    ) {
        binding.tvChargerCurrentLocation.setOnClickListener {
            naverMap.moveCamera(
                CameraUpdate.scrollTo(locationOverlay.position)
                    .animate(CameraAnimation.Linear)
            )
            searchMarker.position = locationOverlay.position
            registerViewModel.updateUserLng(searchMarker.position)
        }
    }

    private fun setSearchAction() {
        with(binding) {
            etSearchLocation.setOnKeyListener { _, keyCode, event ->
                when {
                    event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER -> {
                        registerViewModel.getCoordinate()
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
                registerViewModel.getCoordinate()
            }
        }
    }

    private fun getLocationAddress(latLng: LatLng) {
        if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1) { list ->
                if (list.isNotEmpty()) {
                    registerViewModel.updateAddressText(list[0].getAddressLine(0))
                }
            }
        } else {
            val list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (!list.isNullOrEmpty()) {
                registerViewModel.updateAddressText(list[0].getAddressLine(0))
            }
        }
    }
}