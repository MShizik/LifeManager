package com.example.life_manager

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    private val database : DatabaseReference = Firebase.database.reference

    private var stEmailUser : String = "aboba"
    private var iSwitchesState : Int = 0

    private var percentesProductive : Int = 0
    private var percentesInterest : Int = 0
    private var percentesOrdinary : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var btnCurDay : Button = view.findViewById(R.id.pro_btn_day)

        var tvPercentsInterest : TextView = view.findViewById(R.id.pro_statistic_interest_percents_txt)
        var tvPercentsProductive : TextView = view.findViewById(R.id.pro_statistic_productive_percents_txt)
        var tvPercentsOrdinary : TextView = view.findViewById(R.id.pro_statistic_ordinary_percents_txt)

        stEmailUser = arguments?.getString("email").toString()

        btnCurDay.text = SimpleDateFormat("dd", Locale.getDefault()).format(Date())

        getDataFromFirebase()

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

        tvPercentsInterest.text = percentesInterest.toString() + "%"
        tvPercentsOrdinary.text = percentesOrdinary.toString() + "%"
        tvPercentsProductive.text = percentesProductive.toString() + "%"




    }

    fun getDataFromFirebase(){
        database.child("users").child(stEmailUser).child("days").child(SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())).child(SimpleDateFormat("MM", Locale.getDefault()).format(Date())).child(SimpleDateFormat("dd", Locale.getDefault()).format(Date())).get().addOnSuccessListener {
            if(it.value != null) {
                iSwitchesState = it.child("SwitchesState").value.toString().toInt()
            }
        }
    }


    fun getFriendsData(){
        database.child("users").child(stEmailUser).get().addOnSuccessListener {
            var  tmpProductiveCount : Int = 0
            var tmpInterestCount : Int = 0
            var tmpOrdinaryCount : Int = 0

            tmpProductiveCount = it.child("countProductive").value.toString().toInt()
            tmpInterestCount = it.child("countInterest").value.toString().toInt()
            tmpOrdinaryCount = it.child("countOrdinary").value.toString().toInt()

            percentesProductive = tmpProductiveCount / (tmpProductiveCount + tmpInterestCount + tmpOrdinaryCount) * 100
            percentesInterest = tmpInterestCount / (tmpProductiveCount + tmpInterestCount + tmpOrdinaryCount) * 100
            percentesOrdinary = tmpInterestCount / (tmpProductiveCount + tmpInterestCount + tmpOrdinaryCount) * 100

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentCurrentDay()
    }
}