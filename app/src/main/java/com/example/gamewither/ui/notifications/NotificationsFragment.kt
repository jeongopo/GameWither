package com.example.gamewither.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.gamewither.R
import com.example.gamewither.intro.WitherInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    lateinit var currenuser:FirebaseUser
    lateinit var rdb:DatabaseReference

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        currenuser= FirebaseAuth.getInstance().currentUser!!

        rdb = FirebaseDatabase.getInstance().getReference("Wither")


        rdb.child(currenuser.uid).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get user value
                    if(dataSnapshot!=null) {
                        val getuser = dataSnapshot.getValue(WitherInfo::class.java)
                        if (getuser != null) {
                            root.findViewById<TextView>(R.id.page_with).text=getuser.withcount.toString()
                            root.findViewById<TextView>(R.id.page_wither).text=getuser.withercount.toString()
                            root.findViewById<TextView>(R.id.page_usernick).text=getuser.nickname
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })




        root.findViewById<Button>(R.id.page_idbtn).setOnClickListener{
            val i= Intent(context,IdManageActivity::class.java)
            startActivity(i)
        }
        root.findViewById<Button>(R.id.page_developer).setOnClickListener{
            val i= Intent(context,DeveloperActivity::class.java)
            startActivity(i)
        }

        return root
    }
}
