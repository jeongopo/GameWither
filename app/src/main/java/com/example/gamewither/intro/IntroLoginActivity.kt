package com.example.gamewither.intro

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.gamewither.MainActivity
import com.example.gamewither.R
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_intro_login.*


class IntroLoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var rdb: DatabaseReference
    lateinit var callbackManager:CallbackManager
    var Backwait:Long=0


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_login)

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        init()

        facebooklogin() //콜백 리스너 부착해줌

    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun init(){ //Firebase Login API를 통해 이메일 로그인하기

        auth = FirebaseAuth.getInstance()

        intrologin_login.setOnClickListener {
            if(intrologin_id.text.isNotBlank()&&intrologin_pass.text.isNotBlank()) {
                //모든 항목이 입력되어 있을 때 로그인 진행
                auth.signInWithEmailAndPassword(
                    intrologin_id.text.toString(),
                    intrologin_pass.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information

                            val user = auth.currentUser

                            setResult(Activity.RESULT_OK)
                            finish()

                        } else {
                            // If sign in fails, display a message to the user.
                            setResult(Activity.RESULT_CANCELED)

                            Toast.makeText(
                                baseContext, "로그인 실패, 다시 로그인해주세요",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                        // ...
                    }
            }else {
                Toast.makeText(baseContext,"모든 항목을 입력해주세요",Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun facebooklogin(){ //Facebook Login API를 통해 로그인
        callbackManager = CallbackManager.Factory.create();


        val loginButton = findViewById<View>(R.id.intrologin_facebook) as LoginButton
        loginButton.setReadPermissions("email")

        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                if (loginResult != null) {
                    Log.i("IntroLogin! ::", "회원가입 성공")
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                rdb = FirebaseDatabase.getInstance().getReference("Wither")

                println("유저id "+ (auth.currentUser?.uid))

                val i=Intent(baseContext, IntroActivity::class.java)
                startActivity(i)
            }

            override fun onCancel() {
                Toast.makeText(
                    baseContext, "회원가입을 취소하였습니다",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(
                    baseContext, "회원가입 실패, 다시 로그인해주세요",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    private fun handleFacebookAccessToken(token: AccessToken) { //Firebase Loing API가 제공한 로그인 처리 함수
        Log.d("IntroLogin! ", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    rdb = FirebaseDatabase.getInstance().getReference("Wither")
                    Log.d("IntroLogin! ", "signInWithCredential:success")
                    val userinfo = WitherInfo("noinfo",Profile.getCurrentProfile().name, 0, 0)

                    Log.i("IntroLogin! in handleFacebookAccessToken ::", "로그인 성공 + 이름 "+Profile.getCurrentProfile().name)

                    var user = auth.currentUser
                    if (user != null) {
                        rdb.child(user.uid).setValue(userinfo)
                        println(user.uid)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("IntroLogin! ", "signInWithCredential:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                    // ...
                }
            }

    }

    fun updateUI(currentUser: FirebaseUser) { //바로 로그인하는 경우, 유저 정보 업데이트
        Log.i("IntroLogin! ::", "updateUI 실행")
        val i = Intent(this, MainActivity::class.java)
        lateinit var name: String
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url

            rdb = FirebaseDatabase.getInstance().getReference("Wither")

            rdb.child(user.uid).child("uid").setValue(user.uid)


            rdb.child(user.uid).addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // Get user value
                        if (dataSnapshot != null) {
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
                                startActivityForResult(i, 500)
                            } else Toast.makeText(
                                applicationContext,
                                "로그인 정보를 얻는데 실패했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }



}
