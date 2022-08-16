package com.example.life_manager

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment
import com.example.life_manager.databinding.ActivityWorkBinding
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class WorkActivity : AppCompatActivity() {

    lateinit var menu_binding : ActivityWorkBinding
    lateinit var stEmailUser : String

    var curdate  = SimpleDateFormat("dd", Locale.getDefault()).format(Date())
    var curmth = LocalDate.parse(java.time.format.DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond(Math.round((System.currentTimeMillis() / 1000).toDouble()).toLong())),DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")).month.toString()
    var curyear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menu_binding = ActivityWorkBinding.inflate(layoutInflater)
        setContentView(menu_binding.root)

        stEmailUser = intent.extras?.getString("email").toString().replace(".","")
        var fragmentToChange : Fragment

        when (curdate){
            "01" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_01)
            "02" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_02)
            "03" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_03)
            "04" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_04)
            "05" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_05)
            "06" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_06)
            "07" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_07)
            "08" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_08)
            "09" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_09)
            "10" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_10)
            "11" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_11)
            "12" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_12)
            "13" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_13)
            "14" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_14)
            "15" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_15)
            "16" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_16)
            "17" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_17)
            "18" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_18)
            "19" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_19)
            "20" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_20)
            "21" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_21)
            "22" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_22)
            "23" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_23)
            "24" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_24)
            "25" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_25)
            "26" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_26)
            "27" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_27)
            "28" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_28)
            "29" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_29)
            "30" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_30)
            "31" -> menu_binding.workBNav.menu.getItem(0).setIcon(R.drawable.ic_date_31)
        }




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


        menu_binding.workBNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_item_calendar -> {
                    fragmentToChange = FragmentCalendar()
                    var tmpBundle : Bundle = Bundle()
                    tmpBundle.putString("email",stEmailUser)
                    fragmentToChange.arguments = tmpBundle
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.work_fragment_holder, fragmentToChange)
                        .commit()
                }
                R.id.menu_item_profile -> {
                    fragmentToChange = ProfileFragment()
                    var tmpBundle : Bundle = Bundle()
                    tmpBundle.putString("email",stEmailUser)
                    fragmentToChange.arguments = tmpBundle
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.work_fragment_holder, fragmentToChange)
                        .commit()
                }
                R.id.menu_item_current_day -> {
                    fragmentToChange = FragmentCurrentDay()
                    var tmpBundle : Bundle = Bundle()
                    tmpBundle.putString("email",stEmailUser)
                    tmpBundle.putString("date", SimpleDateFormat("dd", Locale.getDefault()).format(Date()))
                    tmpBundle.putString("month", LocalDate.parse(java.time.format.DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond(Math.round((System.currentTimeMillis() / 1000).toDouble()).toLong())),DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")).month.toString())
                    tmpBundle.putString("year", SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()))
                    fragmentToChange.arguments = tmpBundle
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.work_fragment_holder, fragmentToChange)
                        .commit()
                }
                R.id.menu_item_friends ->{
                    fragmentToChange = FragmentFriends()
                    var tmpBundle : Bundle = Bundle()
                    tmpBundle.putString("email",stEmailUser)
                    fragmentToChange.arguments = tmpBundle
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.work_fragment_holder, fragmentToChange)
                        .commit()
                }
            }
            true
        }

    }
}