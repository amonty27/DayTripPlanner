package com.example.project1_daytripplanner_montgomery

import android.location.Address
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import org.jetbrains.anko.doAsync

class DetailsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        recyclerView = findViewById(R.id.recyclerView)
        // Set the RecyclerView direction to vertical (the default)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Log.d("liciTag", "please work for the love of")
        val places = getFakePlaces()
        val adapter = DetailsAdapter(places)
        recyclerView.adapter = adapter
    }

    /*data class places (
        val name: String,
        val rating : Double,
        val phoneNumber : String,
        val Address : String,
        val pricePoint : String,
        val url : String
    )*/
    fun getFakePlaces(): List<places>{

        val address: Address = intent.getParcelableExtra<Address>("result")
        val yelpApiKey = getString(R.string.yelp_key)
        // get values from the address to use in making a marker on the map
        val markerLatLng = LatLng(address.latitude, address.longitude)
        val inputtedActivityName =intent.getStringExtra("activitySpinnerName")
        val inputtedActivityNumber = intent.getIntExtra("activitySeekBar", 0)
        val inputtedFoodName = intent.getStringExtra("foodSpinnerName")
        val inputtedFoodNumber = intent.getIntExtra("foodSeekBar", 0)

        val mamp = MapsManager()
        var list: List<places> = arrayListOf<places>()
        /*return mamp.retrieveActivity(
            markerLatLng.latitude,
            markerLatLng.longitude,
            yelpApiKey,
            inputtedActivityName,
            inputtedActivityNumber,
            inputtedFoodName,
            inputtedFoodNumber
        )*/
        doAsync {
            list = try {
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
                arrayListOf()
            }
        }

        Log.d("licitag","array list values: ${list.get(0).url}")
        //return list
         return arrayListOf(
            places(
                name = "Ford’s Theatre",
                rating = 1.0f,
                phoneNumber = "+12023474833",
                address = "511 10th St NW, Washington, DC 20004",
                pricePoint = "",
                url = "https://www.yelp.com/biz/fords-theatre-washington?adjust_creative=keu-kOIeln4R7XPAEsPSYg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=keu-kOIeln4R7XPAEsPSYg",
                lat = 0.0,
                long = 0.0,
                type = 0
            ),
            places(
                name = "Ford’s Theatre",
                rating = 4.5f,
                phoneNumber = "+12023474833",
                address = "511 10th St NW, Washington, DC 20004",
                pricePoint = "$$$",
                url = "https://www.yelp.com/biz/fords-theatre-washington?adjust_creative=keu-kOIeln4R7XPAEsPSYg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=keu-kOIeln4R7XPAEsPSYg",
                lat = 0.0,
                long = 0.0,
                type = 0
            ),
            places(
                name = "Ford’s Theatre",
                rating = 4.5f,
                phoneNumber = "",
                address = "511 10th St NW, Washington, DC 20004",
                pricePoint = "$",
                url = "https://www.yelp.com/biz/fords-theatre-washington?adjust_creative=keu-kOIeln4R7XPAEsPSYg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=keu-kOIeln4R7XPAEsPSYg",
                lat = 0.0,
                long = 0.0,
                type = 0
            )
        )
    }
}
