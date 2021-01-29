package com.example.gamewither.ui.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamewither.R
import com.example.gamewither.ui.home.WithInfo

class HistoryShowAdapter(val context: Context, private val mValues: ArrayList<WithInfo>)
    : RecyclerView.Adapter<HistoryShowAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryShowAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mValues.count()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val gamename=mView.findViewById<TextView>(R.id.his_gamename)
        val withtitle=mView.findViewById<TextView>(R.id.his_withtitle)
        val date=mView.findViewById<TextView>(R.id.his_date)
    }

    override fun onBindViewHolder(holder: HistoryShowAdapter.ViewHolder, position: Int) {
        holder.gamename.text=mValues[position].gamename
        holder.withtitle.text=mValues[position].withtitle
        holder.date.text=mValues[position].finish

    }
}