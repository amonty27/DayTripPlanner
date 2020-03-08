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
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.row_details.*
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity() {

    lateinit var foodNumber: TextView
    lateinit var foodSeekBar: SeekBar

    lateinit var activitySeekBar: SeekBar
    lateinit var activityNumber: TextView

    lateinit var searchButton : Button

    lateinit var userInput: EditText

    lateinit var foodSpinner: Spinner
    lateinit var activitySpinner: Spinner

    lateinit var progressBar : ProgressBar

    var foodChange : Boolean = false
    var activityChange : Boolean = false
    var searchChange : Boolean = false
    var activityNumberChange : Boolean = false
    var foodNumberChange : Boolean = false


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
        progressBar = findViewById(R.id.progressBar)

        progressBar.isVisible = false
        // set a listener for the search button to perform certain actions
        searchButton.setOnClickListener{ view: View ->

            // Save user credentials to file
            val inputtedUser: String = userInput.text.toString()
            val inputtedActivity : Int = activitySpinner.selectedItemPosition
            val inputtedFood : Int = foodSpinner.selectedItemPosition
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
                        intent.putExtra("address", arrayAdapter.getItem(which))
                        intent.putExtra("activitySpinnerName", inputtedActivityName)
                        intent.putExtra("activitySeekBar", inputtedActivityNumber)
                        intent.putExtra("foodSpinnerName", inputtedFoodName)
                        intent.putExtra("foodSeekBar", inputtedFoodNumber)
                        startActivity(intent)
                }
                builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

                // Asynchronously access the geocoder class to get the result from users input
                doAsync{
                    val geocoder = Geocoder(this@MainActivity)
                    runOnUiThread { progressBar.isVisible = true }
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
                        runOnUiThread {  progressBar.isVisible = false; builder.show() }
                    }
                    else{
                        runOnUiThread {
                             progressBar.isVisible = false
                            Toast.makeText(this@MainActivity, "No Results", Toast.LENGTH_SHORT).show()
                        }
                    }

                }


        }

        // initial state of checks should be false so the search button is not clickable until all fields are entered
        searchButton.isEnabled = false


        foodSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                    // if the parent position does not equal the defualt of "Choose Food" set the check to true
                    if(parent.selectedItemPosition != 0){
                        foodChange = true
                    }
                    // if it is, set to false
                    if(parent.selectedItemPosition == 0){
                        foodChange = false
                    }
                    // if all of the other vaules have been set, enable the search button
                    if(foodChange && activityChange && searchChange && (activityNumberChange||foodNumberChange)) {
                        searchButton.isEnabled = true
                    }
                    // if not, do not enable the search button
                    else{
                        searchButton.isEnabled = false
                    }
            }
        }

        activitySpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // if not  null and the position is not at the default, set

                    if(parent.selectedItemPosition != 0){
                        activityChange = true
                    }
                    if(parent.selectedItemPosition == 0){
                        activityChange = false
                    }
                    if(foodChange && activityChange && searchChange && (activityNumberChange||foodNumberChange)) {
                        searchButton.isEnabled = true
                    }
                    else{searchButton.isEnabled = false}
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

                if(foodChange && activityChange && searchChange && (activityNumberChange||foodNumberChange)) {
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
                if(progress == 0){
                    foodNumberChange = false
                }
                if(progress !=0 ){
                    foodNumberChange = true
                }

                if(foodChange && activityChange && searchChange && (activityNumberChange||foodNumberChange)) {
                    searchButton.isEnabled = true
                }
                else{searchButton.isEnabled = false}
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        activitySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                activityNumber.text = progress.toString()
                if(progress == 0){
                    activityNumberChange = false
                }
                if(progress !=0 ){
                    activityNumberChange = true
                }

                if(foodChange && activityChange && searchChange && (activityNumberChange||foodNumberChange)) {
                    searchButton.isEnabled = true
                }
                else{searchButton.isEnabled = false}
            }
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
