package com.example.life_manager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.widget.SwitchCompat

class FragmentCurrentDay : Fragment() {

    private var btnInterestSwtch : SwitchCompat? = null
    private var btnProductiveSwtch : SwitchCompat? = null
    private var btnOrdinarySwtch : SwitchCompat? = null
    private var btnCurrentDay : Button? = null
    private var btnSaveUser : Button? = null

    private var etDiaryUser : EditText? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_current_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




    }



    companion object {
        @JvmStatic
        fun newInstance() = FragmentCurrentDay()
    }
}