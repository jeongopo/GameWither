package com.example.gamewither

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gamewither.ui.home.HomeFragment
import com.example.gamewither.ui.home.WithInfo
import com.example.gamewither.ui.home.WitherInActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity(), HomeFragment.OnListFragmentInteractionListener {
    var Backwait:Long=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
//      아이콘 사이즈 늘리기
//        for (i in 0 until navView.childCount) {
//            val iconView: View =
//                navView.getChildAt(i).findViewById(R.id.icon)
//            val layoutParams: ViewGroup.LayoutParams = iconView.layoutParams
//            val displayMetrics = resources.displayMetrics
//            // set your height here
//            layoutParams.height =
//                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64f, displayMetrics).toInt()
//            // set your width here
//            layoutParams.width =
//                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64f, displayMetrics).toInt()
//            iconView.setLayoutParams(layoutParams)
//        }



        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val actionbar=supportActionBar
        if (actionbar != null) {
            actionbar.hide()
        }
    }

    override fun onBackPressed() {

        if(System.currentTimeMillis()-Backwait>=2000) {
            Backwait = System.currentTimeMillis()
            Toast.makeText(applicationContext, "뒤로가기 버튼을 한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
        }else{
            super.onBackPressed()
            println("나가기")
            FirebaseAuth.getInstance().signOut()
            ActivityCompat.finishAffinity(this);
            System.exit(0);

        }
    }

    override fun onListFragmentInteraction(item: WithInfo?){
        print("확인 "+item)
        if (item != null) {
//            Toast.makeText(this,"방 번호 :"+ item.roomid,Toast.LENGTH_SHORT).show()


            val edit = FirebaseDatabase.getInstance().getReference("With/" + item.roomid)
            if (item.withmaxcount == item.withnowcount) {
                Toast.makeText(applicationContext, "방이 꽉 찼습니다", Toast.LENGTH_SHORT)
                    .show()
            }else if(item.finish!="noinfo"){
                Toast.makeText(applicationContext, "종료된 위드입니다", Toast.LENGTH_SHORT)
                    .show()
            }else{
                val i = Intent(baseContext, WitherInActivity::class.java)
                i.putExtra("withinfo", item)
                i.putExtra("gid", "id1234")

                edit.child("/withnowcount").setValue(item.withnowcount + 1)
                edit.child("/user")
                    .setValue(item.user + "|" + FirebaseAuth.getInstance().currentUser!!.uid)

                startActivity(i)
            }
        }

    }
}
