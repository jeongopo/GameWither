package com.example.gamewither.ui.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamewither.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.net.URL

class WithShowAdapter(val context: Context, private val mValues: ArrayList<WithInfo>,private val mListener: HomeFragment.OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<WithShowAdapter.ViewHolder>(){

    private val mOnClickListener: View.OnClickListener
    private lateinit var auth: FirebaseAuth
    lateinit var rdb: DatabaseReference


    init {

        mOnClickListener = View.OnClickListener { v ->
            val item = mValues[v.tag as Int]
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            print("adapter 클릭")
            mListener?.onListFragmentInteraction(item)
        }

        auth = FirebaseAuth.getInstance()


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_withlayout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.withtitle.text = item.withtitle
        holder.withcount.text=item.withnowcount.toString()+"/"+item.withmaxcount.toString()
        holder.gamename.text=item.gamename

//        게임 이미지 설정
//        var image_task = URLtoBitmapTask().apply {
//            url = URL(item.url)
//        }
//
//        var bitmap: Bitmap = image_task.execute().get()
//        holder.image.setImageBitmap(bitmap)

        with(holder.mView) {
            tag = position //view에 태그 달아줄 수 있음, 특정 아이템을 식별 가능
            setOnClickListener(mOnClickListener)
        }

        if(item.finish!="noinfo"){
            holder.withcount.setTextColor(Color.RED)
        }

        val edit = FirebaseDatabase.getInstance().getReference("With/" + item.roomid)
        val valueEventListener=object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val tem=snapshot.getValue(WithInfo::class.java)
                if(tem!=null) {
                    holder.withcount.text =
                        tem.withnowcount.toString() + "/" + tem.withmaxcount.toString()
                    item.withnowcount=tem.withnowcount
                }
            }

        }
        edit.addValueEventListener(valueEventListener)


    }



    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val withtitle: TextView = mView.findViewById(R.id.lay_withtitle)
        val withcount: TextView =mView.findViewById(R.id.lay_withcount)
//        val image: ImageView =mView.findViewById(R.id.gameimg)
        val gamename:TextView=mView.findViewById(R.id.lay_gamename)
    }


    inner class URLtoBitmapTask() : AsyncTask<Void, Void, Bitmap>() {
        //이미지 설정하는 함수. 액티비티에서 설정해줌
        lateinit var url: URL
        override fun doInBackground(vararg params: Void?): Bitmap {
            val bitmap = BitmapFactory.decodeStream(url.openStream())
            return bitmap
        }
        override fun onPreExecute() {
            super.onPreExecute()

        }
        override fun onPostExecute(result: Bitmap) {
            super.onPostExecute(result)
        }
    }


}