package com.example.project1_daytripplanner_montgomery


import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import android.util.Log
import okhttp3.internal.wait
import okhttp3.internal.waitMillis
import java.lang.Exception

/*
    Sometimes, it returns things that are outside of the 1500m radius and I am not sure how to fix that when
    I specify for it
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

    fun retrieveActivity(
        latitude: Double,
        longitude: Double,
        yelpApiKey: String,
        activitySpinnerName: String?,
        activitySeekBar: Int,
        foodSpinnerName: String?,
        foodSeekBar: Int
    ): ArrayList<places> {
        // get the name of the activity and
        val inputtedActivity = activitySpinnerName
        val inputtedActivityNumber = activitySeekBar
        val inputtedFood = foodSpinnerName
        val inputtedFoodNumber = foodSeekBar

        /*Log.d("liciTag", "value of inputtedActivity: " + inputtedActivity)
        Log.d("liciTag", "value of inputtedActivityNumber: " + inputtedActivityNumber)
        Log.d("liciTag", "value of inputtedFood: " + inputtedFood)
        Log.d("liciTag", "value of inputtedFoodNumber: " + inputtedFoodNumber)

        Log.d("liciTag", "value of yelpApiKey: " + yelpApiKey)*/
        //val yelpApiKey = getString()
        val request = Request.Builder()
            .url("https://api.yelp.com/v3/businesses/search?term=$inputtedActivity&latitude=$latitude&longitude=$longitude&limit=$inputtedActivityNumber&sort_by=rating&radius=1500")
            .header("Authorization", "$yelpApiKey")
            .build()

        val response = okHttpClient.newCall(request).execute()

        val places: ArrayList<places> = arrayListOf()
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
                // Log.d("liciTag", "this worked i guess yippy")
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
                    //Log.d("liciTag", "value of pricePoint: $pricePoint")
                    val json: JSONObject = JSONObject(responseString)
                    val url = curr.getString("url")
                    Log.d("liciTag", "url: $url")

                    val latLng = curr.getJSONObject("coordinates")
                    val lat = latLng.getDouble("latitude")
                    val long = latLng.getDouble("longitude")

                    val place = places(
                        name = name,
                        rating = rating,
                        phoneNumber = phoneNumber,
                        address = address,
                        pricePoint = pricePoint,
                        url = url,
                        lat = lat,
                        long = long,
                        type = 1
                    )

                    places.add(place)
                }


            } else {
                //Log.d("liciTag", "didnt work")
            }
        }

        if (inputtedFoodNumber != 0) {
            val request2 = Request.Builder()
                .url("https://api.yelp.com/v3/businesses/search?term=$inputtedFood&latitude=$latitude&longitude=$longitude&limit=$inputtedFoodNumber&sort_by=rating&radius=1500")
                .header("Authorization", "$yelpApiKey")
                .build()

            val response2 = okHttpClient.newCall(request2).execute()
            val responseString2: String? = response2.body?.string()

            if (!responseString2.isNullOrEmpty() && response2.isSuccessful) {
                //Log.d("liciTag", "this worked i guess yippy 2")
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
                    //Log.d("liciTag", "value of pricePoint: $pricePoint")
                    val json: JSONObject = JSONObject(responseString)
                    val url = curr2.getString("url")

                    Log.d("liciTag", "url: $url")

                    val latLng = curr2.getJSONObject("coordinates")
                    val lat = latLng.getDouble("latitude")
                    val long = latLng.getDouble("longitude")

                    val place = places(
                        name = name,
                        rating = rating,
                        phoneNumber = phoneNumber,
                        address = address,
                        pricePoint = pricePoint,
                        url = url,
                        lat = lat,
                        long = long,
                        type = 0
                    )

                    places.add(place)
                }
            } else {
                //Log.d("liciTag", "didnt work2")
            }
        }
        return places
    }

    /*data class metro (
        val name: String,
        val stationCode : String,
        val lat : Double,
        val long : Double
    ) : Serializable*/

    fun returnMetro(
        latitude: Double,
        longitude: Double,
        wmta_key: String,
        wmta_value: String
    ): ArrayList<metro> {


        val metros: ArrayList<metro> = arrayListOf()
        val metroName: ArrayList<String> = arrayListOf()
        val metroLat: ArrayList<Double> = arrayListOf()
        val metroLong: ArrayList<Double> = arrayListOf()
        val metroStationCode: ArrayList<String> = arrayListOf()

        // build the request
        val request = Request.Builder()
            .url("https://api.wmata.com/Rail.svc/json/jStationEntrances?Lat=$latitude&Lon=$longitude&Radius=1500")
            .header("$wmta_key", "$wmta_value")
            .build()
        val response = okHttpClient.newCall(request).execute()

        val responseString: String? = response.body?.string()
        if (!responseString.isNullOrEmpty() && response.isSuccessful) {
            Log.d("licitag", "wmta works")
            val json: JSONObject = JSONObject(responseString)
            val Entrances: JSONArray = json.getJSONArray("Entrances")
            for (i in 0 until 1) {
                val curr = Entrances.getJSONObject(i)

                val stationCode = curr.getString("StationCode1") //string
                val lat = curr.getDouble("Lat")
                val long = curr.getDouble("Lon")

                metroLat.add(lat)
                metroLong.add(long)
                metroStationCode.add(stationCode)
            }

        }

        metroStationCode.forEach {
            val request2 = Request.Builder()
                .url("https://api.wmata.com/Rail.svc/json/jStationInfo?StationCode=${it}")
                .header("$wmta_key", "$wmta_value")
                .build()

            val response2 = okHttpClient.newCall(request2).execute()
            //waitMillis(100)
            val responseString2: String? = response2.body?.string()
            if (!responseString2.isNullOrEmpty() && response2.isSuccessful) {
                val json2 = JSONObject(responseString2)
                metroName.add(json2.getString("Name"))
                Log.d("licitag", "station name : ${json2.getString("Name")}")
            }
        }

        for (i in 0 until metroName.size) {
            val metro = metro(
                name = metroName[i],
                stationCode = metroStationCode[i],
                lat = metroLat[i],
                long = metroLong[i]
            )

            metros.add(metro)
        }
        return metros
    }
}