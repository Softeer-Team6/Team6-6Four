package com.softeer.team6four.ui.home

import android.Manifest
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Build
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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.softeer.team6four.R
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.remote.charger.model.MapChargerModel
import com.softeer.team6four.databinding.FragmentHomeBinding
import com.softeer.team6four.databinding.HeaderNavigationDrawerBinding
import com.softeer.team6four.ui.payment.PaymentConfirmFragment
import com.softeer.team6four.ui.payment.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeFragment : Fragment(), OnMapReadyCallback {
    private lateinit var requestLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var barcodeLauncher: ActivityResultLauncher<ScanOptions>
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private val searchMarker = Marker().apply {
        icon = OverlayImage.fromResource(R.drawable.icon_search_marker)
    }
    private val inputMethodManager by lazy {
        requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    }
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val paymentViewModel: PaymentViewModel by activityViewModels()

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
        setBarcodeLauncher()
        requestLauncher.launch(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.CAMERA
                )
            } else {
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA
                )
            }

        )
        locationSource = FusedLocationSource(this, LOCATION_REQUEST_CODE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewModel = homeViewModel
            lifecycleOwner = viewLifecycleOwner
            mapView.getMapAsync(this@HomeFragment)
        }
        setBtnShowChargerList()
        setNavigationDrawer()
        setSearchAction()
        sendFcmToken()
        setNickname()
        setLogout()
        setBtnQRScan()
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
            addOnLocationChangeListener { location ->
                homeViewModel.updateUserLatLng(location.latitude, location.longitude)
            }
            createSearchMarker(locationOverlay.position)
            setMapOnClickListener()
            setBtnCurrentLocation(locationOverlay)
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    createChargerInfoWindowList()
                }
            }
        }
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

    private fun setNavigationDrawer() {
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
        }
    }

    private fun setMapOnClickListener() {
        naverMap.setOnMapClickListener { _, latLng ->
            searchMarker.position = latLng
            naverMap.moveCamera(
                CameraUpdate.scrollTo(latLng)
                    .animate(CameraAnimation.Linear)
            )
            homeViewModel.updateSearchMarkerLatLng(latLng)
            homeViewModel.fetchMapChargerList()
            homeViewModel.fetchBottomSheetChargerList()
        }
    }

    private fun createSearchMarker(latLng: LatLng) {
        searchMarker.position = latLng
        searchMarker.map = naverMap
    }


    private fun createChargerInfoWindow(mapChargerModel: MapChargerModel): InfoWindow {
        return InfoWindow().apply {
            position =
                LatLng(
                    mapChargerModel.latitude,
                    mapChargerModel.longitude
                )
            tag = mapChargerModel.chargerId
            adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
                override fun getText(p0: InfoWindow): CharSequence {
                    return mapChargerModel.feePerHour
                }
            }
            setOnClickListener { _ ->
                homeViewModel.updateSelectedCharger((tag as Long))
                findNavController().navigate(R.id.action_homeFragment_to_applyDetailFragment)
                true
            }
        }
    }

    private suspend fun createChargerInfoWindowList() {
        homeViewModel.mapChargerList.collect { list ->
            homeViewModel.clearInfoWindows()
            val infoWindows = list.map { model ->
                val infoWindow = withContext(Dispatchers.Default) { createChargerInfoWindow(model) }
                infoWindow.map = naverMap
                infoWindow
            }
            homeViewModel.updateInfoWindows(infoWindows)
        }
    }

    private fun setBtnShowChargerList() {
        binding.btnShowChargerList.setOnClickListener {
            BottomSheetFragment().show(
                parentFragmentManager,
                "BottomSheet"
            )
        }
    }

    private fun setBtnCurrentLocation(
        locationOverlay: LocationOverlay
    ) {
        binding.btnCurrentLocation.setOnClickListener {
            naverMap.moveCamera(
                CameraUpdate.scrollTo(locationOverlay.position)
                    .animate(CameraAnimation.Linear)
            )
            searchMarker.position = locationOverlay.position
            homeViewModel.updateSearchMarkerLatLng(locationOverlay.position)

            homeViewModel.fetchMapChargerList()
            homeViewModel.fetchBottomSheetChargerList()
            homeViewModel.clearInfoWindows()
            homeViewModel.updateSearchMarkerLatLng(locationOverlay.position)
        }
    }

    private fun setPermissionLauncher() {
        requestLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == false
                    && permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == false
                ) {
                    Toast.makeText(requireContext(), "위치 권한 설정이 필요합니다", Toast.LENGTH_SHORT).show()
                } else if (permissions[Manifest.permission.POST_NOTIFICATIONS] == false) {
                    Toast.makeText(requireContext(), "알림 권한 설정이 필요합니다", Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun setBarcodeLauncher() {
        barcodeLauncher =
            registerForActivityResult(ScanContract()) { result ->
                if (result.contents != null) {
                    paymentViewModel.updateCipher(result.contents)
                    paymentViewModel.updateReservationId()
                    PaymentConfirmFragment().show(parentFragmentManager, PaymentConfirmFragment.TAG)
                }
            }
    }

    private fun setSearchAction() {
        with(binding) {
            etSearchLocation.setOnKeyListener { _, keyCode, event ->
                when {
                    event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER -> {
                        homeViewModel.getCoordinate()
                        inputMethodManager.hideSoftInputFromWindow(
                            binding.etSearchLocation.windowToken,
                            0
                        )
                        etSearchLocation.clearFocus()
                        true
                    }

                    else -> false
                }
            }
            searchbarLayout.setEndIconOnClickListener {
                inputMethodManager.hideSoftInputFromWindow(
                    binding.etSearchLocation.windowToken,
                    0
                )
                etSearchLocation.clearFocus()
                homeViewModel.getCoordinate()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.searchAddressLatLng.collect { latLng ->
                    if (latLng.longitude != 0.toDouble() && latLng.latitude != 0.toDouble()) {
                        searchMarker.position = latLng
                        naverMap.moveCamera(
                            CameraUpdate.scrollTo(latLng)
                                .animate(CameraAnimation.Linear)
                        )
                        homeViewModel.updateSearchMarkerLatLng(latLng)
                        homeViewModel.fetchMapChargerList()
                        homeViewModel.fetchBottomSheetChargerList()
                    }
                }
            }
        }
    }

    private fun sendFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }
            val token = task.result
            homeViewModel.sendFcmToken(token)
        }
    }

    private fun setNickname() {
        HeaderNavigationDrawerBinding.bind(
            binding.navigationView.getHeaderView(0)
        ).apply {
            this.lifecycleOwner = viewLifecycleOwner
            viewModel = homeViewModel
        }
    }

    private fun setLogout() {
        binding.tvLogout.setOnClickListener {
            homeViewModel.logout()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.nickname.collect { nickname ->
                    if (nickname.isEmpty()) {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun setBtnQRScan() {
        binding.btnCamera.setOnClickListener {
            val options = ScanOptions()
            options.setPrompt("QR 스캔해주세요")
            options.setOrientationLocked(false)
            barcodeLauncher.launch(options)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                paymentViewModel.paymentInfoModelState.collect { resource ->
                    if (resource is Resource.Success) {
                        findNavController().navigate(R.id.action_homeFragment_to_paymentSuccessFragment)
                    } else if (resource is Resource.Error) {
                        findNavController().navigate(R.id.action_homeFragment_to_paymentFailFragment)
                    }
                    paymentViewModel.updatePaymentInfoModel()
                }
            }
        }
    }

    companion object {
        private const val LOCATION_REQUEST_CODE = 1000
    }
}