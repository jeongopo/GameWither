package com.example.gamewither.ui.notifications

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamewither.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class IdShowAdapter(val context: Context, val mValues:ArrayList<GameIDInfo>) : RecyclerView.Adapter<IdShowAdapter.ViewHolder>() {
    lateinit var rdb:DatabaseReference
    lateinit var user:FirebaseUser
    var clicklistener:Clicklistener?=null

    interface Clicklistener {
        fun onEditClick(holder: ViewHolder, view : View, gamename:String, gid: String)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdShowAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_id, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val gamename=mView.findViewById<TextView>(R.id.layid_gamename)
        val gid=mView.findViewById<TextView>(R.id.layid_gid)
        val edit=mView.findViewById<ImageButton>(R.id.layid_edit)
        val delete=mView.findViewById<ImageButton>(R.id.layid_delete)
    }

    override fun getItemCount(): Int {
        return mValues.count()
    }

    override fun onBindViewHolder(holder: IdShowAdapter.ViewHolder, position: Int) {
        val item=mValues[position]
        holder.gamename.text=item.gamename
        holder.gid.text=item.gameid

        user=FirebaseAuth.getInstance().currentUser!!

        rdb = FirebaseDatabase.getInstance().getReference("GameID/" + user.uid)
        holder.delete.setOnClickListener{
            rdb.child(holder.gamename.text.toString()).removeValue()
        }

        holder.edit.setOnClickListener{
            clicklistener?.onEditClick(holder,it,holder.gamename.text.toString(),holder.gid.text.toString())
        }
    }


}