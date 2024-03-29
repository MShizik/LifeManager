package com.example.life_manager

import android.app.*
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.system.Os.remove
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
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
    private lateinit var btnSetNotification : Button

    private lateinit var tvPercentsOrdinary : TextView
    private lateinit var tvPercentsInterest : TextView
    private lateinit var tvPercentsProductive : TextView

    private lateinit var progressbarProfile : ConstraintLayout
    private lateinit var popupNotificationLayout : ConstraintLayout



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
        btnSetNotification = view.findViewById(R.id.pro_set_schedudle)

        tvPercentsInterest = view.findViewById(R.id.pro_statistic_interest_percents_txt)
        tvPercentsProductive = view.findViewById(R.id.pro_statistic_productive_percents_txt)
        tvPercentsOrdinary = view.findViewById(R.id.pro_statistic_ordinary_percents_txt)

        progressbarProfile  = view.findViewById(R.id.progress_layout)
        progressbarProfile.visibility = View.VISIBLE

        popupNotificationLayout = view.findViewById(R.id.popup_constraint_layout)
        popupNotificationLayout.visibility = View.GONE



        btnCurDay.text = SimpleDateFormat("dd", Locale.getDefault()).format(Date())

        getProfileData()

        btnCurDay.setOnClickListener {
            val fragmentToChange = FragmentCurrentDay()
            val tmpBundle = Bundle()
            tmpBundle.putString("email",stEmailUser)
            tmpBundle.putSerializable("curdate",LocalDate.parse(java.time.format.DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond(Math.round((System.currentTimeMillis() / 1000).toDouble()).toLong())),DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
            tmpBundle.putInt("prevfragment",2)
            fragmentToChange.arguments = tmpBundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.work_fragment_holder, fragmentToChange)
            transaction.commit()
        }

        btnChangeUser.setOnClickListener {
            val preferencesUserData = requireContext().getSharedPreferences("user",Context.MODE_PRIVATE)
            preferencesUserData.edit().remove("email").apply()
            preferencesUserData.edit().remove("password").apply()
            val intentToWorkActivity = Intent(requireContext(), MainActivity::class.java)
            startActivity(intentToWorkActivity)
        }

        btnSetNotification.setOnClickListener{
            createSchedudlePopup()
            createNotificationChannel()
            scheduleNotification()
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
        progressbarProfile.animate().setDuration(1000L).alpha(0.0f).withEndAction(Runnable {
            progressbarProfile.visibility = View.GONE
        })

        percentsProductive = iProductivePercents
        percentsInterest = iInterestPercents
        percentsOrdinary = iOrdinaryPercents
        tvPercentsInterest.text = (percentsInterest*100).toInt().toString() + "%"
        tvPercentsOrdinary.text = (percentsOrdinary*100).toInt().toString() + "%"
        tvPercentsProductive.text = (percentsProductive*100).toInt().toString() + "%"

    }


    private fun scheduleNotification()
    {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 22)
        calendar.set(Calendar.MINUTE, 30)
        calendar.set(Calendar.SECOND, 0)

        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        val intent = Intent(requireContext(), MemoBroadcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.getTimeInMillis(),
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
            )
        }
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "PASTICCINO"
            val description = "PASTICCINO`S CHANNEL"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Notification", name, importance)
            channel.description = description
            val notificationManager = activity?.getSystemService(
                NotificationManager::class.java
            )
            notificationManager?.createNotificationChannel(channel)
        }
    }

    //EggTimerFragment.kt

    private fun createSchedudlePopup(){
        popupNotificationLayout.visibility = View.VISIBLE
        popupNotificationLayout.translationY = (-150).toFloat()
        popupNotificationLayout.animate().translationY(0F).setDuration(1000L).withEndAction{popupNotificationLayout.animate().translationY(popupNotificationLayout.height*(-1).toFloat()).setDuration(1000L).withEndAction{popupNotificationLayout.visibility = View.GONE}}
    }


    companion object {
        @JvmStatic
        fun newInstance() = FragmentCurrentDay()
    }
}