package com.example.gamewither.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamewither.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var auth: FirebaseAuth
    lateinit var layoutManager:LinearLayoutManager
    lateinit var rdb: DatabaseReference
    private lateinit var adapter:WithShowAdapter
    var Withlist=ArrayList<WithInfo>(10)
    private var listener: OnListFragmentInteractionListener? = null


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) { //로그인한 상태로만 들어옴, 혹시모를 체크
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            root.findViewById<RecyclerView>(R.id.home_withrecycler).layoutManager=layoutManager


            root.findViewById<Button>(R.id.home_mkwith).setOnClickListener {
                val i= Intent(context,MakeWithActivity::class.java)
                requireActivity().startActivity(i)
            }

            initAdaper()
            root.findViewById<RecyclerView>(R.id.home_withrecycler).adapter=adapter

            rdb = FirebaseDatabase.getInstance().getReference("With")

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
                    adapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    Withlist.clear()
                    root.findViewById<RecyclerView>(R.id.home_withrecycler).adapter=adapter
//                    adapter.notifyDataSetChanged()
                }


            }

            rdb.addChildEventListener(childEventListener)

            root.findViewById<ImageButton>(R.id.home_refresh).setOnClickListener {
                Withlist.clear() //비우고 새로 넣기
                rdb.addChildEventListener(childEventListener)
//                root.findViewById<RecyclerView>(R.id.home_withrecycler).adapter=adapter
//                adapter.notifyDataSetChanged()
            }



        }





        return root
    }

    fun initAdaper(){
        adapter= context?.let {
            WithShowAdapter(it,Withlist,listener) }!!
//        val currentUser = auth.currentUser
//        if (currentUser != null) { //로그인한 경우
//            if(adapter!=null) {
//                val query = FirebaseDatabase.getInstance().reference
//                    .child("With").limitToLast(50)
//                //50개의 데이터를 가지고 오겠음
//
//                val option = FirebaseRecyclerOptions.Builder<KeepData>()
//                    .setQuery(query, KeepData::class.java)
//                    .build() //질의문 수행하는 옵션
//
//                adapter = KeepAdapter(option)
//                keeprecycler.adapter = adapter
//                adapter.startListening()
//            }
//        }
    }

    //adatper 리스너 부착
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    //adapter 리스너
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: WithInfo?)
    }



}
