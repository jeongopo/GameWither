package com.example.gamewither.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.example.gamewither.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    var text= arrayListOf<String>("위더 목록", "위드 내역")

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val tablayout=root.findViewById<TabLayout>(R.id.mem_tab)
        val adapter=Dash_TabPageAdapter(requireActivity())
        val view=root.findViewById<ViewPager2>(R.id.mem_viewpager)
        view.adapter=adapter
        TabLayoutMediator(tablayout,view){
            tab, position ->
            tab.text=text[position]
        }.attach()
        return root
    }
}
