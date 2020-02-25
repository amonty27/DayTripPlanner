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
       Ask, "Is there a way to not display the Dialog until the thread has returned the addresses?
       Geocoding error: - if there is no results, display "no results"
                        - if there is no network detectiction, toast "no network connectivity"
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
            val inputtedActivityName : String = activitySpinner.getItemAtPosition(inputtedActivity).toString()
            val inputtedFoodName : String = foodSpinner.getItemAtPosition(inputtedFood).toString()
            val inputtedFoodNumber : Int = foodSeekBar.getProgress()
            val inputtedActivityNumber : Int = activitySeekBar.getProgress()

            // save what the user entered to the shared preferences folder
            preferences
                .edit()
                .putString("userInput", inputtedUser)
                .putInt("activitySpinner", inputtedActivity)
                .putInt("foodSpinner", inputtedFood)
                .putString("activitySpinnerName", inputtedActivityName)
                .putString("foodSpinnerName", inputtedFoodName)
                .putInt("foodSeekBar", inputtedFoodNumber)
                .putInt("activitySeekBar", inputtedActivityNumber)
                .apply()

            // create an arrayAdapter to make a dialog and one to send through the intent
            val arrayAdapter = ArrayAdapter<Address>(this, android.R.layout.select_dialog_multichoice)
            val arrayAdapter2 = ArrayAdapter<String>(this, android.R.layout.select_dialog_multichoice)

            // create the alert diaolg
            val builder = AlertDialog.Builder(this)
            // set the title and other things to be displayed in the box
            builder.setTitle("Results")
            builder.setAdapter(arrayAdapter2) { dialog, which ->
                // create and start an intent to go to the MapsActivity
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra("result", arrayAdapter.getItem(which))
                startActivity(intent)
            }
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

            // Asynchronously access the geocoder class to get the result from users input
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
                    // the size of the results array of returned values from geocode
                    var length = results.size

                    // for debugging
                    Log.d("liciTag", "Value: " + length.toString());

                    // for all the values returned, add the addresses to the 2 arrayAdapters
                    for (i in 0 until length){
                        var what: Address = results.get(i)
                        runOnUiThread {
                            arrayAdapter.add(what)
                            arrayAdapter2.add(what.getAddressLine(0))
                        }
                    }
                }
            }
            //build the Radio Button view
            builder.show()

        }

        // initial state of checks should be false so the search button is not clickable until all fields are entered
        searchButton.isEnabled = false
        foodChange = false
        activityChange = false
        searchChange = false

        foodSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // parent is the current value selected in the spinner
                if (parent != null) {
                    // if the parent position does not equal the defualt of "Choose Food" set the check to true
                    if(!parent.getItemAtPosition(position).equals("Choose Food")){
                        foodChange = true
                    }
                    // if it is, set to false
                    if(parent.getItemAtPosition(position).equals("Choose Food")){
                        foodChange = false
                    }
                    // if all of the other vaules have been set, enable the search button
                    if(foodChange === true && activityChange === true && searchChange === true) {
                        searchButton.isEnabled = true
                    }
                    // if not, do not enable the search button
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
