package com.example.life_manager

import android.graphics.Color
import android.media.tv.TvContract
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Switch
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class FragmentCurrentDay : Fragment() {

    private val database : DatabaseReference = Firebase.database.reference
    private var countProductive = 0
    private var countInterest = 0
    private var countOrdinary = 0
    private var iSwitchesState = 0
    private var iPreviousState = 0
    private var stEmailUser : String = "aboba"
    private var stNoteUser : String = "aboba"
    private var chosenYear : String = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())
    private var chosenMonth : String = SimpleDateFormat("MM", Locale.getDefault()).format(Date())
    private var chosenDate : String = SimpleDateFormat("dd", Locale.getDefault()).format(Date())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_current_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCurDay : AppCompatButton = view.findViewById(R.id.cur_btn_day)
        val btnSaveUser : Button = view.findViewById(R.id.cur_day_save_btn)

        val switchOrdinaryDay : SwitchCompat = view.findViewById(R.id.cur_day_switch_ordinary)
        val switchProductiveDay : SwitchCompat = view.findViewById(R.id.cur_day_switch_productive)
        val switchInterestDay : SwitchCompat = view.findViewById(R.id.cur_day_switch_interest)

        val etNoteUser : EditText = view.findViewById(R.id.cur_et_day)

        val prbarCurDay  = view.findViewById(R.id.progress_layout) as ConstraintLayout
        prbarCurDay.visibility = View.VISIBLE


        stEmailUser = arguments?.getString("email").toString()

        chosenDate = arguments?.getString("date").toString()
        chosenMonth = arguments?.getString("month").toString()
        chosenYear = arguments?.getString("year").toString()

        getDataFromFirebase(object : callbackCurDayData{
            override fun OnCallback() {
                when(iSwitchesState){
                    1 -> {
                        switchOrdinaryDay.isChecked = true
                        btnCurDay.setBackgroundResource(R.drawable.rounded_day_ordinary)
                    }

                    2 -> {
                        switchInterestDay.isChecked = true
                        btnCurDay.setBackgroundResource(R.drawable.rounded_day_interest)
                    }

                    3 -> {
                        switchProductiveDay.isChecked = true
                        btnCurDay.setBackgroundResource(R.drawable.rounded_day_productive)
                    }
                }

                if(stNoteUser != "aboba"){
                    etNoteUser.setText(stNoteUser)
                }
                TimeUnit.MILLISECONDS.sleep(500L)
                prbarCurDay.visibility = View.GONE
            }
        })

        btnCurDay.text = chosenDate.toString()

        switchInterestDay.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                switchOrdinaryDay.isChecked = false
                switchProductiveDay.isChecked = false
                iSwitchesState = 2
                btnCurDay.setBackgroundResource(R.drawable.rounded_day_interest)
            }
            else{
                iSwitchesState = 0
                btnCurDay.setBackgroundResource(R.drawable.rounded_day)
            }
        }

        switchOrdinaryDay.setOnCheckedChangeListener { buttonView, isChecked  ->
            if(isChecked){
                switchInterestDay.isChecked = false
                switchProductiveDay.isChecked = false
                iSwitchesState = 1
                btnCurDay.setBackgroundResource(R.drawable.rounded_day_ordinary)
            }
            else{
                iSwitchesState = 0
                btnCurDay.setBackgroundResource(R.drawable.rounded_day)
            }
        }

        switchProductiveDay.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(isChecked){
                switchInterestDay.isChecked = false
                switchOrdinaryDay.isChecked = false
                iSwitchesState = 3
                btnCurDay.setBackgroundResource(R.drawable.rounded_day_productive)

            }
            else{
                iSwitchesState = 0
                btnCurDay.setBackgroundResource(R.drawable.rounded_day)
            }

        }

        btnSaveUser.setOnClickListener{
            var tmpDataSaver : Int = 0
            if (iPreviousState == 0){
                when (iSwitchesState){
                    1-> countOrdinary +=1
                    2->countInterest+=1
                    3->countProductive+=1
                }
            }else if(iPreviousState != iSwitchesState){
                when (iSwitchesState){
                    1-> countOrdinary +=1
                    2->countInterest+=1
                    3->countProductive+=1
                }
                when (iPreviousState)
                {
                    1-> countOrdinary -=1
                    2->countInterest-=1
                    3->countProductive-=1
                }
            }
            database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).child(resources.getString(R.string.db_count_ordinary_str)).setValue(countOrdinary)
            database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).child(resources.getString(R.string.db_count_inter_str)).setValue(countInterest)
            database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).child(resources.getString(R.string.db_count_prod_str)).setValue(countProductive)
            database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).child(resources.getString(R.string.db_last_notion_str)).setValue(etNoteUser.text.toString())
            database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).child(resources.getString(R.string.db_days_str)).child(chosenYear).child(chosenMonth).child(chosenDate).child("SwitchesState").setValue(iSwitchesState)
            database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).child(resources.getString(R.string.db_days_str)).child(chosenYear).child(chosenMonth).child(chosenDate).child("Notion").setValue(etNoteUser.text.toString())
        }


    }

    fun getDataFromFirebase(callbackCurDayData: callbackCurDayData){
        database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).get().addOnSuccessListener {
            if(it.value != null) {
                countInterest = it.child(resources.getString(R.string.db_count_inter_str)).value.toString().toInt()
                countOrdinary = it.child(resources.getString(R.string.db_count_ordinary_str)).value.toString().toInt()
                countProductive = it.child(resources.getString(R.string.db_count_prod_str)).value.toString().toInt()
                if(it.child(resources.getString(R.string.db_days_str)).child(chosenYear).child(chosenMonth).child(chosenDate).child("SwitchesState").value != null){
                    iSwitchesState = it.child(resources.getString(R.string.db_days_str)).child(chosenYear).child(chosenMonth).child(chosenDate).child("SwitchesState").value.toString().toInt()
                }
                iPreviousState = iSwitchesState
                if (it.child(resources.getString(R.string.db_days_str)).child(chosenYear).child(chosenMonth).child(chosenDate).child("Notion").value != null){
                    stNoteUser = it.child(resources.getString(R.string.db_days_str)).child(chosenYear).child(chosenMonth).child(chosenDate).child("Notion").value.toString()
                }
                callbackCurDayData.OnCallback()
            }
        }
    }

    interface callbackCurDayData{
        fun OnCallback()
    }


    companion object {
        @JvmStatic
        fun newInstance() = FragmentCurrentDay()
    }
}