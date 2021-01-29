package com.example.gamewither.intro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.gamewither.MainActivity
import com.example.gamewither.R
import com.facebook.AccessToken
import com.facebook.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_intro_main.*

class IntroMainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var rdb: DatabaseReference
    var Backwait:Long=0
    val resnum:Int=500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_main)
        init()
    }

    fun init(){
        val gojoin=findViewById<Button>(R.id.intromain_join)
        gojoin.setOnClickListener {
            val i=Intent(this,IntroMainJoinActivity::class.java)
            startActivityForResult(i,100)
        }

        intromain_gologin.setOnClickListener {
            val i=Intent(this,IntroLoginActivity::class.java)
            startActivityForResult(i,200)
        }

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }else{
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        when(requestCode){
            100 -> {
                val i=Intent(this, IntroLoginActivity::class.java)
                startActivityForResult(i,200)
            }
            200 -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.i("IntroMain! ::", "로그인 성공")
                    auth = FirebaseAuth.getInstance()
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        updateUI(currentUser)
                    } else {
                        Log.i("IntroMain! ::", "로그인은 성공했는데 데이터 없음")
                    }
                }
            }
            }

    }

    fun updateUI(currentUser: FirebaseUser) {
        Log.i("IntroMain! ::", "updateUI 실행")
        val i = Intent(this, MainActivity::class.java)
        lateinit var name: String
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url

            rdb = FirebaseDatabase.getInstance().getReference("Wither")


            rdb.child(user.uid).addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // Get user value
                        if(dataSnapshot!=null) {
                            val getuser = dataSnapshot.getValue(WitherInfo::class.java)
                            if (getuser != null) {
                                val senduser = WitherInfo(
                                    user.uid,
                                    getuser.nickname,
                                    getuser.withcount,
                                    getuser.withercount
                                )
                                i.putExtra("wither", senduser)
                                println("바로 실행?")

                                Toast.makeText(
                                    applicationContext,
                                    getuser.nickname + "님 환영합니다!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.i("Intromain! ::", "updateUI, 이동")
                                startActivityForResult(i, resnum)
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "초기 데이터를 설정합니다.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val userinfo = WitherInfo("noinfo",Profile.getCurrentProfile().name, 0, 0)
                                if (user != null) {
                                    rdb.child(user.uid).setValue(userinfo)
                                    println(user.uid)
                                }
                                startActivityForResult(i, resnum)
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
//            println("user이름 : "+myself.snapshots[0])


//            i.putExtra("guestName", name)

//            Toast.makeText(this,myself.snapshots[0].toString()+"님 환영합니다",Toast.LENGTH_SHORT).show()


        }
    }


    override fun onBackPressed() {

        if(System.currentTimeMillis()-Backwait>=2000) {
            Backwait = System.currentTimeMillis()
            Toast.makeText(applicationContext, "뒤로가기 버튼을 한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
        }else{
            super.onBackPressed()
            onDestroy()
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        }
    }
}
