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
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import java.lang.Exception

/*
    TODO
        Nearest Metro
        No Results
        Errors
        Ask "Where should api keys be stored to be accessed"
 */
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var detailsButton : Button
    private lateinit var progressBar : ProgressBar

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
        val inputtedActivityName : String =intent.getStringExtra("activitySpinnerName")
        val inputtedActivityNumber = intent.getIntExtra("activitySeekBar", 0)
        val inputtedFoodName = intent.getStringExtra("foodSpinnerName")
        val inputtedFoodNumber = intent.getIntExtra("foodSeekBar", 0)

        progressBar = findViewById(R.id.progressBar2)
        detailsButton = findViewById(R.id.detailsButton)
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
       // Log.d("liciTag", "value of marker lat: " + markerLatLng.latitude + "___value of maker long: " + markerLatLng.longitude)

        detailsButton = findViewById(R.id.detailsButton)
        detailsButton.isEnabled = false
        progressBar.isVisible= true
        val inputtedActivityName : String =intent.getStringExtra("activitySpinnerName")
        val inputtedActivityNumber = intent.getIntExtra("activitySeekBar", 0)
        val inputtedFoodName = intent.getStringExtra("foodSpinnerName")
        val inputtedFoodNumber = intent.getIntExtra("foodSeekBar", 0)
        val mamp = MapsManager()
        val yelpApiKey = getString(R.string.yelp_key)
        val wmta_key = getString(R.string.wmta_key)
        val wmta_value = getString(R.string.wmta_value)
        var placeP : ArrayList<places>
        var metro : ArrayList<metro>

        val intent = Intent(this, DetailsActivity::class.java)
/*
        doAsync {
            placeP = try {
               // Log.d("licitag", "got here i guess so that thats good")
                mamp.retrieveActivity(
                    markerLatLng.latitude,
                    markerLatLng.longitude,
                    yelpApiKey,
                    inputtedActivityName,
                    inputtedActivityNumber,
                    inputtedFoodName,
                    inputtedFoodNumber
                )

            } catch (exception: Exception) {
                //Log.d("licitag", "got here i guess so that thats bad")
                exception.printStackTrace()
                arrayListOf()
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
                        intent.putExtra("places", placeP)
                    }
                }
            }

            if(placeP.isEmpty()){
                runOnUiThread{
                    intent.putExtra("places", placeP)
                    Toast.makeText(this@MapsActivity, "No Results for Activities or Food", Toast.LENGTH_SHORT).show()
                    detailsButton.isEnabled = false
                    progressBar.isVisible = false
                }
            }

        }
*/

        detailsButton.setOnClickListener{
            intent.putExtra("result", address)
            intent.putExtra("activitySpinnerName", inputtedActivityName)
            intent.putExtra("activitySeekBar", inputtedActivityNumber)
            intent.putExtra("foodSpinnerName", inputtedFoodName)
            intent.putExtra("foodSeekBar", inputtedFoodNumber)
            startActivity(intent)
        }
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

        doAsync {
            var check = 0
            placeP = try {
                // Log.d("licitag", "got here i guess so that thats good")
                mamp.retrieveActivity(
                    markerLatLng.latitude,
                    markerLatLng.longitude,
                    yelpApiKey,
                    inputtedActivityName,
                    inputtedActivityNumber,
                    inputtedFoodName,
                    inputtedFoodNumber
                )

            } catch (exception: Exception) {
                //Log.d("licitag", "got here i guess so that thats bad")
                exception.printStackTrace()
                arrayListOf()
            }

            if(placeP.isNotEmpty()){
                check = 1
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
                        intent.putExtra("places", placeP)
                    }
                }
            }

            if(placeP.isEmpty()){
                runOnUiThread{
                    intent.putExtra("places", placeP)
                    Toast.makeText(this@MapsActivity, "No Results for Activities or Food", Toast.LENGTH_SHORT).show()
                    detailsButton.isEnabled = false
                    progressBar.isVisible = false
                    check = 0
                }
            }
            metro = try {
                mamp.returnMetro(markerLatLng.latitude,
                    markerLatLng.longitude,wmta_key, wmta_value)
            }
            catch(exception : Exception){
                exception.printStackTrace()
                arrayListOf<metro>()
            }

            if(metro.isNotEmpty()){
                Log.d("licitag", "got here i guess so that thats good metro")
                metro.forEach{
                    runOnUiThread {
                        val markerOptions2 =
                            MarkerOptions().position(LatLng(it.lat, it.long))
                                .title(it.name)
                        mMap.addMarker(markerOptions2).setIcon(
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                    }
                }
                if(check == 1) {
                    runOnUiThread {
                        detailsButton.isEnabled = true
                        progressBar.isVisible = false
                    }
                }
            }

            if(metro.isEmpty()){
                Log.d("licitag", "got here i guess so that thats bad metro")
                runOnUiThread{
                    Toast.makeText(this@MapsActivity, "No Results for WMTA Metros", Toast.LENGTH_SHORT).show()
                }
                if(check == 0) {
                    runOnUiThread {
                        detailsButton.isEnabled = false
                        progressBar.isVisible = false
                    }
                }
                if(check == 1) {
                    runOnUiThread {
                        detailsButton.isEnabled = true
                        progressBar.isVisible = false
                    }
                }
            }
        }



    }

}
