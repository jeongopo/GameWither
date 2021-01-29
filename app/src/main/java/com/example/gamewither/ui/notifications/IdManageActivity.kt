package com.example.gamewither.ui.notifications

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamewither.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_id_manage.*

class IdManageActivity : AppCompatActivity() {

    lateinit var rdb:DatabaseReference
    lateinit var user:FirebaseUser
    var idlist=ArrayList<GameIDInfo>(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_id_manage)
        init()
    }

    fun init() {

        var layoutmanager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mag_recycler.layoutManager = layoutmanager

        val adapter = IdShowAdapter(this, idlist)
        mag_recycler.adapter = adapter

        user = FirebaseAuth.getInstance().currentUser!!
        val rdb = FirebaseDatabase.getInstance().getReference("GameID/" + user.uid)

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌

                //새로 추가된 데이터(값 : MessageItem객체) 가져오기
                var newid = dataSnapshot.getValue(GameIDInfo::class.java)

                //새로운 메세지를 리스뷰에 추가하기 위해 ArrayList에 추가
                if (newid != null) {
                    idlist.add(newid)
                }

                //리스트뷰를 갱신
//                    Withlist.clear()
                if (adapter != null) {
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                idlist.clear()
                mag_recycler.adapter = adapter
//                adapter.notifyDataSetChanged()
            }


        }

        rdb.addChildEventListener(childEventListener)

        mag_addid.setOnClickListener {
            mag_tab.visibility = View.VISIBLE
            magtab_title.text="게임 ID 추가"
            mag_addid.isClickable = false
            mag_backicon.isClickable = false
        }
        magtab_cancelbtn.setOnClickListener {
            mag_tab.visibility = View.GONE
            mag_addid.isClickable = true
            mag_backicon.isClickable = true
        }

        magtab_addbtn.setOnClickListener {
            val newinfo = GameIDInfo(magtab_gamename.text.toString(), magtab_gid.text.toString())
            rdb.child(magtab_gamename.text.toString()).setValue(newinfo)
            Toast.makeText(this, "아이디를 추가했습니다", Toast.LENGTH_SHORT).show()
            mag_tab.visibility = View.GONE
            mag_addid.isClickable = true
            mag_backicon.isClickable = true
        }

        adapter.clicklistener=object : IdShowAdapter.Clicklistener {
            override fun onEditClick(
                holder: IdShowAdapter.ViewHolder,
                view: View,
                gamename: String,
                gid: String
            ) {
                mag_tab.visibility = View.VISIBLE
                magtab_title.text="게임 ID 편집"
                mag_addid.isClickable = false
                mag_backicon.isClickable = false
                magtab_gamename.setText(gamename)
                magtab_gid.setText(gid)
            }

        }

        mag_backicon.setOnClickListener{
            finish()
        }
    }

}
