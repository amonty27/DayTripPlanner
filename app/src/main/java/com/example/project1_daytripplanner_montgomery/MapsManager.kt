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
import java.lang.Exception

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

    fun retrieveActivity(latitude: Double, longitude: Double, sharedPreferences: SharedPreferences, yelpApiKey : String): List<places>{
        // get the name of the activity and
        val inputtedActivity = sharedPreferences.getString("activitySpinnerName", "")
        val inputtedActivityNumber = sharedPreferences.getInt("activitySeekBar", 0)
        val inputtedFood = sharedPreferences.getString("foodSpinnerName","")
        val inputtedFoodNumber = sharedPreferences.getInt("foodSeekBar",0)

        Log.d("liciTag", "value of inputtedActivity: " + inputtedActivity)
        Log.d("liciTag", "value of inputtedActivityNumber: " + inputtedActivityNumber)
        Log.d("liciTag", "value of inputtedFood: " + inputtedFood)
        Log.d("liciTag", "value of inputtedFoodNumber: " + inputtedFoodNumber)

        Log.d("liciTag", "value of yelpApiKey: " + yelpApiKey)
        //val yelpApiKey = getString()
        val request = Request.Builder()
            .url("https://api.yelp.com/v3/businesses/search?term=$inputtedActivity&latitude=$latitude&longitude=$longitude&limit=$inputtedActivityNumber&sort_by=rating&radius=1500")
            .header("Authorization", "$yelpApiKey")
            .build()

        val response = okHttpClient.newCall(request).execute()

        val places: MutableList<places> = mutableListOf()
        val responseString: String? = response.body?.string()

        /*data class places (
            val name: String,
            val rating : Float,
            val phoneNumber : String,
            val Address : String,
            val pricePoint : String,
            val url : String
            val lat : Float,
            val long : Float
        )*/
        if (inputtedActivityNumber != 0) {
            if (!responseString.isNullOrEmpty() && response.isSuccessful) {
                Log.d("liciTag", "this worked i guess yippy")
                val json: JSONObject = JSONObject(responseString)
                val businesses: JSONArray = json.getJSONArray("businesses")

                for (i in 0 until businesses.length()) {
                    val curr = businesses.getJSONObject(i)

                    val name = curr.getString("name") //string
                    val rating = curr.getInt("rating").toFloat() // decimal
                    val phoneNumber = curr.getString("phone")

                    val location = curr.getJSONObject("location")
                    val address = location.getString("address1")
                    val pricePoint = try {
                        curr.getString("price")
                    } catch (exception: Exception) {
                        ""
                    }
                    Log.d("liciTag", "value of pricePoint: $pricePoint")
                    val json: JSONObject = JSONObject(responseString)
                    val url = curr.getString("url")

                    val latLng = curr.getJSONObject("coordinates")
                    val lat = latLng.getInt("latitude").toFloat()
                    val long = latLng.getInt("longitude").toFloat()

                    val place = places(
                        name = name,
                        rating = rating,
                        phoneNumber = phoneNumber,
                        address = address,
                        pricePoint = pricePoint,
                        url = url,
                        lat = lat,
                        long = long
                    )

                    places.add(place)
                }


            } else {
                Log.d("liciTag", "didnt work")
            }
        }

        if(inputtedFoodNumber != 0) {
            val request2 = Request.Builder()
                .url("https://api.yelp.com/v3/businesses/search?term=$inputtedFood&latitude=$latitude&longitude=$longitude&limit=$inputtedFoodNumber&sort_by=rating&radius=1500")
                .header("Authorization", "$yelpApiKey")
                .build()

            val response2 = okHttpClient.newCall(request2).execute()
            val responseString2: String? = response2.body?.string()

            if (!responseString2.isNullOrEmpty() && response2.isSuccessful) {
                Log.d("liciTag", "this worked i guess yippy 2")
                val json2: JSONObject = JSONObject(responseString2)
                val businesses2: JSONArray = json2.getJSONArray("businesses")

                for (i in 0 until businesses2.length()) {
                    val curr2 = businesses2.getJSONObject(i)

                    val name = curr2.getString("name") //string
                    val rating = curr2.getInt("rating").toFloat() // decimal
                    val phoneNumber = curr2.getString("phone")

                    val location = curr2.getJSONObject("location")
                    val address = location.getString("address1")
                    val pricePoint = try {
                        curr2.getString("price")
                    } catch (exception: Exception) {
                        ""
                    }
                    Log.d("liciTag", "value of pricePoint: $pricePoint")
                    val json: JSONObject = JSONObject(responseString)
                    val url = curr2.getString("url")

                    val latLng = curr2.getJSONObject("coordinates")
                    val lat = latLng.getInt("latitude").toFloat()
                    val long = latLng.getInt("longitude").toFloat()

                    val place = places(
                        name = name,
                        rating = rating,
                        phoneNumber = phoneNumber,
                        address = address,
                        pricePoint = pricePoint,
                        url = url,
                        lat = lat,
                        long = long
                    )

                    places.add(place)
                }
            } else {
                Log.d("liciTag", "didnt work2")
            }
        }
       return places
    }
}