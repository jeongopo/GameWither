package com.example.gamewither.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamewither.R
import com.example.gamewither.intro.WitherInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_wither_in.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WitherInActivity : AppCompatActivity(){

    lateinit var auth:FirebaseAuth
    lateinit var rdb:DatabaseReference
    lateinit var layoutManager:LinearLayoutManager
    lateinit var adapter:WithChatAdapter
    var Chatlist=ArrayList<ChatInfo>(10)
    lateinit var withinfo:WithInfo
    var Witherlist=ArrayList<WitherInfo>(10)
    var Withermap=HashMap<String,WitherInfo>()
    lateinit var myinfo:WitherInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wither_in)
        auth=FirebaseAuth.getInstance()
        init()

    }

    fun init(){

        var currentUser=auth.currentUser

        val wither = FirebaseDatabase.getInstance().getReference("Wither")

        wither.child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
//                            TODO("Not yet implemented")
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get user value
                    if (dataSnapshot != null) {
                        myinfo= dataSnapshot.getValue(WitherInfo::class.java)!!
                    }
                }
            })


        withinfo=intent.getSerializableExtra("withinfo") as WithInfo
        val gid=intent.getStringExtra("gid")
        rdb=FirebaseDatabase.getInstance().getReference("Chat/"+withinfo.roomid)

        in_tab.visibility= View.GONE

        in_gamename.text= withinfo.gamename
        in_withfin.text= "("+withinfo.withnowcount+"/"+withinfo.withmaxcount+")"
        in_withtitle.text=withinfo.withtitle

        in_chatbtn.setOnClickListener {
            if(in_chattext.text.isNotBlank()) { //내용이 비어있지 않으면
                val tem = in_chattext.text
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                val formatted = current.format(formatter)

                val pathtime=DateTimeFormatter.ofPattern("YYYYMMddHHmmss")
                val pathret = current.format(pathtime)
                var rdbpath=FirebaseDatabase.getInstance().getReference("Chat/"+withinfo.roomid+"/"+pathret)

                val chat=ChatInfo(currentUser!!.uid,formatted,gid,in_chattext.text.toString())
                rdbpath.setValue(chat)

                in_chattext.text.clear() //텍스트뷰 값 비우기
            }
        }

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        in_recycler.layoutManager=layoutManager

        adapter= WithChatAdapter(this,Chatlist)
        in_recycler.adapter=adapter

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌

                rdb=FirebaseDatabase.getInstance().getReference("Chat/"+withinfo.roomid)

                //새로 추가된 데이터(값 : MessageItem객체) 가져오기
                var chat= dataSnapshot.getValue(ChatInfo::class.java)

                //새로운 메세지를 리스뷰에 추가하기 위해 ArrayList에 추가
                if (chat != null) {
                    Chatlist.add(chat)
                    print("chat 확인!!! : "+chat)
                }

                //리스트뷰를 갱신
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }


        }

        rdb.addChildEventListener(childEventListener)

        val member=FirebaseDatabase.getInstance().getReference("With/"+withinfo.roomid)
        val ValueEventListener=object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
//
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var with=snapshot.getValue(WithInfo::class.java)
                if(with!=null) {
                    in_withfin.text = "(" + with.withnowcount + "/" + with.withmaxcount + ")"
                    withinfo.withnowcount=with.withnowcount
                    withinfo.user=with.user
                    withinfo.withboss=with.withboss
                    withinfo.finish=with.finish

                    if(withinfo.finish!="noinfo"){
                        in_withfin.text="모집완료"
                    }

//                    val tem=with.user.split("|")
//                    for(uid in tem) {
//                        if(!(Withermap.containsKey(uid))){ //새로운 유저라면
//                            val find=FirebaseDatabase.getInstance().getReference("Wither/"+uid)
//                            val efind=object:ValueEventListener{
//                                override fun onCancelled(error: DatabaseError) {
//                                }
//
//                                override fun onDataChange(snapshot: DataSnapshot) {
//                                    Withermap[uid]=snapshot.getValue(WitherInfo::class.java) as WitherInfo
//                                }
//
//                            }
//                            find.addValueEventListener(efind)
//
//                        }
//                    }
                }
            }

        }
        member.addValueEventListener(ValueEventListener)


        in_backbtn.setOnClickListener{
            without()
        }

        in_btn.setOnClickListener{
            in_tab_withtitle.text=withinfo.withtitle
            in_tab.visibility=View.VISIBLE
        }

        val layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        in_tab_recycler.layoutManager=layoutManager



        val adapter=ProfileShowAdapter(this,Witherlist)
        in_tab_recycler.adapter=adapter

        in_tab_btn.setOnClickListener{
            in_tab.visibility=View.GONE
        }

        in_withfin.setOnClickListener{
            withfinish()
        }
    } //init 끝

    override fun onBackPressed() {
        super.onBackPressed()
        without()
    }

    fun without(){


            val with = FirebaseDatabase.getInstance().getReference("With/" + withinfo.roomid)

            if (withinfo.withnowcount == 1) {
                with.removeValue()
                if(withinfo.finish!="noinfo"){
                    myinfo.withcount++
                    val user=FirebaseDatabase.getInstance().getReference("Wither/"+FirebaseAuth.getInstance().currentUser!!.uid)
                    user.setValue(myinfo)
                    print("나의 위드 카운트 : "+myinfo.withcount )
                }
                finish()
            } else {

                val arr = withinfo.user.split("|")

                if (withinfo.withboss == FirebaseAuth.getInstance().currentUser!!.uid) {
                    with.child("withboss").setValue(arr[1]) //방장 새로 정해주기
                }

                var ret: String = ""
                for (uid in arr) {
                    if (uid != FirebaseAuth.getInstance().currentUser!!.uid)
                        ret = ret + uid + "|"
                }

                if(withinfo.finish!="noinfo"){
                    myinfo.withcount++
                    val user=FirebaseDatabase.getInstance().getReference("Wither/"+FirebaseAuth.getInstance().currentUser!!.uid)
                    user.setValue(myinfo)
                    print("나의 위드 카운트 : "+myinfo.withcount )
                }

                with.child("user").setValue(ret)
                with.child("withnowcount").setValue(withinfo.withnowcount - 1)
                finish()
            }


    }

    fun withfinish(){
        val with=FirebaseDatabase.getInstance().getReference("With/"+withinfo.roomid)


        if(withinfo.withboss==(FirebaseAuth.getInstance().currentUser!!.uid)){
            //방장일때만 방을 종료할 수 있음
            val arr=withinfo.user.split("|")
            var ret:String=""

            //방 종료하기
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
            val formatted = current.format(formatter)

            val pathfomat=DateTimeFormatter.ofPattern("yyyyMMddHHmm")
            val pathfomatted=current.format(pathfomat)
            with.child("finish").setValue(formatted)
            withinfo.finish=formatted

            //멤버들의 위드 내역에 추가하기
            for(uid in arr){
                val add = FirebaseDatabase.getInstance().getReference("History")
                add.child(uid+"/"+pathfomatted).setValue(withinfo)

            }
        }else {
            Toast.makeText(this,"방장이 아닙니다",Toast.LENGTH_SHORT).show()
        }


    }


}
