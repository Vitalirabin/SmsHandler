package com.example.smshandler

data class PostModel(
    val version:String,
    val sender:String,
    val message:String,
    val phone_number:String,
    val ip:String
)
