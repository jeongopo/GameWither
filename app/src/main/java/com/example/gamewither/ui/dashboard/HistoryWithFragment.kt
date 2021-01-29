package com.example.gamewither.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamewither.R
import com.example.gamewither.ui.home.WithInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HistoryWithFragment : Fragment() {

    lateinit var rdb:DatabaseReference
    var Withlist=ArrayList<WithInfo>(10)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val root= inflater.inflate(R.layout.fragment_history_with, container, false)

        val layoutmanager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        root.findViewById<RecyclerView>(R.id.hisfrag_recycler).layoutManager=layoutmanager
        rdb = FirebaseDatabase.getInstance().getReference("History/"+(FirebaseAuth.getInstance().currentUser!!.uid)+"/")
        val adapter= context?.let { HistoryShowAdapter(it, Withlist) }
        root.findViewById<RecyclerView>(R.id.hisfrag_recycler).adapter=adapter

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌

                //새로 추가된 데이터(값 : MessageItem객체) 가져오기
                var newwith= dataSnapshot.getValue(WithInfo::class.java)

                //새로운 메세지를 리스뷰에 추가하기 위해 ArrayList에 추가
                if (newwith != null) {
                    Withlist.add(newwith)
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
            }


        }

        rdb.addChildEventListener(childEventListener)

        return root
    }



}
