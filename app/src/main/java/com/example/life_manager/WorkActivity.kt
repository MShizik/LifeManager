package com.example.life_manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.life_manager.databinding.ActivityWorkBinding
import java.text.SimpleDateFormat
import java.util.*

class WorkActivity : AppCompatActivity() {

    lateinit var menu_binding : ActivityWorkBinding
    lateinit var stEmailUser : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menu_binding = ActivityWorkBinding.inflate(layoutInflater)
        setContentView(menu_binding.root)

        stEmailUser = intent.extras?.getString("email").toString()

        var fragmentToChange : Fragment

        fragmentToChange = FragmentCurrentDay()
        fragmentToChange.arguments?.putString("email",stEmailUser)
        fragmentToChange.arguments?.putString("date", SimpleDateFormat("dd", Locale.getDefault()).format(Date()))
        fragmentToChange.arguments?.putString("month",SimpleDateFormat("MM", Locale.getDefault()).format(Date()))
        fragmentToChange.arguments?.putString("year", SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()))

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.work_fragment_holder, FragmentCurrentDay.newInstance())
            .commit()


        menu_binding.workBNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_item_calendar -> {
                    fragmentToChange = FragmentCalendar()
                    fragmentToChange.arguments?.putString("email", stEmailUser)
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.work_fragment_holder, fragmentToChange)
                        .commit()
                }
                R.id.menu_item_profile -> {
                    fragmentToChange = ProfileFragment()
                    fragmentToChange.arguments?.putString("email", stEmailUser)
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.work_fragment_holder, fragmentToChange)
                        .commit()
                }
                R.id.menu_item_current_day -> {
                    fragmentToChange = FragmentCurrentDay()
                    fragmentToChange.arguments?.putString("email", stEmailUser)
                    fragmentToChange.arguments?.putString("date", SimpleDateFormat("dd", Locale.getDefault()).format(Date()))
                    fragmentToChange.arguments?.putString("month",SimpleDateFormat("MM", Locale.getDefault()).format(Date()))
                    fragmentToChange.arguments?.putString("year", SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()))
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.work_fragment_holder, fragmentToChange)
                        .commit()
                }
                R.id.menu_item_friends ->{
                    fragmentToChange = FragmentFriends()
                    fragmentToChange.arguments?.putString("email", stEmailUser)
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