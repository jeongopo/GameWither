package com.example.gamewither.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamewither.R
import com.example.gamewither.intro.WitherInfo

class ProfileShowAdapter(val context: Context, private val mValues: ArrayList<WitherInfo>)
    : RecyclerView.Adapter<ProfileShowAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_profile, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val nickname=mView.findViewById<TextView>(R.id.lay_pro_gid)
    }

    override fun onBindViewHolder(holder: ProfileShowAdapter.ViewHolder, position: Int) {
        holder.nickname.text=mValues[position].nickname
    }
}