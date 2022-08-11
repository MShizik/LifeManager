package com.example.life_manager

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.gridlayout.widget.GridLayout
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*


class FragmentCalendar : Fragment() {

    private val database : DatabaseReference = Firebase.database.reference
    private var stEmailUser : String = "aboba"
    private var iSwitchesState : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCurDay : Button = view.findViewById(R.id.calendar_btn_day)
        val btnPrevMth : Button = view.findViewById(R.id.cal_btn_prev_mth)
        val btnCurMth : Button = view.findViewById(R.id.cal_btn_cur_mth)
        val btnNextMth : Button = view.findViewById(R.id.cal_btn_next_mth)
        val btnPrevYear : Button = view.findViewById(R.id.cal_btn_prev_year)
        val btnCurYear : Button = view.findViewById(R.id.cal_btn_cur_year)
        val btnNextYear : Button = view.findViewById(R.id.cal_btn_cur_year)

        val grlDaysHolder : GridLayout = view.findViewById(R.id.calendar_days_holder)

        val secondApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val timestamp = 1565209665.toLong() // timestamp in Long
        val timestampAsDateString = java.time.format.DateTimeFormatter.ISO_INSTANT
            .format(java.time.Instant.ofEpochSecond(timestamp))
        var curdate = LocalDate.parse(timestampAsDateString, secondApiFormat)
        var prevdate = curdate.plus(Period.of(-1,-1,0))
        var nextdate = curdate.plus(Period.of(1,1,0))

        stEmailUser = arguments?.getString("email").toString()


        when(iSwitchesState){
            1 -> {
                btnCurDay.setBackgroundColor(Color.parseColor("#bbdefb"))
            }

            2 -> {
                btnCurDay.setBackgroundColor(Color.parseColor("#ffcc00"))
            }

            3 -> {
                btnCurDay.setBackgroundColor(Color.parseColor("#f0989f"))
            }
        }

        btnCurDay.text = curdate.dayOfMonth.toString()
        btnCurMth.text = curdate.month.toString()
        btnCurYear.text = curdate.year.toString()

        btnPrevYear.text = prevdate.year.toString()
        btnPrevMth.text = prevdate.month.toString()

        btnNextYear.text = nextdate.year.toString()
        btnNextMth.text = nextdate.month.toString()

        fillGrlLayout(grlDaysHolder,curdate)

        btnCurDay.setOnClickListener {
            var fragmentCurDay = FragmentCurrentDay()
            fragmentCurDay.arguments?.putString("email", stEmailUser)
            fragmentCurDay.arguments?.putString("date", SimpleDateFormat("dd", Locale.getDefault()).format(Date()))
            fragmentCurDay.arguments?.putString("month",SimpleDateFormat("MM", Locale.getDefault()).format(Date()))
            fragmentCurDay.arguments?.putString("year", SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()))
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.work_fragment_holder, fragmentCurDay)
            transaction.commit()
        }

        btnNextYear.setOnClickListener {
            val changePeriod : Period = Period.of(1,0,0)
            prevdate = prevdate.plus(changePeriod)
            curdate = curdate.plus(changePeriod)
            nextdate = nextdate.plus(changePeriod)

            btnPrevYear.text = prevdate.year.toString()
            btnCurYear.text = curdate.year.toString()
            btnNextYear.text = nextdate.year.toString()

            fillGrlLayout(grlDaysHolder, curdate)
        }

        btnNextMth.setOnClickListener {
            val changePeriod : Period = Period.of(0,1,0)
            prevdate = prevdate.plus(changePeriod)
            curdate = curdate.plus(changePeriod)
            nextdate = nextdate.plus(changePeriod)

            btnPrevYear.text = prevdate.year.toString()
            btnCurYear.text = curdate.year.toString()
            btnNextYear.text = nextdate.year.toString()

            fillGrlLayout(grlDaysHolder, curdate)
        }

        btnPrevYear.setOnClickListener {
            val changePeriod : Period = Period.of(-1,0,0)
            prevdate = prevdate.plus(changePeriod)
            curdate = curdate.plus(changePeriod)
            nextdate = nextdate.plus(changePeriod)

            btnPrevYear.text = prevdate.year.toString()
            btnCurYear.text = curdate.year.toString()
            btnNextYear.text = nextdate.year.toString()

            fillGrlLayout(grlDaysHolder, curdate)
        }

        btnNextMth.setOnClickListener {
            val changePeriod : Period = Period.of(0,-1,0)
            prevdate = prevdate.plus(changePeriod)
            curdate = curdate.plus(changePeriod)
            nextdate = nextdate.plus(changePeriod)

            btnPrevYear.text = prevdate.year.toString()
            btnCurYear.text = curdate.year.toString()
            btnNextYear.text = nextdate.year.toString()

            fillGrlLayout(grlDaysHolder, curdate)
        }

    }

    fun fillGrlLayout (grlDaysHolder : GridLayout, curdate : LocalDate) {
        grlDaysHolder.removeAllViews()
        for (i in 1..curdate.month.maxLength()){
            var newc : CardView = CardView(ApplicationProvider.getApplicationContext<Context>())
            newc.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            newc.radius = 33F
            var btnNewCard : Button = Button(ApplicationProvider.getApplicationContext<Context>())
            btnNewCard.layoutParams =
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            btnNewCard.setBackgroundResource(R.drawable.rounded_day)
            btnNewCard.text = i.toString()
            database.child("users").child(stEmailUser).child("days").child(curdate.year.toString()).child(curdate.getMonthValue().toString()).child(i.toString()).child("SwitchesState").get().addOnSuccessListener {
                when(it.value){
                    1->{
                        btnNewCard.setBackgroundColor(Color.parseColor("#bbdefb"))
                    }
                    2->{
                        btnNewCard.setBackgroundColor(Color.parseColor("#ffcc00"))
                    }
                    3->{
                        btnNewCard.setBackgroundColor(Color.parseColor("#f0989f"))
                    }
                }
            }
            btnNewCard.setOnClickListener {
                var fragmentCurDay = FragmentCurrentDay()
                fragmentCurDay.arguments?.putString("email", stEmailUser)
                fragmentCurDay.arguments?.putString("date", SimpleDateFormat("dd", Locale.getDefault()).format(Date()))
                fragmentCurDay.arguments?.putString("month",SimpleDateFormat("MM", Locale.getDefault()).format(Date()))
                fragmentCurDay.arguments?.putString("year", SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()))
                val transition = requireActivity().supportFragmentManager.beginTransaction()
                transition.replace(R.id.work_fragment_holder,fragmentCurDay)
                    .addToBackStack(null).commit()
            }
            newc.addView(btnNewCard)
            val row = GridLayout.spec(i%7-1)
            val col = GridLayout.spec(i/7-1)
            val gridP = GridLayout.LayoutParams(row, col)
            grlDaysHolder.addView(newc, gridP)
        }
    }

    fun getDataFromFirebase(){
        database.child("users").child(stEmailUser).child("days").child(SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())).child(SimpleDateFormat("MM", Locale.getDefault()).format(Date())).child(SimpleDateFormat("dd", Locale.getDefault()).format(Date())).get().addOnSuccessListener {
            iSwitchesState = it.child("SwitchesState").value.toString().toInt()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentCurrentDay()
    }
}