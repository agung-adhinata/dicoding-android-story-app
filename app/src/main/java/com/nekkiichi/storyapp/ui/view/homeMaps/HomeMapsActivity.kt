package com.nekkiichi.storyapp.ui.view.homeMaps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nekkiichi.storyapp.R
import com.nekkiichi.storyapp.data.ResponseStatus
import com.nekkiichi.storyapp.data.remote.response.ListStoryResponse
import com.nekkiichi.storyapp.databinding.ActivityHomeMapsBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityHomeMapsBinding
    private val viewModel: HomeMapsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.stories.collect {
                        collectStoryList(it)
                    }
                }
            }
        }


        // Add a marker in Sydney and move the camera
        val indonesia = LatLng(0.7893, 113.9213)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(indonesia))
    }

    private fun collectStoryList(data: ResponseStatus<ListStoryResponse>) {
        when(data) {
            is ResponseStatus.Loading ->{}
            is ResponseStatus.Success -> {
                val storyLists = data.data.listStory
                if (storyLists != null) {
                    // add story marker from story lists
                    for (item in storyLists) {
                        val latLng = LatLng(
                            item.lat?:continue,
                            item.lon?:continue
                        )
                        val pin = MarkerOptions().position(latLng).title(item.name).snippet(item.description)
                        mMap.addMarker(pin)
                    }
                }
            }
            else -> {}
        }
    }
}