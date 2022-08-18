package com.example.life_manager

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import kotlinx.coroutines.newFixedThreadPoolContext
import nl.joery.animatedbottombar.AnimatedBottomBar
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class WorkActivity : AppCompatActivity() {

    lateinit var BottomNavBar : AnimatedBottomBar
    lateinit var stEmailUser : String

    var curdate  = SimpleDateFormat("dd", Locale.getDefault()).format(Date())
    var curmth = LocalDate.parse(java.time.format.DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond(Math.round((System.currentTimeMillis() / 1000).toDouble()).toLong())),DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")).month.toString()
    var curyear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work)

        BottomNavBar = findViewById(R.id.work_b_nav)

        stEmailUser = intent.extras?.getString("email").toString().replace(".","")
        var fragmentToChange : Fragment

        var bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_01, R.string.nav_days_title, R.id.menu_item_calendar)

        when (curdate){
            "01" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_01,R.string.nav_days_title,R.id.menu_item_current_day)
            "02" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_02,R.string.nav_days_title,R.id.menu_item_current_day)
            "03" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_03,R.string.nav_days_title,R.id.menu_item_current_day)
            "04" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_04,R.string.nav_days_title,R.id.menu_item_current_day)
            "05" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_05,R.string.nav_days_title,R.id.menu_item_current_day)
            "06" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_06,R.string.nav_days_title,R.id.menu_item_current_day)
            "07" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_07,R.string.nav_days_title,R.id.menu_item_current_day)
            "08" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_08,R.string.nav_days_title,R.id.menu_item_current_day)
            "09" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_09,R.string.nav_days_title,R.id.menu_item_current_day)
            "10" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_10,R.string.nav_days_title,R.id.menu_item_current_day)
            "11" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_11,R.string.nav_days_title,R.id.menu_item_current_day)
            "12" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_12,R.string.nav_days_title,R.id.menu_item_current_day)
            "13" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_13,R.string.nav_days_title,R.id.menu_item_current_day)
            "14" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_14,R.string.nav_days_title,R.id.menu_item_current_day)
            "15" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_15,R.string.nav_days_title,R.id.menu_item_current_day)
            "16" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_16,R.string.nav_days_title,R.id.menu_item_current_day)
            "17" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_17,R.string.nav_days_title,R.id.menu_item_current_day)
            "18" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_18,R.string.nav_days_title,R.id.menu_item_current_day)
            "19" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_19,R.string.nav_days_title,R.id.menu_item_current_day)
            "20" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_20,R.string.nav_days_title,R.id.menu_item_current_day)
            "21" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_21,R.string.nav_days_title,R.id.menu_item_current_day)
            "22" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_22,R.string.nav_days_title,R.id.menu_item_current_day)
            "23" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_23,R.string.nav_days_title,R.id.menu_item_current_day)
            "24" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_24,R.string.nav_days_title,R.id.menu_item_current_day)
            "25" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_25,R.string.nav_days_title,R.id.menu_item_current_day)
            "26" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_26,R.string.nav_days_title,R.id.menu_item_current_day)
            "27" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_27,R.string.nav_days_title,R.id.menu_item_current_day)
            "28" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_28,R.string.nav_days_title,R.id.menu_item_current_day)
            "29" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_29,R.string.nav_days_title,R.id.menu_item_current_day)
            "30" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_30,R.string.nav_days_title,R.id.menu_item_current_day)
            "31" -> bottomBarTabTmp = BottomNavBar.createTab(R.drawable.ic_date_31,R.string.nav_days_title,R.id.menu_item_current_day)
        }
        BottomNavBar.removeTabAt(0)
        BottomNavBar.addTabAt(0,bottomBarTabTmp)

        fragmentToChange = FragmentCurrentDay()
        var tmpBundle : Bundle = Bundle()
        tmpBundle.putString("email",stEmailUser)
        tmpBundle.putString("date", curdate)
        tmpBundle.putString("month", curmth)
        tmpBundle.putString("year", curyear)
        fragmentToChange.arguments = tmpBundle
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.work_fragment_holder, fragmentToChange)
            .commit()


        BottomNavBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                when (newIndex) {
                    1 -> {
                        fragmentToChange = FragmentCalendar()
                        var tmpBundle: Bundle = Bundle()
                        tmpBundle.putString("email", stEmailUser)
                        fragmentToChange.arguments = tmpBundle
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.work_fragment_holder, fragmentToChange)
                            .commit()
                    }
                    2 -> {
                        fragmentToChange = ProfileFragment()
                        var tmpBundle: Bundle = Bundle()
                        tmpBundle.putString("email", stEmailUser)
                        fragmentToChange.arguments = tmpBundle
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.work_fragment_holder, fragmentToChange)
                            .commit()
                    }
                    0-> {
                        fragmentToChange = FragmentCurrentDay()
                        var tmpBundle: Bundle = Bundle()
                        tmpBundle.putString("email", stEmailUser)
                        tmpBundle.putString(
                            "date",
                            SimpleDateFormat("dd", Locale.getDefault()).format(Date())
                        )
                        tmpBundle.putString(
                            "month", LocalDate.parse(
                                DateTimeFormatter.ISO_INSTANT.format(
                                    Instant.ofEpochSecond(
                                        Math.round((System.currentTimeMillis() / 1000).toDouble())
                                            .toLong()
                                    )
                                ), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                            ).month.toString()
                        )
                        tmpBundle.putString(
                            "year",
                            SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())
                        )
                        fragmentToChange.arguments = tmpBundle
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.work_fragment_holder, fragmentToChange)
                            .commit()
                    }
                    3 -> {
                        fragmentToChange = FragmentFriends()
                        var tmpBundle: Bundle = Bundle()
                        tmpBundle.putString("email", stEmailUser)
                        fragmentToChange.arguments = tmpBundle
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.work_fragment_holder, fragmentToChange)
                            .commit()
                    }
                }
                true
            }
        })
    }
}