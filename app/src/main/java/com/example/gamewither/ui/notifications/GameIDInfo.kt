package com.example.gamewither.ui.notifications

import java.io.Serializable

data class GameIDInfo(var gamename:String, var gameid:String) :Serializable {
    constructor(): this("noinfo","noinfo")
}