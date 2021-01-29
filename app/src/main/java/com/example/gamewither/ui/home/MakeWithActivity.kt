package com.example.gamewither.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gamewither.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_make_with.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MakeWithActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var rdb:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_with)
        init()
    }

    fun init(){
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if(currentUser!=null) { //로그인한 경우
            lateinit var name: String
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                val formatted = current.format(formatter)

                rdb = FirebaseDatabase.getInstance().getReference("With/" + user.uid+"|"+formatted)
                print("확인 : "+formatted)

                make_mkbtn.setOnClickListener {
                    val newwith = WithInfo(
                        user.uid+"|"+formatted,
                        1,
                        make_gamename.text.toString(),
                        make_withtitle.text.toString(),
                        1,
                        (make_count.text.toString().toInt()),
                        currentUser.uid,
                        currentUser.uid,
                        "noinfo"
                    )
                    rdb.setValue(newwith)
                    val i= Intent(this,WitherInActivity::class.java)
                    i.putExtra("withinfo",newwith)

                    i.putExtra("gid",make_gameid.text.toString())
                    startActivity(i)
                }
            make_cancelbtn.setOnClickListener{
                finish()
            }

            }
        }
    }
}
