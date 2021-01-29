package com.example.gamewither.intro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.gamewither.MainActivity
import com.example.gamewither.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


class IntroActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var rdb: DatabaseReference
    var Backwait:Long=0
    val resnum:Int=500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        init()
        auth = FirebaseAuth.getInstance()



    }

    public override fun onStart() {
        super.onStart()

        // Firebase Login API를 통해 로그인 여부를 판단
        val currentUser = auth.currentUser
        if(currentUser==null){ //로그인 안되어있을 시 로그인 선택화면으로 이동
            val i = Intent(this, IntroMainActivity::class.java)

            Toast.makeText(applicationContext, "GameWither에 오신 것을 환영합니다", Toast.LENGTH_SHORT).show()
            Handler().postDelayed(Runnable {
                startActivityForResult(i,resnum)
            }, 4000L)
        }else{Log.i("intro","로그인 상태 "+currentUser.uid)
            Handler().postDelayed(Runnable {
            updateUI(currentUser) 
            }, 4000L)

        }
    }

    fun updateUI(currentUser: FirebaseUser) { //로그인 상태일 경우, 유저 정보를 업데이트하는 함수
        val i = Intent(this, MainActivity::class.java)
        lateinit var name: String
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url

            rdb = FirebaseDatabase.getInstance().getReference("Wither")

            rdb.child(user.uid).child("uid").setValue(user.uid)
            //Firebase RealDatabase API로부터 데이터 값을 한번 가지고오는 코드
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
                                startActivityForResult(i, resnum)
                            } else Toast.makeText(
                                applicationContext,
                                "로그인 정보를 얻는데 실패했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
//
//                            FirebaseAuth.getInstance().signOut()
//                            val i=Intent(baseContext,IntroMainActivity::class.java)
//                            startActivity(i)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
        }
    }


    fun init(){ //인트로 애니메이션 효과
        val image: ImageView = findViewById(R.id.intro_logo)
        val hyperspaceJump: Animation = AnimationUtils.loadAnimation(this, R.anim.alpha)
        image.startAnimation(hyperspaceJump)


    }
    override fun onBackPressed() { //뒤로가기 버튼을 클릭하면 앱이 종료되도록 하는 이벤트

        if(System.currentTimeMillis()-Backwait>=2000) {
            Backwait = System.currentTimeMillis()
            Toast.makeText(applicationContext, "뒤로가기 버튼을 한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
        }else{
            super.onBackPressed()
            ActivityCompat.finishAffinity(this); 
            System.exit(0); //모든 액티비티 종료하고, 바로 앱 종료하기
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { 
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==resnum) {
            if(resultCode == Activity.RESULT_CANCELED){ // 만약 이전 액티비티에서 뒤로가기를 통해 돌아왔을 경우, 종료되도록 함
                finish()
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().signOut() // 어떠한 에러로 Destroy 되었을 때, 로그인을 해제함
    }
}
