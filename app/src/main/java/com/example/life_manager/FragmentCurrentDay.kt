package com.example.life_manager

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class FragmentCurrentDay : Fragment() {

    private val database : DatabaseReference = Firebase.database.reference

    private var stEmailUser : String = "aboba"
    private var iSwitchesState : Int = 0
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

        btnCurDay.text = chosenDate.toString()

        when(iSwitchesState){
            1 -> {
                switchOrdinaryDay.isChecked = true
                btnCurDay.setBackgroundColor(Color.parseColor("#bbdefb"))
            }

            2 -> {
                switchInterestDay.isChecked = true
                btnCurDay.setBackgroundColor(Color.parseColor("#ffcc00"))
            }

            3 -> {
                switchProductiveDay.isChecked = true
                btnCurDay.setBackgroundColor(Color.parseColor("#f0989f"))
            }
        }

        if(stNoteUser != "aboba"){
            etNoteUser.setText(stNoteUser)
        }



        switchInterestDay.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                switchOrdinaryDay.isChecked = false
                switchProductiveDay.isChecked = false

                iSwitchesState = 2

                btnCurDay.setBackgroundColor(Color.parseColor("#ffcc00"))

            }
            else{
                iSwitchesState = 0

                btnCurDay.setBackgroundColor(Color.parseColor("#f5f5f5"))
            }
        }

        switchOrdinaryDay.setOnCheckedChangeListener { buttonView, isChecked  ->
            if(isChecked){
                switchInterestDay.isChecked = false
                switchProductiveDay.isChecked = false

                iSwitchesState = 1

                btnCurDay.setBackgroundColor(Color.parseColor("#bbdefb"))
            }
            else{
                iSwitchesState = 0

                btnCurDay.setBackgroundColor(Color.parseColor("#f5f5f5"))
            }
        }

        switchProductiveDay.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(isChecked){
                switchInterestDay.isChecked = false
                switchOrdinaryDay.isChecked = false

                iSwitchesState = 3

                btnCurDay.setBackgroundColor(Color.parseColor("#f0989f"))

            }
            else{
                iSwitchesState = 0

                btnCurDay.setBackgroundColor(Color.parseColor("#f5f5f5"))
            }

        }

        btnSaveUser.setOnClickListener{
            var tmpDataSaver : Int
            when(iSwitchesState) {

                3 ->{
                    database.child("users").child(stEmailUser).child("countProductive").get().addOnSuccessListener { tmpDataSaver = it.value.toString().toInt() }
                    database.child("users").child(stEmailUser).child("countProductive").setValue(tmpDataSaver+1)
                }
                2 ->{
                    database.child("users").child(stEmailUser).child("countInterest").get().addOnSuccessListener { tmpDataSaver = it.value.toString().toInt() }
                    database.child("users").child(stEmailUser).child("countInterest").setValue(tmpDataSaver+1)
                }
                1 ->{
                    database.child("users").child(stEmailUser).child("countOrdinary").get().addOnSuccessListener { tmpDataSaver = it.value.toString().toInt() }
                    database.child("users").child(stEmailUser).child("countOrdinary").setValue(tmpDataSaver+1)
                }
            }
            database.child("users").child(stEmailUser).child("lastnotion").setValue(etNoteUser.text.toString())
            database.child("users").child(stEmailUser).child("days").child(chosenYear).child(chosenMonth).child(chosenDate).child("SwitchesState").setValue(iSwitchesState)
            database.child("users").child(stEmailUser).child("days").child(chosenYear).child(chosenMonth).child(chosenDate).child("Notion").setValue(etNoteUser.text.toString())
        }


    }

    fun getDataFromFirebase(){
        database.child("users").child(stEmailUser).child("days").child(chosenYear).child(chosenMonth).child(chosenDate).get().addOnSuccessListener {
            iSwitchesState = it.child("SwitchesState").value.toString().toInt()
            stNoteUser = it.child("Notion").value.toString()
        }
    }



    companion object {
        @JvmStatic
        fun newInstance() = FragmentCurrentDay()
    }
}