package com.example.project1_daytripplanner_montgomery

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get

/* TODO :   Add shared preferences for the foodSpinner and activitySpinner
            Show previously entered values in the Address, foodSpinner, activitySpinner, foodSeekBar, and activitySeekBar
            Enable the search button when address, foodspinner, and activityspinner options have been chosen
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
            // save what the user entered to the shared preferences folder
            preferences
                .edit()
                .putString("userInput", inputtedUser.toString())
                .apply()

            val array = listOf(preferences.getString("userInput", ""))
            val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice)
            arrayAdapter.addAll(array)
            val builder = AlertDialog.Builder(this)

            builder.setTitle("dialog Box")
                .setAdapter(arrayAdapter) { dialog, which ->
                    Toast.makeText(
                        this,
                        "You picked: ${preferences.getString("userInput", "")}",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            ///builder.setSingleChoiceItems(array,-1)
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
                // if not  null and the position is not at the default, set

                if (parent != null) {
                    if(!parent.getItemAtPosition(position).equals("Choose Food")){
                        //searchButton.isEnabled = true
                        foodChange = true
                    }
                    if(parent.getItemAtPosition(position).equals("Choose Food")){
                        //searchButton.isEnabled = true
                        foodChange = false
                    }
                    if(foodChange === true && activityChange === true && searchChange === true) {
                        searchButton.isEnabled = true
                        //foodChange = true
                    }
                    else{searchButton.isEnabled = false}
                }
            }
        }

       activitySpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // if not  null and the position is not at the default, set

                if (parent != null) {
                    if(!parent.getItemAtPosition(position).equals("Choose Activity")){
                        //searchButton.isEnabled = true
                        activityChange = true
                    }
                    if(parent.getItemAtPosition(position).equals("Choose Activity")){
                        //searchButton.isEnabled = true
                        activityChange = false
                    }
                    if(foodChange && activityChange && searchChange) {
                        searchButton.isEnabled = true
                        //foodChange = true
                    }
                    else{searchButton.isEnabled = false}
                }
            }
        }

        //foodSpinner.setOnItemSelectedListener(spinnerWatcher)
        //activitySpinner.setOnItemSelectedListener(spinnerWatcher)

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

        val savedSearch = preferences.getString("userInput", "")

        userInput.setText(savedSearch)
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

    }

}
