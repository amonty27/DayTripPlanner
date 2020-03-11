package com.example.project1_daytripplanner_montgomery

import java.io.Serializable

data class metro (
    val name: String,
    val stationCode : String,
    val lat : Double,
    val long : Double
) : Serializable