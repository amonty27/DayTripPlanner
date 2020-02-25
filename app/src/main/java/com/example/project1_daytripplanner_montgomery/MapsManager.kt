package com.example.project1_daytripplanner_montgomery

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.android.gms.maps.model.LatLng
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit


import android.content.DialogInterface
import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import org.jetbrains.anko.doAsync
import android.location.Address

/*
    TODO
        - retireve api key from xml file or something like that
 */

class MapsManager {

    private val okHttpClient: OkHttpClient

    init {
        val builder = OkHttpClient.Builder()

        // Set up our OkHttpClient instance to log all network traffic to Logcat
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)

        builder.connectTimeout(15, TimeUnit.SECONDS)
        builder.readTimeout(15, TimeUnit.SECONDS)
        builder.writeTimeout(15, TimeUnit.SECONDS)

        okHttpClient = builder.build()
    }

    fun retrieveActivity(latitude: Double, longitude: Double, sharedPreferences: SharedPreferences,
                       searchNumFood : Int, searchNumActivity : Int): List<places>{
        // get the name of the activity and
        val inputtedActivity = sharedPreferences.getString("activitySpinnerName", "")
        val inputtedActivityNumber = sharedPreferences.getInt("activitySeekBar", 0)

        Log.d("liciTag", "value of inputtedActivity: " + inputtedActivity)

        val request = Request.Builder()
            .url("https://api.yelp.com/v3/businesses/search?term=$inputtedActivity&latitude=38.$latitude&longitude=$longitude&limit=$inputtedActivityNumber&sort_by=rating&radius=1500")
            .header("Authorization", "Bearer")
            .build()

        val response = okHttpClient.newCall(request).execute()

        val places: MutableList<places> = mutableListOf()
        val responseString: String? = response.body?.string()

        if (!responseString.isNullOrEmpty() && response.isSuccessful) {

        }

       return listOf(places("",5.0f, "", "", "", ""))
        //return listOf(places("","",""))
    }
}