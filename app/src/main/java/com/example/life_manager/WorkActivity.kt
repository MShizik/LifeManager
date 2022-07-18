package com.example.life_manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.life_manager.databinding.ActivityWorkBinding

class WorkActivity : AppCompatActivity() {

    lateinit var menu_binding : ActivityWorkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menu_binding = ActivityWorkBinding.inflate(layoutInflater)
        setContentView(menu_binding.root)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.work_fragment_holder, FragmentCurrentDay.newInstance())
            .commit()


        menu_binding.workBNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_item_calendar -> {

                }
                R.id.menu_item_profile -> {}
                R.id.menu_item_current_day -> {}
            }
            true
        }

    }
}