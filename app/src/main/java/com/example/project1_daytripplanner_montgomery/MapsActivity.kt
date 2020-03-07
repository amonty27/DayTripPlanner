package com.example.project1_daytripplanner_montgomery

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.doAsync
import android.location.Address
import android.location.Geocoder
import android.telecom.Call
import android.util.Log
import android.widget.Button
import com.google.android.gms.maps.model.CircleOptions
import java.lang.Exception
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import java.nio.channels.spi.AbstractSelectionKey

/*
    TODO
        Nearest Metro
        Nearest Restaurant
        Nearest Attraction
        Marker colors
        Details Buttons
        Loading
        No Results
        Errors
        Ask "Where should api keys be stored to be accessed"
 */
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var detailsButton : Button
   // private lateinit var yelpApiKey: String
    private lateinit var places : places

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        title = "Day Trip Planner"

        // get the button
        detailsButton = findViewById(R.id.detailsButton)

        detailsButton.setOnClickListener{
            val intent = Intent(this, DetailsActivity::class.java)
            startActivity(intent)
        }

        val address: Address = intent.getParcelableExtra<Address>("result")
        // get values from the address to use in making a marker on the map
        val markerLatLng : LatLng = LatLng(address.latitude, address.longitude)
        val streetAddress : String = address.getAddressLine(0)
        val preferences = getSharedPreferences("dayTripPlaner_data", Context.MODE_PRIVATE)
        val mamp = MapsManager()
        val yelpApiKey = getString(R.string.yelp_key)

        doAsync {
            val places = try {
                mamp.retrieveActivity(
                    markerLatLng.latitude,
                    markerLatLng.longitude,
                    preferences,
                    yelpApiKey
                )

            } catch (exception: Exception) {
                listOf<places>()
            }

        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // get the Address value passed through the intent
        val address: Address = intent.getParcelableExtra<Address>("result")
        // get values from the address to use in making a marker on the map
        val markerLatLng : LatLng = LatLng(address.latitude, address.longitude)
        val streetAddress : String = address.getAddressLine(0)
        // for debugging
        Log.d("liciTag", "value of marker lat: " + markerLatLng.latitude + "___value of maker long: " + markerLatLng.longitude)

        // create a new marker and add it to the map
        val markerOptions = MarkerOptions().position(markerLatLng).title(streetAddress)
        mMap.addMarker(markerOptions)

        // update the camera to point to the Address
        val zoom : Float = 13.9f
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,zoom))

        // create a circle around the maker 1500m wide
        val circleOptions : CircleOptions = CircleOptions().center(markerLatLng).radius(1500.0)
        mMap.addCircle(circleOptions)

        val preferences = getSharedPreferences("dayTripPlaner_data", Context.MODE_PRIVATE)

    }
}
