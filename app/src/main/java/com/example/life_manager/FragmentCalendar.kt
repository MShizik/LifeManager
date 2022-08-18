package com.example.life_manager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToLong


class FragmentCalendar : Fragment() {

    private val database : DatabaseReference = Firebase.database.reference

    private var stEmailUser : String = "aboba"

    private var alDatesData : ArrayList<DateHolder> = arrayListOf()

    private var dateCurDate = LocalDate.parse(
        DateTimeFormatter.ISO_INSTANT.format(
            java.time.Instant.ofEpochSecond(
                (System.currentTimeMillis() / 1000).toDouble().roundToLong()
            )
        ), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))
    private var prevdate = dateCurDate.plus(Period.of(-1,-1,0))
    private var nextdate = dateCurDate.plus(Period.of(1,1,0))



    private lateinit var progressbarMainProgressBar: ConstraintLayout
    private lateinit var progressbarCalendarProgressBar : ConstraintLayout

    private lateinit var btnCurDay : Button
    private lateinit var btnPrevMth : Button
    private lateinit var btnCurMth : Button
    private lateinit var btnNextMth : Button
    private lateinit var btnPrevYear : Button
    private lateinit var btnCurYear : Button
    private lateinit var btnNextYear : Button

    private lateinit var grlDaysHolder : GridView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnCurDay = view.findViewById(R.id.calendar_btn_day)
        btnPrevMth = view.findViewById(R.id.cal_btn_prev_mth)
        btnCurMth = view.findViewById(R.id.cal_btn_cur_mth)
        btnNextMth = view.findViewById(R.id.cal_btn_next_mth)
        btnPrevYear = view.findViewById(R.id.cal_btn_prev_year)
        btnCurYear = view.findViewById(R.id.cal_btn_cur_year)
        btnNextYear = view.findViewById(R.id.cal_btn_next_year)

        grlDaysHolder = view.findViewById(R.id.calendar_days_holder)

        progressbarMainProgressBar  = view.findViewById(R.id.progress_layout) as ConstraintLayout
        progressbarCalendarProgressBar  = view.findViewById(R.id.progress_layout_calendar) as ConstraintLayout
        progressbarCalendarProgressBar.visibility = View.GONE

        showMainProgressBar()

        deleteGridLayout()

        stEmailUser = arguments?.getString("email").toString()

        getDataFromFirebase(dateCurDate.month.toString())

        fillArrayListOfDates(dateCurDate.month.toString(), dateCurDate.year.toString(), dateCurDate.month.maxLength())

        setButtonText()

        btnCurDay.setOnClickListener {
            val fragmentToChange = FragmentCurrentDay()
            val tmpBundle = Bundle()
            tmpBundle.putString("email",stEmailUser)
            tmpBundle.putString("date", dateCurDate.dayOfMonth.toString())
            tmpBundle.putString("month", dateCurDate.month.toString())
            tmpBundle.putString("year", dateCurDate.year.toString())
            fragmentToChange.arguments = tmpBundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.work_fragment_holder, fragmentToChange)
            transaction.commit()
        }

        btnNextYear.setOnClickListener {
            showCalendarProgressBar()

            changeDateByPeriod(1,0)

            alDatesData.clear()

            fillArrayListOfDates(dateCurDate.month.toString(), dateCurDate.year.toString(), dateCurDate.month.maxLength())
        }

        btnNextMth.setOnClickListener {
            showCalendarProgressBar()

            changeDateByPeriod(0,1)

            alDatesData.clear()

            fillArrayListOfDates(dateCurDate.month.toString(), dateCurDate.year.toString(), dateCurDate.month.maxLength())
        }

        btnPrevYear.setOnClickListener {
            showCalendarProgressBar()

            changeDateByPeriod(-1,0)

            alDatesData.clear()

            fillArrayListOfDates(dateCurDate.month.toString(), dateCurDate.year.toString(), dateCurDate.month.maxLength())
        }

        btnPrevMth.setOnClickListener {

            showCalendarProgressBar()

            changeDateByPeriod(0,-1)

            alDatesData.clear()

            fillArrayListOfDates(dateCurDate.month.toString(), dateCurDate.year.toString(), dateCurDate.month.maxLength())
        }

        grlDaysHolder.setOnItemClickListener { _, _, i, _ ->
            val fragmentToChange = FragmentCurrentDay()
            val tmpBundle  = Bundle()
            tmpBundle.putString("email",stEmailUser)
            tmpBundle.putString("date", alDatesData[i].stDate)
            tmpBundle.putString("month", dateCurDate.month.toString())
            tmpBundle.putString("year", dateCurDate.year.toString())
            fragmentToChange.arguments = tmpBundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.work_fragment_holder, fragmentToChange)
            transaction.commit()
        }

    }

    private fun setButtonText(){
        btnCurDay.text = dateCurDate.dayOfMonth.toString()
        btnCurMth.text = dateCurDate.month.toString().substring(0,3)
        btnCurYear.text = dateCurDate.year.toString()

        btnPrevYear.text = prevdate.year.toString()
        btnPrevMth.text = prevdate.month.toString().substring(0,3)

        btnNextYear.text = nextdate.year.toString()
        btnNextMth.text = nextdate.month.toString().substring(0,3)
    }

    private fun changeDateByPeriod(iYear : Int, iMonth : Int){

        val changePeriod : Period = Period.of(iYear,iMonth,0)

        prevdate = prevdate.plus(changePeriod)
        dateCurDate = dateCurDate.plus(changePeriod)
        nextdate = nextdate.plus(changePeriod)

        setButtonText()
    }


    private fun fillArrayListOfDates ( chosenMonth : String, chosenYear : String, monthLength : Int) {
        alDatesData.clear()
        for (i in 1..monthLength ){
            var tmpDate : DateHolder
            database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).child(resources.getString(R.string.db_days_str)).child(chosenYear).child(chosenMonth).child(i.toString()).get().addOnSuccessListener{
                if (it.value == null){
                    tmpDate = DateHolder(i.toString(),0)
                    prepareGridLayout(tmpDate)
                }else{
                    tmpDate = DateHolder(i.toString(),it.child(resources.getString(R.string.db_switches_state_str)).value.toString().toInt())
                    prepareGridLayout(tmpDate)
                }
            }
        }
    }

    private fun prepareGridLayout(dateHolder : DateHolder){
        alDatesData.add(dateHolder)
        if(alDatesData.size >= dateCurDate.month.maxLength()){
            val adapterDatesHolder = DateHolderAdapter(requireContext(), alDatesData)
            grlDaysHolder.adapter = adapterDatesHolder
            showGridLayout()
            deleteMainProgressBar()
        }
        if(progressbarCalendarProgressBar.isVisible){
            deleteCalendarProgressBar()
        }
    }


    private fun getDataFromFirebase(stCurMth : String){
        database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).child(resources.getString(R.string.db_days_str)).child(SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())).child(stCurMth).child(SimpleDateFormat("dd", Locale.getDefault()).format(Date())).get().addOnSuccessListener {
            if(it.value != null) {
                prepareProfileData(it.child(resources.getString(R.string.db_switches_state_str)).value.toString().toInt())
            }else{
                prepareProfileData(0)
            }
        }
    }

    private fun prepareProfileData(iSwitchState : Int){

        when(iSwitchState){
            1 -> btnCurDay.setBackgroundResource(R.drawable.rounded_day_ordinary)

            2 -> btnCurDay.setBackgroundResource(R.drawable.rounded_day_interest)

            3 -> btnCurDay.setBackgroundResource(R.drawable.rounded_day_productive)
        }

    }


    private fun showMainProgressBar(){
        progressbarMainProgressBar.visibility = View.VISIBLE
        progressbarMainProgressBar.alpha = 1.0F

    }

    private fun deleteMainProgressBar(){
        progressbarMainProgressBar.animate()
            .setDuration(500L)
            .alpha(0.0f)
            .withEndAction{
                progressbarMainProgressBar.visibility = View.GONE
            }
    }

    private fun showCalendarProgressBar(){
        progressbarCalendarProgressBar.visibility = View.VISIBLE
        progressbarCalendarProgressBar.alpha = 1.0F
        deleteGridLayout()
    }

    private fun deleteCalendarProgressBar(){
        progressbarCalendarProgressBar.animate()
            .alpha(0.0f)
            .setDuration(500L)
            .withEndAction{
                progressbarCalendarProgressBar.visibility = View.GONE
            }
    }

    private fun showGridLayout(){
        grlDaysHolder.animate()
            .setDuration(500L)
            .alpha(1.0f)
            .setListener(null)
    }

    private fun deleteGridLayout(){
        grlDaysHolder.animate()
            .alpha(0.0f)
            .setDuration(250L)
            .setListener(null)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentCurrentDay()
    }
}