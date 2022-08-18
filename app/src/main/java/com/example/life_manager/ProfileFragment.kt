package com.example.life_manager

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import kotlin.math.roundToLong


class ProfileFragment : Fragment() {

    private val database : DatabaseReference = Firebase.database.reference

    private var stEmailUser = "default"

    var percentsProductive = 0.0F
    var percentsInterest = 0.0F
    var percentsOrdinary = 0.0F

    private lateinit var btnCurDay : Button
    private lateinit var btnChangeUser : Button

    private lateinit var tvPercentsOrdinary : TextView
    private lateinit var tvPercentsInterest : TextView
    private lateinit var tvPercentsProductive : TextView

    private lateinit var progressbarProfile : ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        stEmailUser = arguments?.getString("email").toString()
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnCurDay = view.findViewById(R.id.pro_btn_day)
        btnChangeUser = view.findViewById(R.id.pro_change_user)

        tvPercentsInterest = view.findViewById(R.id.pro_statistic_interest_percents_txt)
        tvPercentsProductive = view.findViewById(R.id.pro_statistic_productive_percents_txt)
        tvPercentsOrdinary = view.findViewById(R.id.pro_statistic_ordinary_percents_txt)

        progressbarProfile  = view.findViewById(R.id.progress_layout)
        progressbarProfile.visibility = View.VISIBLE

        btnCurDay.text = SimpleDateFormat("dd", Locale.getDefault()).format(Date())

        getProfileData()

        btnCurDay.setOnClickListener {
            val fragmentToChange = FragmentCurrentDay()
            val tmpBundle = Bundle()
            tmpBundle.putString("email",stEmailUser)
            tmpBundle.putString("date", SimpleDateFormat("dd", Locale.getDefault()).format(Date()))
            tmpBundle.putString("month", LocalDate.parse(
                DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond(
                    Math.round((System.currentTimeMillis() / 1000).toDouble())
                )),DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")).month.toString())
            tmpBundle.putString("year", SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()))
            fragmentToChange.arguments = tmpBundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.work_fragment_holder, fragmentToChange)
            transaction.commit()
        }

        btnChangeUser.setOnClickListener {
            val intentToWorkActivity = Intent(requireContext(), MainActivity::class.java)
            startActivity(intentToWorkActivity)
        }


    }


    private fun getProfileData() {
        database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).get().addOnSuccessListener {

            var tmpProductiveCount = it.child(resources.getString(R.string.db_count_prod_str)).value.toString().toFloat()
            var tmpInterestCount = it.child(resources.getString(R.string.db_count_inter_str)).value.toString().toFloat()
            var tmpOrdinaryCount = it.child(resources.getString(R.string.db_count_ordinary_str)).value.toString().toFloat()

            if(tmpInterestCount == 0.0F && tmpOrdinaryCount == 0.0F && tmpProductiveCount == 0.0F){

                tmpProductiveCount = 0.33F
                tmpInterestCount = 0.33F
                tmpOrdinaryCount = 0.33F

            }else {

                val tmpSum : Float = tmpProductiveCount + tmpInterestCount + tmpOrdinaryCount
                tmpProductiveCount /= tmpSum
                tmpOrdinaryCount /= tmpSum
                tmpInterestCount /= tmpSum

            }

            var iSwitchState = it.child(resources.getString(R.string.db_days_str)).child(SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())).child(LocalDate.parse(
                DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond(
                    (System.currentTimeMillis() / 1000).toDouble().roundToLong()
                )),DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")).month.toString()).child(SimpleDateFormat("dd", Locale.getDefault()).format(Date())).child(resources.getString(R.string.db_switches_state_str)).value.toString()
            if (iSwitchState == "null"){

                iSwitchState = "0"

            }
            setDataFromFirebase(tmpProductiveCount,tmpInterestCount, tmpOrdinaryCount,iSwitchState.toInt())

        }
    }

    private fun setDataFromFirebase(iProductivePercents : Float, iInterestPercents : Float, iOrdinaryPercents : Float, iSwitchState: Int){

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
        progressbarProfile.animate().setDuration(1000L).alpha(0.0f).withEndAction(Runnable { progressbarProfile.visibility = View.GONE })

        percentsProductive = iProductivePercents
        percentsInterest = iInterestPercents
        percentsOrdinary = iOrdinaryPercents
        tvPercentsInterest.text = (percentsInterest*100).toInt().toString() + "%"
        tvPercentsOrdinary.text = (percentsOrdinary*100).toInt().toString() + "%"
        tvPercentsProductive.text = (percentsProductive*100).toInt().toString() + "%"

    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentCurrentDay()
    }
}