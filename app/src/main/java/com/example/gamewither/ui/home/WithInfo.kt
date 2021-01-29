package com.example.gamewither.ui.home

import java.io.Serializable

data class WithInfo(var roomid:String,var gamenum:Int, var gamename:String, var withtitle : String, var withnowcount:Int,var withmaxcount:Int,var withboss:String, var user:String, var finish:String):Serializable {
    constructor():this(
        "noinfo",
        -1,
        "noinfo",
        "noinfo",
        1,
        1,
        "noinfo",
        "noinfo",
    "noinfo")
// roomid : 게임 방 구분하는 ID, uid+20201102 , gamenum : 게임 데이터 상 번호, gamename: 게임이름
// var withtitle: 위드 방 제목, withcount: 위드 방 사람 수, user : String으로 이어져있는 유저 정보
}