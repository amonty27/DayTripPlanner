package com.example.project1_daytripplanner_montgomery

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

    lateinit var foodChange : seekBarChange
    lateinit var activityChange : seekBarChange



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initializing variables
        foodSeekBar = findViewById(R.id.foodSeekBar)
        foodNumber = findViewById(R.id.foodNumber)


        activitySeekBar = findViewById(R.id.activitySeekBar)
        activityNumber = findViewById(R.id.activityNumber)

        searchButton = findViewById(R.id.searchButton)

        userInput = findViewById(R.id.address)

        foodSpinner = findViewById(R.id.foodSpin)
        activitySpinner = findViewById(R.id.activitySpin)

        // for future use
        /*foodChange = seekBarChange.NOTCHANGED
        activityChange = seekBarChange.NOTCHANGED*/

        // set the search button to false so that the user cannot search until conditions cleared
        searchButton.isEnabled = false

        //set up the saved preferences to save the Address
        val preferences = getSharedPreferences("dayTripPlaner_data", Context.MODE_PRIVATE)


        // create a listener for the SeekBar used for the food
        foodSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //update the number beside the seek bar with the value the seek bar is at currently
                foodNumber.text = progress.toString()
                foodChange = seekBarChange.CHANGED
                //init = 1
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })








        activitySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                activityNumber.text = progress.toString()
                activityChange = seekBarChange.CHANGED
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })


        // add a listener to the userInput Field to track changes
        userInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                preferences.edit().putString("SEARCH", userInput.text.toString()).apply()
                searchButton.isEnabled = true
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //userInput = preferences.getString("SEARCH","")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchButton.isEnabled = true
            }
        })


               //searchButton.isEnabled = true
               searchButton.setOnClickListener {
                   val array = listOf(preferences.getString("SEARCH", ""))
                   val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice)
                   arrayAdapter.addAll(array)
                   val builder = AlertDialog.Builder(this)

                   builder.setTitle("dialog Box")
                       .setAdapter(arrayAdapter) { dialog, which ->
                           Toast.makeText(
                               this,
                               "You picked: ${preferences.getString("SEARCH", "")}",
                               Toast.LENGTH_SHORT
                           ).show()
                       }


                   ///builder.setSingleChoiceItems(array,-1)
                   builder.show()
               }
    }

}
enum class seekBarChange{
    NOTCHANGED,CHANGED
}