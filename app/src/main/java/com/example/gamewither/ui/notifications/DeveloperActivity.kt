package com.example.gamewither.ui.notifications

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gamewither.R
import kotlinx.android.synthetic.main.activity_developer.*

class DeveloperActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer)
        init()
    }

    fun init(){
        dev_backicon.setOnClickListener{
            finish()
        }
    }
}
