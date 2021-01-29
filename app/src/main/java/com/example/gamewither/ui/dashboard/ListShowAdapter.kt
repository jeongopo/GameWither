package com.example.gamewither.ui.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamewither.R
import com.example.gamewither.intro.WitherInfo
import com.google.firebase.database.FirebaseDatabase

class ListShowAdapter(val context: Context, private val mValues: ArrayList<WitherInfo>) : RecyclerView.Adapter<ListShowAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListShowAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_wither, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mValues.count()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val nickname=mView.findViewById<TextView>(R.id.lay_wither_nick)
        val showid=mView.findViewById<Button>(R.id.lay_wither_id)
        val delete=mView.findViewById<ImageButton>(R.id.lay_wither_delete)
    }

    override fun onBindViewHolder(holder: ListShowAdapter.ViewHolder, position: Int) {
        val item= FirebaseDatabase.getInstance().getReference("Join")
        holder.nickname.text=mValues[position].nickname

        holder.showid.setOnClickListener{
            //id 보여주는 창으로 넘어가기
        }
        holder.delete.setOnClickListener{
            item.child(mValues[position].uid).removeValue()
        }

    }
}