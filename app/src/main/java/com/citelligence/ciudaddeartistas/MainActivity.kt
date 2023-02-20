package com.citelligence.ciudaddeartistas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.citelligence.ciudaddeartistas.adapter.PageAdapter
import com.citelligence.ciudaddeartistas.fragment.GridViewFragment
import com.citelligence.ciudaddeartistas.fragment.RecyclerFragment
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        Thread.sleep(2000)
        setTheme(R.style.splashTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.viewPager)

        setupViewPager()
    }

    private fun agregarFragment(): ArrayList<Fragment>{
        val frag: ArrayList<Fragment> = arrayListOf(
            GridViewFragment.newInstance(),
            RecyclerFragment.newInstance())
        return  frag
    }

    private fun setupViewPager(){
        viewPager!!.adapter = PageAdapter(supportFragmentManager, agregarFragment())
        tabLayout!!.setupWithViewPager(viewPager)
        tabLayout!!.getTabAt(0)!!.text = "CATEGORIAS"
        tabLayout!!.getTabAt(1)!!.text = "TODOS"
    }

}