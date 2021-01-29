package com.example.gamewither.ui.home

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamewither.R
import com.example.gamewither.intro.WitherInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_user_profile_acitivty.*

class UserProfileAcitivty : AppCompatActivity() {

    var joinflag=0 // 0이면 친구x, 1이면 친구
    var myinfo=WitherInfo("noinfo","noinfo",0,0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile_acitivty)
        init()
    }

    fun init() {
        val uid = intent.getStringExtra("uid")
        var userinfo = WitherInfo("noinfo","noinfo", 0, 0)
        val rdb = FirebaseDatabase.getInstance().getReference("Wither/" + uid)
        val event = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userinfo = snapshot.getValue(WitherInfo::class.java) as WitherInfo
                Log.i("Profile","다른 유저 정보 변경")
                pro_usernick.text = userinfo.nickname
//                pro_usergid.text = intent.getStringExtra("gid")
                pro_with.text = userinfo.withcount.toString()
                pro_wither.text = userinfo.withercount.toString()
            }

        }
        rdb.addValueEventListener(event)

        pro_usernick.text = userinfo.nickname
        pro_usergid.text = intent.getStringExtra("gid")
        pro_with.text = userinfo.withcount.toString()
        pro_wither.text = userinfo.withercount.toString()


//        //친구추가
//        val join = FirebaseDatabase.getInstance()
//            .getReference("Join/" + uid )
//        val ChildEventListener = object : ChildEventListener {
//            override fun onCancelled(error: DatabaseError) {
////
//            }
//
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//            }
//
//            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//            }
//
//            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                if(snapshot.getValue(String::class.java)==FirebaseAuth.getInstance().currentUser!!.uid){
//                    joinflag = 1 //친구추가 됐음
//                }
//            }
//
//            override fun onChildRemoved(snapshot: DataSnapshot) {
//                if(snapshot.getValue(String::class.java)==FirebaseAuth.getInstance().currentUser!!.uid){
//                    joinflag = 0 //친구추가 삭제됨
//                }
//            }
//        }

//        join.addChildEventListener(ChildEventListener)

        val wither = FirebaseDatabase.getInstance()
            .getReference("Wither/" + FirebaseAuth.getInstance().currentUser!!.uid)

        val valueEventListener=object:ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val tem = snapshot.getValue(WitherInfo::class.java)
                Log.i("Profile","내 정보 변경")
                if(tem!=null){
                    myinfo.nickname=tem.nickname
                    myinfo.withcount=tem.withcount
                    myinfo.withercount=tem.withercount
                }
            }
        }
        wither.addValueEventListener(valueEventListener)

        pro_sendreq.setOnClickListener {
            if (joinflag == 1) {
                //친구상태
                Toast.makeText(this, "이미 친구입니다", Toast.LENGTH_SHORT).show()
            } else {
                val tem = FirebaseDatabase.getInstance().getReference("Join/" + uid+"/")
//                tem.setValue(FirebaseAuth.getInstance().currentUser!!.uid)
                tem.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(userinfo)
                val tem2 = FirebaseDatabase.getInstance().getReference("Join/" + FirebaseAuth.getInstance().currentUser!!.uid+"/")
//                tem2.setValue(uid)
                tem2.child(uid).setValue(myinfo)

//                wither.child("withercount").setValue((myinfo.withercount)+1)
                    joinflag=1
                }
            }


    }
}
