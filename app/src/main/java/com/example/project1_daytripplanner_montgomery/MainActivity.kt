package com.example.project1_daytripplanner_montgomery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var foodNumber: TextView
    lateinit var foodSeekBar: SeekBar

    lateinit var activitySeekBar: SeekBar
    lateinit var activityNumber: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        foodSeekBar = findViewById(R.id.foodSeekBar) as SeekBar
        foodNumber = findViewById(R.id.foodNumber) as TextView

        activitySeekBar = findViewById(R.id.activitySeekBar) as SeekBar
        activityNumber = findViewById(R.id.activityNumber) as TextView


        foodSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                foodNumber.text = progress.toString()
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


    }
}
