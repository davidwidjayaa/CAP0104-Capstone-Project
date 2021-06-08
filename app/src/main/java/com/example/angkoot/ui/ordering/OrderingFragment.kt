package com.example.angkoot.ui.ordering

import android.Manifest
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.angkoot.databinding.FragmentOrderingBinding
import com.example.angkoot.utils.PermissionUtils
import com.example.angkoot.utils.PermissionUtils.REQUEST_CODE_LOCATION_PERMISSION
import com.example.angkoot.utils.ToastUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.IOException

@AndroidEntryPoint
class OrderingFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    private var _binding: FragmentOrderingBinding? = null
    private val binding get() = _binding!!
    private var _view: View? = null

    private val viewModel: OrderingViewModel by viewModels()

    private var _fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val fusedLocationProviderClient: FusedLocationProviderClient get() = _fusedLocationProviderClient!!

    private var _googleMap: GoogleMap? = null
    private val googleMap: GoogleMap get() = _googleMap!!

    private var _geoCoder: Geocoder? = null
    private val geoCoder: Geocoder get() = _geoCoder!!

    private var currentLocationMarker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderingBinding.inflate(inflater, container, false)
        _view = _binding?.root
        return _view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermission()
        //initUI()
        //observeData()
    }

    private fun initUI() {
        _fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        with(binding) {
            mapViewOrdering.getMapAsync {
                _googleMap = it

                getLastLocation()
            }
        }
    }

    private fun observeData() {
        //
    }

    private fun setMarker(latLng: LatLng, locationName: String) {
        currentLocationMarker?.remove()

        with(googleMap) {
            currentLocationMarker = addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Current Location")
                    .snippet(locationName)
                    .icon(
                        BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_GREEN
                        )
                    )
            )

            moveMapCameraTo(latLng)
        }
    }

    private fun moveMapCameraTo(latLng: LatLng, zoom: Float = DEFAULT_ZOOM) {
        with(googleMap) {
            val cameraPosition = CameraPosition.Builder()
                .target(latLng)
                .zoom(zoom)
                .build()

            moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }

    private fun getLastLocation() {
        if (PermissionUtils.checkLocationPermission(requireContext())) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val lastLocation = it.result
                    var address = "No known address"

                    try {
                        val addressList = geoCoder.getFromLocation(
                            lastLocation.latitude,
                            lastLocation.longitude,
                            1
                        )

                        if (addressList.isNotEmpty()) {
                            address = addressList[0].getAddressLine(0)
                        }

                        currentLocationMarker?.remove()
                        moveMapCameraTo(LatLng(lastLocation.latitude, lastLocation.longitude))
                        googleMap.isMyLocationEnabled = true

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                } else {
                    ToastUtils.show(
                        requireContext(),
                        "No current location found"
                    )

                    moveMapCameraTo(LatLng(-6.23139938, 106.95464244))
                    googleMap.isMyLocationEnabled = true
                }
            }
        }
    }

    private fun requestPermission() {
        if (PermissionUtils.hasLocationPermission(requireContext())) return

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permission to use this application",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permission to use this application",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    // PERMISSIONS
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermission()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    // LIFECYCLE
    override fun onDestroyView() {
        super.onDestroyView()
        _fusedLocationProviderClient = null
        _googleMap = null
        currentLocationMarker = null
        _binding = null
        _view = null
    }

//    override fun onResume() {
//        super.onResume()
//        getLastLocation()
//        _binding?.mapViewOrdering?.onResume()
//    }

//    override fun onStart() {
//        super.onStart()
//        _binding?.mapViewOrdering?.onStart()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        _binding?.mapViewOrdering?.onPause()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        _binding?.mapViewOrdering?.onStop()
//    }
//
//    override fun onLowMemory() {
//        super.onLowMemory()
//        _binding?.mapViewOrdering?.onLowMemory()
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        _binding?.mapViewOrdering?.onSaveInstanceState(outState)
//    }

    // CONSTANTS
    companion object {
        const val DEFAULT_ZOOM = 17f
    }
}