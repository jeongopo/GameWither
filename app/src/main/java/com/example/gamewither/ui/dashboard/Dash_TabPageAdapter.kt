package com.example.gamewither.ui.dashboard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class Dash_TabPageAdapter(fragmentActivity: FragmentActivity):
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0-> {return ListWitherFragment()} //위더목록
            1-> {return HistoryWithFragment()} //위드내역
        }
        return ListWitherFragment()
    }
}