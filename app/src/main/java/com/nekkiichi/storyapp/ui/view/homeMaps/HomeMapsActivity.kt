package com.nekkiichi.storyapp.ui.view.homeMaps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.nekkiichi.storyapp.R
import com.nekkiichi.storyapp.data.ResponseStatus
import com.nekkiichi.storyapp.data.remote.response.ListStoryResponse
import com.nekkiichi.storyapp.databinding.ActivityHomeMapsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityHomeMapsBinding
    private val viewModel: HomeMapsViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    //accurate location access, granted
                    getMyLastLocation()
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = resources.getString(R.string.maps_title)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        val success =  mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json))
        if(!success) {
            Log.d(TAG, "failed load map style")
        }
        getMyLastLocation()

        viewModel.stories.observe(this) {
            collectStoryList(it)
        }

        val indonesia = LatLng(0.7893, 113.9213)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(indonesia))
    }

    private fun getMyLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return
        } else {
            mMap.isMyLocationEnabled = true

        }
    }

    private fun collectStoryList(data: ResponseStatus<ListStoryResponse>) {
        when (data) {
            is ResponseStatus.Loading -> {}
            is ResponseStatus.Success -> {
                val storyLists = data.data.listStory
                if (storyLists != null) {
                    // add story marker from story lists
                    for (item in storyLists) {
                        val latLng = LatLng(
                            item.lat ?: continue,
                            item.lon ?: continue
                        )
                        val pin = MarkerOptions()
                            .position(latLng)
                            .title(item.name)
                            .snippet(item.description)
                        mMap.addMarker(pin)
                    }
                }
            }

            else -> {}
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> false
        }
    }

    companion object {
        val TAG = this::class.java.simpleName
    }
}