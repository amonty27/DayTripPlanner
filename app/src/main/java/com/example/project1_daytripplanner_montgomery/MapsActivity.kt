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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        title = "Day Trip Planner"

        val address: Address = intent.getParcelableExtra<Address>("address")
        // get values from the address to use in making a marker on the map
        val markerLatLng = LatLng(address.latitude, address.longitude)
        val streetAddress : String = address.getAddressLine(0)
        val inputtedActivityName : String =intent.getStringExtra("activitySpinnerName")
        val inputtedActivityNumber = intent.getIntExtra("activitySeekBar", 0)
        val inputtedFoodName = intent.getStringExtra("foodSpinnerName")
        val inputtedFoodNumber = intent.getIntExtra("foodSeekBar", 0)
        val mamp = MapsManager()
        val yelpApiKey = getString(R.string.yelp_key)

        // get the button
        detailsButton = findViewById(R.id.detailsButton)
        detailsButton.setOnClickListener{
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("result", address)
            intent.putExtra("activitySpinnerName", inputtedActivityName)
            intent.putExtra("activitySeekBar", inputtedActivityNumber)
            intent.putExtra("foodSpinnerName", inputtedFoodName)
            intent.putExtra("foodSeekBar", inputtedFoodNumber)
            startActivity(intent)
        }

       /* doAsync {
            placeP = try {
                mamp.retrieveActivity(
                    markerLatLng.latitude,
                    markerLatLng.longitude,
                    yelpApiKey,
                    inputtedActivity,
                    inputtedActivityNumber,
                    inputtedFood,
                    inputtedFoodNumber
                )

            } catch (exception: Exception) {
                mutableListOf()
            }
        }*/


    }
    fun getPlaces(){

        //return placeP
        val address: Address = intent.getParcelableExtra<Address>("address")
        // get values from the address to use in making a marker on the map
        val markerLatLng = LatLng(address.latitude, address.longitude)
        val streetAddress : String = address.getAddressLine(0)
        val preferences = getSharedPreferences("dayTripPlaner_data", Context.MODE_PRIVATE)
        val inputtedActivity = preferences.getString("activitySpinnerName", "")
        val inputtedActivityNumber = preferences.getInt("activitySeekBar", 0)
        val inputtedFood = preferences.getString("foodSpinnerName","")
        val inputtedFoodNumber = preferences.getInt("foodSeekBar",0)
        val mamp = MapsManager()
        val yelpApiKey = getString(R.string.yelp_key)
        var placeP : List<places> = listOf()

        doAsync {
            placeP = try {
                Log.d("licitag", "got here i guess so that thats good")
                mamp.retrieveActivity(
                    markerLatLng.latitude,
                    markerLatLng.longitude,
                    yelpApiKey,
                    inputtedActivity,
                    inputtedActivityNumber,
                    inputtedFood,
                    inputtedFoodNumber
                )

            } catch (exception: Exception) {
                Log.d("licitag", "got here i guess so that thats bad")
                exception.printStackTrace()
                mutableListOf()
            }

            Log.d("licitag", "size of placeP : ${placeP.size}")
            Log.d("licitag", "name of place in placeP : ${placeP.get(0).name}")
            runOnUiThread {
                val markerOptions2 = MarkerOptions().position(LatLng(placeP.get(0).lat.toDouble(), placeP.get(0).long.toDouble())).title(placeP.get(0).address)
                mMap.addMarker(markerOptions2)
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
        val address: Address = intent.getParcelableExtra<Address>("address")
        // get values from the address to use in making a marker on the map
        val markerLatLng : LatLng = LatLng(address.latitude, address.longitude)
        val streetAddress : String = address.getAddressLine(0)
        // for debugging
        Log.d("liciTag", "value of marker lat: " + markerLatLng.latitude + "___value of maker long: " + markerLatLng.longitude)


        val preferences = getSharedPreferences("dayTripPlaner_data", Context.MODE_PRIVATE)
        val inputtedActivity = preferences.getString("activitySpinnerName", "")
        val inputtedActivityNumber = preferences.getInt("activitySeekBar", 0)
        val inputtedFood = preferences.getString("foodSpinnerName","")
        val inputtedFoodNumber = preferences.getInt("foodSeekBar",0)
        val mamp = MapsManager()
        val yelpApiKey = getString(R.string.yelp_key)
        var placeP : List<places> = listOf()

        doAsync {
            placeP = try {
                Log.d("licitag", "got here i guess so that thats good")
                mamp.retrieveActivity(
                    markerLatLng.latitude,
                    markerLatLng.longitude,
                    yelpApiKey,
                    inputtedActivity,
                    inputtedActivityNumber,
                    inputtedFood,
                    inputtedFoodNumber
                )

            } catch (exception: Exception) {
                Log.d("licitag", "got here i guess so that thats bad")
                exception.printStackTrace()
                mutableListOf()
            }

            if(placeP.isNotEmpty()){
                placeP.forEach{
                    runOnUiThread {
                        val markerOptions2 =
                            MarkerOptions().position(LatLng(it.lat, it.long))
                                .title(it.name)
                        if(it.type == 0) { //food
                            mMap.addMarker(markerOptions2).setIcon(
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                            )
                        }
                        if(it.type == 1){ // activity
                            mMap.addMarker(markerOptions2).setIcon(
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)
                            )
                        }
                    }
                }
            }
            /*if(placeP.isNotEmpty()) {
                for(i in placeP) {
                    var index = 0
                    Log.d("licitag", "size of placeP : ${placeP.size}")
                    Log.d("licitag", "name of place 0 in placeP : ${placeP.get(index).name}")
                    Log.d(
                        "licitag",
                        "lat/long of place 0 in placeP : ${placeP.get(index).lat} , ${placeP.get(index).long}"
                    )
                    runOnUiThread {
                        val markerOptions2 =
                            MarkerOptions().position(LatLng(placeP.get(index).lat, placeP.get(index).long))
                                .title(placeP.get(index).name)
                        mMap.addMarker(markerOptions2)
                    }
                    index++
                }
            }*/
        }
        //getPlaces()
        //val placeP2 = getPlaces()
        // create a new marker and add it to the map
        val markerOptions = MarkerOptions().position(markerLatLng).title(streetAddress)
        mMap.addMarker(markerOptions)

        //val markerOptions2 = MarkerOptions().position(LatLng(placeP2.get(0).lat, placeP2.get(0).long)).title(placeP2.get(0).address)
        //mMap.addMarker(markerOptions2)

        // update the camera to point to the Address
        val zoom = 13.9f
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,zoom))

        // create a circle around the maker 1500m wide
        val circleOptions : CircleOptions = CircleOptions().center(markerLatLng).radius(1500.0)
        mMap.addCircle(circleOptions)

    }

}
