package com.richa.applicationproject.model

import java.util.ArrayList

data class FirebaseUserDataProject(
    var dName:String,
    var dEmail:String,
    var dPhone:String,
    var dPassword:String,
    var dOrderHistory: ArrayList<String> = ArrayList(),
    var dAge:String="",
    var dGender:String="",
    var dInterests:String=""
)