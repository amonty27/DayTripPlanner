package com.example.project1_daytripplanner_montgomery

data class places (
    val name: String,
    val rating : Float,
    val phoneNumber : String,
    val address : String,
    val pricePoint : String,
    val url : String,
    val lat : Double,
    val long : Double,
    val type : Int // 0 for food, 1 for activity. Using for differentiating map marker colors
)