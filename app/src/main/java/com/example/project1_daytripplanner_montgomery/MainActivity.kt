package com.example.project1_daytripplanner_montgomery

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
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
    TODO:
        Geocoding and Geocoding Errors
 */
class MainActivity : AppCompatActivity() {

    lateinit var foodNumber: TextView
    lateinit var foodSeekBar: SeekBar

    lateinit var activitySeekBar: SeekBar
    lateinit var activityNumber: TextView

    lateinit var searchButton : Button

    lateinit var userInput: EditText

    lateinit var foodSpinner: Spinner
    lateinit var activitySpinner: Spinner

    var foodChange : Boolean = false
    var activityChange : Boolean = false
    var searchChange : Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set up the saved preferences to save the Address
        val preferences = getSharedPreferences("dayTripPlaner_data", Context.MODE_PRIVATE)

        //initializing variables
        foodSeekBar = findViewById(R.id.foodSeekBar)
        foodNumber = findViewById(R.id.foodNumber)
        activitySeekBar = findViewById(R.id.activitySeekBar)
        activityNumber = findViewById(R.id.activityNumber)
        searchButton = findViewById(R.id.searchButton)
        userInput = findViewById(R.id.address)
        foodSpinner = findViewById(R.id.foodSpin)
        activitySpinner = findViewById(R.id.activitySpin)

        // set a listener for the search button to perform certain actions
        searchButton.setOnClickListener{ view: View ->

            // Save user credentials to file
            val inputtedUser: String = userInput.text.toString()
            val inputtedActivity : Int = activitySpinner.firstVisiblePosition
            val inputtedFood : Int = foodSpinner.firstVisiblePosition
            val inputtedFoodNumber : Int = foodSeekBar.getProgress()
            val inputtedActivityNumber : Int = activitySeekBar.getProgress()

            // save what the user entered to the shared preferences folder
            preferences
                .edit()
                .putString("userInput", inputtedUser)
                .putInt("activitySpinner", inputtedActivity)
                .putInt("foodSpinner", inputtedFood)
                .putInt("foodSeekBar", inputtedFoodNumber)
                .putInt("activitySeekBar", inputtedActivityNumber)
                .apply()

            val array = preferences.getString("userInput", "")
            val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice)

            doAsync{
                val geocoder = Geocoder(this@MainActivity)
                val results: List<Address> = try {
                 geocoder.getFromLocationName(preferences.getString("userInput",""),5)
                }
                catch (exception : Exception){
                    exception.printStackTrace()
                    listOf<Address>()
                }

                if (results.isNotEmpty()){
                    for (i in 0 until 5){
                        var what = results.get(i)
                        runOnUiThread {
                            arrayAdapter.add(what.getAddressLine(0))
                        }
                    }
                }
            }
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Results")
                .setAdapter(arrayAdapter) { dialog, which ->
                    val intent: Intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
            //build the Radio Button view
            builder.show()


        }

        searchButton.isEnabled = false
        foodChange = false
        activityChange = false
        searchChange = false

        foodSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (parent != null) {
                    if(!parent.getItemAtPosition(position).equals("Choose Food")){
                        foodChange = true
                    }
                    if(parent.getItemAtPosition(position).equals("Choose Food")){
                        foodChange = false
                    }
                    if(foodChange === true && activityChange === true && searchChange === true) {
                        searchButton.isEnabled = true
                    }
                    else{searchButton.isEnabled = false}
                }
            }
        }

       activitySpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // if not  null and the position is not at the default, set

                if (parent != null) {
                    if(!parent.getItemAtPosition(position).equals("Choose Activity")){
                        activityChange = true
                    }
                    if(parent.getItemAtPosition(position).equals("Choose Activity")){
                        activityChange = false
                    }
                    if(foodChange && activityChange && searchChange) {
                        searchButton.isEnabled = true
                    }
                    else{searchButton.isEnabled = false}
                }
            }
        }

        // add a listener to the userInput Field to track changes
        userInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val inputtedUser : String = userInput.text.toString()
                // check that the code has not been changed and if not enable the search button
                searchChange = inputtedUser.trim().isNotEmpty()

                if(foodChange && activityChange && searchChange) {
                    searchButton.isEnabled = true
                }
                else{searchButton.isEnabled = false}
            }
        })

        // create a listener for the SeekBar used for the food
        foodSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //update the number beside the seek bar with the value the seek bar is at currently
                foodNumber.text = progress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        activitySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {activityNumber.text = progress.toString()}
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // set the values of the userInput, Spinners, and SeekBars from the shared preferences
        userInput.setText(preferences.getString("userInput", ""))
        activitySpinner.setSelection(preferences.getInt("activitySpinner",-1))
        activitySeekBar.setProgress(preferences.getInt("activitySeekBar",-1))
        foodSpinner.setSelection(preferences.getInt("foodSpinner",-1))
        foodSeekBar.setProgress(preferences.getInt("foodSeekBar",-1))

    }

}
