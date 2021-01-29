package com.example.gamewither.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamewither.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import java.net.URL

class WithChatAdapter(val context: Context, private val mValues: ArrayList<ChatInfo>)
    : RecyclerView.Adapter<WithChatAdapter.ViewHolder>(){

//    private val mOnClickListener: View.OnClickListener
    private lateinit var auth: FirebaseAuth
    lateinit var rdb: DatabaseReference
    lateinit var viewgroup:ViewGroup


    init {

//        mOnClickListener = View.OnClickListener { v ->
//            val item = v.tag as String
//            // Notify the active callbacks interface (the activity, if the fragment is attached to
//            // one) that an item has been selected.
//            print("adapter 클릭")
//            mListener?.onListFragmentInteraction(item)
//        }

        auth = FirebaseAuth.getInstance()


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_chat, parent, false)
        viewgroup=parent
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        if(auth.currentUser!!.uid==item.uid){
            //내가보낸 채팅이면
            holder.chatcontent.visibility=View.GONE
            holder.chatid.visibility=View.GONE
            holder.chattime.visibility=View.GONE
            holder.chatimg.visibility=View.GONE
            holder.chatmytime.visibility=View.VISIBLE
            holder.chatmycontent.visibility=View.VISIBLE
            holder.chatmytime.text=item.time
            holder.chatmycontent.text=item.content
        }else{
            holder.chatcontent.visibility=View.VISIBLE
            holder.chatid.visibility=View.VISIBLE
            holder.chattime.visibility=View.VISIBLE
            holder.chatimg.visibility=View.VISIBLE
            holder.chatmytime.visibility=View.GONE
            holder.chatmycontent.visibility=View.GONE
            holder.chatid.text=item.gid
            holder.chatcontent.text=item.content
            holder.chattime.text=item.time
        }

        holder.chatimg.setOnClickListener{
            val i= Intent(context,UserProfileAcitivty::class.java)
            i.putExtra("uid",item.uid)
            i.putExtra("gid",item.gid)
            context.startActivity(i)
        }

//        게임 이미지 설정
//        var image_task = URLtoBitmapTask().apply {
//            url = URL(item.url)
//        }
//
//        var bitmap: Bitmap = image_task.execute().get()
//        holder.image.setImageBitmap(bitmap)

        with(holder.mView) {
            tag = item.uid //view에 태그 달아줄 수 있음, 특정 아이템을 식별 가능
//            setOnClickListener(mOnClickListener)
        }


    }



    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var chatid=mView.findViewById<TextView>(R.id.laychat_id)
        var chatcontent=mView.findViewById<TextView>(R.id.laychat_content)
        var chattime=mView.findViewById<TextView>(R.id.laychat_time)
        var chatimg=mView.findViewById<ImageView>(R.id.laychat_img)
        var chatmycontent=mView.findViewById<TextView>(R.id.laychat_mycontent)
        var chatmytime=mView.findViewById<TextView>(R.id.laychat_mytime)
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