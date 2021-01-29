package com.example.gamewither.intro

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamewither.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_intro_join.*

class IntroMainJoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var rdb: DatabaseReference
    var Backwait:Long=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_join)
        init()

        auth = FirebaseAuth.getInstance()// Initialize Firebase Auth
    }

    fun init() {

        var flag1 = 0
        var flag2 = 0
        var flag3=0
        var flag4=0


        introjoin_id.addTextChangedListener(object : TextWatcher {
            //이미 있는 id는 안됨
            override fun afterTextChanged(s: Editable?) {//
                if(!s.toString().contains("@")){
                    flag1 = 0
                    textInputLayout1.error = "올바른 이메일 형식이 아닙니다."

                }
                else {
                    flag1 = 1
                    textInputLayout1.error = null

                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //TODO("Not yet implemented")
            }

        })

        introjoin_nickname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length>0) {
                    textInputLayout2.error = null
                    flag2=1


                }else if(s.toString().length==0){
                    flag2=0
                    textInputLayout2.error = "닉네임을 입력해주세요."

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        introjoin_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length < 6) {
                    textInputLayout3.error = "비밀번호는 6자리 이상이어야 합니다."
                    flag3 = 0
                } else if (s.toString().length > 14) {
                    textInputLayout3.error = "비밀번호는 15자리 이하여야 합니다."
                    flag3 = 0
                } else {


                    textInputLayout3.error = null
                    flag3 = 1

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //TODO("Not yet implemented")
            }
        }
        )

        introjoin_passcheck.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length < 6) {
                    textInputLayout4.error = "비밀번호는 6자리 이상이어야 합니다."
                    flag4 = 0
                } else if (s.toString().length > 14) {
                    textInputLayout4.error = "비밀번호는 15자리 이하여야 합니다."
                    flag4 = 0
                } else {

                    if(introjoin_password.text.toString()!=s.toString()){
                        textInputLayout4.error = "비밀번호가 일치하지 않습니다."
                    }else {

                        textInputLayout4.error = null
                        flag4 = 1

                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //TODO("Not yet implemented")

            }
        }
        )


        introjoin_join.setOnClickListener {
            Log.i("join","회원가입 버튼 입력")
            if(flag1==1&&flag2==1&&flag3==1&&flag4==1) {
                Log.i("Join","회원가입 시작")
                introjoin_join.isEnabled = true
                rdb = FirebaseDatabase.getInstance().getReference("Wither")


                auth.createUserWithEmailAndPassword(
                    introjoin_id.text.toString(),
                    introjoin_password.text.toString()
                ).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        var user = auth.currentUser
                        //user.displayName=personname.toString()
                        Toast.makeText(baseContext, "회원가입 성공", Toast.LENGTH_SHORT).show()

                        val userinfo = WitherInfo("noinfo",introjoin_nickname.text.toString(), 0, 0)
                        if (user != null) {
                            rdb.child(user.uid).setValue(userinfo)
                            println(user.uid)
                        }


                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext, "회원가입 실패",
                            Toast.LENGTH_SHORT
                        ).show()
                        //updateUI(null)
                    }

                    // ...
                }
            }else {
                Log.i("Join","항목 덜 채움")
                Toast.makeText(this,"모든 항목을 입력해주세요",Toast.LENGTH_SHORT).show()
            }

        }


    }


}
