package com.example.project1_daytripplanner_montgomery

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

class MainActivity : AppCompatActivity() {

    lateinit var foodNumber: TextView
    lateinit var foodSeekBar: SeekBar

    lateinit var activitySeekBar: SeekBar
    lateinit var activityNumber: TextView

    lateinit var searchButton : Button

    lateinit var userInput: EditText

    lateinit var foodSpinner: Spinner
    lateinit var activitySpinner: Spinner



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


        // set the search button to false so that the user cannot search until conditions cleared
        //searchButton.isEnabled = false
        //set up the saved preferences to save the Address
        val preferences = getSharedPreferences("dayTripPlaner_data", Context.MODE_PRIVATE)

        //foodSpinner = preferences.getString("FOOD_SPIN","")









        foodSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                foodNumber.text = progress.toString()
                //preferences.edit().putString("FOOD_SPIN", foodSpinner.toString().get(1).toString())
                    //userInput.text
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })









        activitySeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                activityNumber.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })








        userInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                preferences.edit().putString("SEARCH",userInput.text.toString()).apply()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        searchButton.setOnClickListener{
            val array = listOf(preferences.getString("SEARCH",""))
            val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice)
            arrayAdapter.addAll(array)
            val builder = AlertDialog.Builder(this)

            builder.setTitle("dialog Box")
                .setAdapter(arrayAdapter) { dialog, which ->
                    Toast.makeText(this, "You picked: ${preferences.getString("SEARCH","")}", Toast.LENGTH_SHORT).show()
                }



            ///builder.setSingleChoiceItems(array,-1)
            builder.show()
        }



    }
}
