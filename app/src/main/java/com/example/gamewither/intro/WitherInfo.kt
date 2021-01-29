package com.example.gamewither.intro

import java.io.Serializable

data class WitherInfo(var uid:String, var nickname:String, var withcount:Int, var withercount:Int) : Serializable {
    constructor():this("noinfo","noinfo",0,0)
}