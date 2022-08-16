package com.example.life_manager

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit


class ProfileFragment : Fragment() {

    private val database : DatabaseReference = Firebase.database.reference

    private var stEmailUser : String = "aboba"
    private var iSwitchesState : Int = 0

    var percentesProductive : Float = 0.0F
    var percentesInterest : Float = 0.0F
    var percentesOrdinary : Float = 0.0F

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        stEmailUser = arguments?.getString("email").toString()

        println("first_step")
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var btnCurDay : Button = view.findViewById(R.id.pro_btn_day)
        var btnChangeUser : Button = view.findViewById(R.id.pro_change_user)

        var tvPercentsInterest : TextView = view.findViewById(R.id.pro_statistic_interest_percents_txt)
        var tvPercentsProductive : TextView = view.findViewById(R.id.pro_statistic_productive_percents_txt)
        var tvPercentsOrdinary : TextView = view.findViewById(R.id.pro_statistic_ordinary_percents_txt)

        var prbarProfile  = view.findViewById(R.id.progress_layout) as ConstraintLayout
        prbarProfile.visibility = View.VISIBLE

        btnCurDay.text = SimpleDateFormat("dd", Locale.getDefault()).format(Date())

        getDataFromFirebase(object: callbackDayData{
            override fun onCallback(iSwitchState: Int) {
                when(iSwitchesState){
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
                TimeUnit.MILLISECONDS.sleep(500L)
                prbarProfile.visibility = View.GONE
            }
        })

        btnCurDay.setOnClickListener {
            var fragmentToChange = FragmentCurrentDay()
            var tmpBundle : Bundle = Bundle()
            tmpBundle.putString("email",stEmailUser)
            tmpBundle.putString("date", SimpleDateFormat("dd", Locale.getDefault()).format(Date()))
            tmpBundle.putString("month", LocalDate.parse(java.time.format.DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond(Math.round((System.currentTimeMillis() / 1000).toDouble()).toLong())),DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")).month.toString())
            tmpBundle.putString("year", SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()))
            fragmentToChange.arguments = tmpBundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.work_fragment_holder, fragmentToChange)
            transaction.commit()
        }

        btnChangeUser.setOnClickListener {
            var intentToWorkActivity = Intent(requireContext(), MainActivity::class.java)
            startActivity(intentToWorkActivity)
        }

        getProfileData(object : callbackProfileData {
            override fun onCallback(iProdPerc: Float, iInterPerc: Float, iOrdinPerc: Float) {
                percentesProductive = iProdPerc
                percentesInterest = iInterPerc
                percentesOrdinary = iOrdinPerc
                tvPercentsInterest.text = (percentesInterest*100).toInt().toString() + "%"
                tvPercentsOrdinary.text = (percentesOrdinary*100).toInt().toString() + "%"
                tvPercentsProductive.text = (percentesProductive*100).toInt().toString() + "%"
            }
        })

    }

    fun getDataFromFirebase(callbackDayData: callbackDayData){
        database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).child(resources.getString(R.string.db_days_str)).child(SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())).child(LocalDate.parse(java.time.format.DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond(Math.round((System.currentTimeMillis() / 1000).toDouble()).toLong())),DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")).month.toString()).child(SimpleDateFormat("dd", Locale.getDefault()).format(Date())).get().addOnSuccessListener {
            if(it.value != null) {
                iSwitchesState = it.child("SwitchesState").value.toString().toInt()
                callbackDayData.onCallback(iSwitchesState)
            }
        }
    }


    fun getProfileData(myCallBack: callbackProfileData) {
        database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).get().addOnSuccessListener {
            var  tmpProductiveCount : Float = 0.1F
            var tmpInterestCount : Float = 0.1F
            var tmpOrdinaryCount : Float = 0.1F
            println("second_step")
            tmpProductiveCount = it.child(resources.getString(R.string.db_count_prod_str)).value.toString().toFloat()
            tmpInterestCount = it.child(resources.getString(R.string.db_count_inter_str)).value.toString().toFloat()
            tmpOrdinaryCount = it.child(resources.getString(R.string.db_count_ordinary_str)).value.toString().toFloat()
            if(tmpInterestCount == 0.0F && tmpOrdinaryCount == 0.0F && tmpProductiveCount == 0.0F){
                myCallBack.onCallback(0.33F,0.33F, 0.33F)
            }else {
                println("third_step")
                var tmpSum : Float = tmpProductiveCount + tmpInterestCount + tmpOrdinaryCount
                myCallBack.onCallback(tmpProductiveCount/tmpSum,tmpInterestCount/tmpSum, tmpOrdinaryCount/tmpSum)
            }
        }
    }

    interface callbackProfileData {
        fun onCallback(iProdPerc : Float, iInterPerc : Float, iOrdinPerc : Float)
    }

    interface callbackDayData{
        fun onCallback(iSwitchState : Int)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentCurrentDay()
    }
}