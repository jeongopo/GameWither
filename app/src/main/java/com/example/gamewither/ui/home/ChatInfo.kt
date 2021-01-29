package com.example.gamewither.ui.home

data class ChatInfo(val uid:String, var time:String, var gid:String,var content:String){
    constructor():this("noinfo","99:99","noinfo","noinfo")


}