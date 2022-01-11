package com.wojciechkula.locals.presentation.creategroup.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.firebase.geofire.core.GeoHash
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.wojciechkula.locals.R
import com.wojciechkula.locals.databinding.FragmentCreateGroupMapBinding
import com.wojciechkula.locals.domain.model.LocationModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateGroupMapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentCreateGroupMapBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var selectedLocation: LocationModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateGroupMapBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        initViews()
    }

    private fun initViews() {
        with(binding) {
            backButton.setOnClickListener {
                findNavController().popBackStack()
            }

            submitButton.setOnClickListener {
                setFragmentResult("requestKey", bundleOf("selectedLocation" to selectedLocation))
                findNavController().popBackStack()
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            val lat = task.result.latitude
            val lng = task.result.longitude
            val myPosition = LatLng(lat, lng)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 13.0f))
        }

        googleMap.setOnMapClickListener { selectedPlace ->
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(selectedPlace).title("Selected place"))
            binding.submitButton.isEnabled = true
            selectedLocation = LocationModel(
                GeoHash(selectedPlace.latitude, selectedPlace.longitude).geoHashString,
                selectedPlace.latitude,
                selectedPlace.longitude
            )
        }
    }
}