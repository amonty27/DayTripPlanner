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
        val places = getPlaces()
        val adapter = DetailsAdapter(places)
        recyclerView.adapter = adapter
    }

    fun getPlaces(): List<places>{
        val placesSearch = intent.getSerializableExtra("places") as List<places>
        return placesSearch
    }
}
