package com.example.life_manager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton

class DateHolderAdapter(
    private val context: Context,
    private var alDates: ArrayList<DateHolder>
) :
    BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var btnChosenDate: AppCompatButton

    override fun getCount(): Int {
        return alDates.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View? {
        var convertView = convertView
        if (convertView != null) {
            convertView.elevation = 0F
        }
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.card_layout, null)
        }
        btnChosenDate = convertView!!.findViewById(R.id.calendar_btn_day)
        btnChosenDate.text = alDates[position].stDate
        when (alDates[position].iSwitchState){
            1->{
                btnChosenDate.setBackgroundResource(R.drawable.rounded_day_ordinary)
            }
            2->{
                btnChosenDate.setBackgroundResource(R.drawable.rounded_day_interest)
            }
            3->{
                btnChosenDate.setBackgroundResource(R.drawable.rounded_day_productive)
            }
        }

        return convertView
    }
}