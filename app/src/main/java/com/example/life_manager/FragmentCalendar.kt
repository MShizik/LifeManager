package com.example.life_manager

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class FragmentCalendar : Fragment() {

    private val database : DatabaseReference = Firebase.database.reference
    private var stEmailUser : String = "aboba"
    private var alDatesData : ArrayList<DateHolder> = arrayListOf()

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
        val btnNextYear : Button = view.findViewById(R.id.cal_btn_next_year)

        val grlDaysHolder : GridView = view.findViewById(R.id.calendar_days_holder)

        val prbarMainProgressBar  = view.findViewById(R.id.progress_layout) as ConstraintLayout
        val prbarCalendarProgressBar  = view.findViewById(R.id.progress_layout_calendar) as ConstraintLayout
        prbarMainProgressBar.visibility = View.VISIBLE
        prbarCalendarProgressBar.visibility = View.GONE

        var curdate = LocalDate.parse(
            java.time.format.DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond(Math.round((System.currentTimeMillis() / 1000).toDouble()).toLong())),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))
        var prevdate = curdate.plus(Period.of(-1,-1,0))
        var nextdate = curdate.plus(Period.of(1,1,0))


        stEmailUser = arguments?.getString("email").toString()

        getDataFromFirebase(object: callBackProfileData{
            override fun OnCallback(iSwitchState: Int) {
                when(iSwitchState){
                    1 -> {
                        btnCurDay.setBackgroundResource(R.drawable.rounded_day_ordinary)
                    }

                    2 -> {
                        btnCurDay.setBackgroundResource(R.drawable.rounded_day_interest)
                    }

                    3 -> {
                        btnCurDay.setBackgroundResource(R.drawable.rounded_day_productive)
                    }
                }
                TimeUnit.SECONDS.sleep(1L)
                prbarMainProgressBar.visibility = View.GONE
                if (alDatesData.size <= curdate.month.maxLength()){
                    prbarCalendarProgressBar.visibility = View.VISIBLE
                }
            }
        },curdate.month.toString())

        fillGrlLayout(object: callbackDateData{
            override fun OnCallback(tmpDate: DateHolder) {
                alDatesData.add(tmpDate)
                if(alDatesData.size >= curdate.month.maxLength()){
                    val adapterDatesHolder = DateHolderAdapter(requireContext(), alDatesData)
                    grlDaysHolder.adapter = adapterDatesHolder
                    TimeUnit.SECONDS.sleep(1L)
                    prbarCalendarProgressBar.visibility = View.GONE
                }
            }

        }, curdate.month.toString(), curdate.year.toString(), curdate.month.maxLength())

        btnCurDay.text = curdate.dayOfMonth.toString()
        btnCurMth.text = curdate.month.toString().substring(0,3)
        btnCurYear.text = curdate.year.toString()

        btnPrevYear.text = prevdate.year.toString()
        btnPrevMth.text = prevdate.month.toString().substring(0,3)

        btnNextYear.text = nextdate.year.toString()
        btnNextMth.text = nextdate.month.toString().substring(0,3)


        btnCurDay.setOnClickListener {
            var fragmentToChange = FragmentCurrentDay()
            var tmpBundle : Bundle = Bundle()
            tmpBundle.putString("email",stEmailUser)
            tmpBundle.putString("date", curdate.dayOfMonth.toString())
            tmpBundle.putString("month", curdate.month.toString())
            tmpBundle.putString("year", curdate.year.toString())
            fragmentToChange.arguments = tmpBundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.work_fragment_holder, fragmentToChange)
            transaction.commit()
        }

        btnNextYear.setOnClickListener {
            prbarCalendarProgressBar.visibility = View.VISIBLE
            val changePeriod : Period = Period.of(1,0,0)
            prevdate = prevdate.plus(changePeriod)
            curdate = curdate.plus(changePeriod)
            nextdate = nextdate.plus(changePeriod)

            btnPrevYear.text = prevdate.year.toString()
            btnCurYear.text = curdate.year.toString()
            btnNextYear.text = nextdate.year.toString()
            alDatesData.clear()

            fillGrlLayout(object: callbackDateData{
                override fun OnCallback(tmpDate: DateHolder) {
                    alDatesData.add(tmpDate)
                    if(alDatesData.size >= curdate.month.maxLength()){
                        val adapterDatesHolder = DateHolderAdapter(requireContext(), alDatesData)
                        grlDaysHolder.adapter = adapterDatesHolder
                        TimeUnit.SECONDS.sleep(1L)
                        prbarCalendarProgressBar.visibility = View.GONE
                    }
                }

            }, curdate.month.toString(), curdate.year.toString(), curdate.month.maxLength())
        }

        btnNextMth.setOnClickListener {
            prbarCalendarProgressBar.visibility = View.VISIBLE
            val changePeriod : Period = Period.of(0,1,0)
            prevdate = prevdate.plus(changePeriod)
            curdate = curdate.plus(changePeriod)
            nextdate = nextdate.plus(changePeriod)

            btnPrevMth.text = prevdate.month.toString().substring(0,3)
            btnCurMth.text = curdate.month.toString().substring(0,3)
            btnNextMth.text = nextdate.month.toString().substring(0,3)

            btnPrevYear.text = prevdate.year.toString()
            btnCurYear.text = curdate.year.toString()
            btnNextYear.text = nextdate.year.toString()

            alDatesData.clear()

            fillGrlLayout(object: callbackDateData{
                override fun OnCallback(tmpDate: DateHolder) {
                    alDatesData.add(tmpDate)
                    if(alDatesData.size >= curdate.month.maxLength()){
                        val adapterDatesHolder = DateHolderAdapter(requireContext(), alDatesData)
                        grlDaysHolder.adapter = adapterDatesHolder
                        TimeUnit.MILLISECONDS.sleep(500L)
                        prbarCalendarProgressBar.visibility = View.GONE
                    }
                }

            }, curdate.month.toString(), curdate.year.toString(), curdate.month.maxLength())
        }

        btnPrevYear.setOnClickListener {
            prbarCalendarProgressBar.visibility = View.VISIBLE
            val changePeriod : Period = Period.of(-1,0,0)
            prevdate = prevdate.plus(changePeriod)
            curdate = curdate.plus(changePeriod)
            nextdate = nextdate.plus(changePeriod)

            btnPrevYear.text = prevdate.year.toString()
            btnCurYear.text = curdate.year.toString()
            btnNextYear.text = nextdate.year.toString()

            alDatesData.clear()

            fillGrlLayout(object: callbackDateData{
                override fun OnCallback(tmpDate: DateHolder) {
                    alDatesData.add(tmpDate)
                    if(alDatesData.size >= curdate.month.maxLength()){
                        val adapterDatesHolder = DateHolderAdapter(requireContext(), alDatesData)
                        grlDaysHolder.adapter = adapterDatesHolder
                        TimeUnit.MILLISECONDS.sleep(500L)
                        prbarCalendarProgressBar.visibility = View.GONE
                    }
                }

            }, curdate.month.toString(), curdate.year.toString(), curdate.month.maxLength())
        }

        btnPrevMth.setOnClickListener {
            prbarCalendarProgressBar.visibility = View.VISIBLE
            val changePeriod : Period = Period.of(0,-1,0)
            prevdate = prevdate.plus(changePeriod)
            curdate = curdate.plus(changePeriod)
            nextdate = nextdate.plus(changePeriod)

            btnPrevMth.text = prevdate.month.toString().substring(0,3)
            btnCurMth.text = curdate.month.toString().substring(0,3)
            btnNextMth.text = nextdate.month.toString().substring(0,3)

            btnPrevYear.text = prevdate.year.toString()
            btnCurYear.text = curdate.year.toString()
            btnNextYear.text = nextdate.year.toString()

            alDatesData.clear()

            fillGrlLayout(object: callbackDateData{
                override fun OnCallback(tmpDate: DateHolder) {
                    alDatesData.add(tmpDate)
                    if(alDatesData.size >= curdate.month.maxLength()){
                        val adapterDatesHolder = DateHolderAdapter(requireContext(), alDatesData)
                        grlDaysHolder.adapter = adapterDatesHolder
                        TimeUnit.MILLISECONDS.sleep(500L)
                        prbarCalendarProgressBar.visibility = View.GONE
                    }
                }

            }, curdate.month.toString(), curdate.year.toString(), curdate.month.maxLength())
        }

        grlDaysHolder.setOnItemClickListener { adapterView, view, i, l ->
            var fragmentToChange = FragmentCurrentDay()
            var tmpBundle : Bundle = Bundle()
            tmpBundle.putString("email",stEmailUser)
            tmpBundle.putString("date", alDatesData[i].stDate.toString())
            tmpBundle.putString("month", curdate.month.toString())
            tmpBundle.putString("year", curdate.year.toString())
            fragmentToChange.arguments = tmpBundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.work_fragment_holder, fragmentToChange)
            transaction.commit()
        }

    }

    fun fillGrlLayout (callbackDateData: callbackDateData, chosenMonth:String, chosenYear : String, monthLength : Int) {
        alDatesData.clear()
        for (i in 1..monthLength ){
            var tmpDate : DateHolder
            database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).child(resources.getString(R.string.db_days_str)).child(chosenYear).child(chosenMonth).child(i.toString()).get().addOnSuccessListener{
                if (it.value == null){
                    System.out.println("Error: "+ chosenYear + ","+chosenMonth+","+i.toString())
                    tmpDate = DateHolder(i.toString(),0)
                    callbackDateData.OnCallback(tmpDate)
                }else{
                    tmpDate = DateHolder(i.toString(),it.child("SwitchesState").value.toString().toInt())
                    System.out.println("Success")
                    callbackDateData.OnCallback(tmpDate)
                }
            }
        }
    }

    interface callbackDateData{
        fun OnCallback(tmpDate : DateHolder)
    }

    interface callBackProfileData{
        fun OnCallback(iSwitchState : Int)
    }

    fun getDataFromFirebase(callBackProfileData: callBackProfileData,stCurMth : String){
        database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).child(resources.getString(R.string.db_days_str)).child(SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())).child(stCurMth).child(SimpleDateFormat("dd", Locale.getDefault()).format(Date())).get().addOnSuccessListener {
            callBackProfileData.OnCallback( it.child("SwitchesState").value.toString().toInt())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentCurrentDay()
    }
}