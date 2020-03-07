package com.example.project1_daytripplanner_montgomery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log

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
        return listOf(
            places(
                name = "Ford’s Theatre",
                rating = 1.0f,
                phoneNumber = "+12023474833",
                address = "511 10th St NW, Washington, DC 20004",
                pricePoint = "",
                url = "https://www.yelp.com/biz/fords-theatre-washington?adjust_creative=keu-kOIeln4R7XPAEsPSYg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=keu-kOIeln4R7XPAEsPSYg",
                lat = 0.0f,
                long = 0.0f
            ),
            places(
                name = "Ford’s Theatre",
                rating = 4.5f,
                phoneNumber = "+12023474833",
                address = "511 10th St NW, Washington, DC 20004",
                pricePoint = "$$$",
                url = "https://www.yelp.com/biz/fords-theatre-washington?adjust_creative=keu-kOIeln4R7XPAEsPSYg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=keu-kOIeln4R7XPAEsPSYg",
                lat = 0.0f,
                long = 0.0f
            ),
            places(
                name = "Ford’s Theatre",
                rating = 4.5f,
                phoneNumber = "",
                address = "511 10th St NW, Washington, DC 20004",
                pricePoint = "$",
                url = "https://www.yelp.com/biz/fords-theatre-washington?adjust_creative=keu-kOIeln4R7XPAEsPSYg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=keu-kOIeln4R7XPAEsPSYg",
                lat = 0.0f,
                long = 0.0f
            )
        )
    }
}
