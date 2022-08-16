package com.example.life_manager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.w3c.dom.Text

class DataFriendAdapter  (private val context: Context, private val arrayList: java.util.ArrayList<DataHolder>) : BaseAdapter() {

        private lateinit var tvFirstName: TextView
        private lateinit var tvSurName : TextView
        private lateinit var tvNickName : TextView
        private lateinit var tvLastNotion : TextView
        private lateinit var tvPercentsProductive : TextView
        private lateinit var tvPercentsInterest : TextView
        private lateinit var tvPercentsOrdinary : TextView


        override fun getCount(): Int {
            return arrayList.size
        }


        override fun getItem(position: Int): Any {
            return position
        }


        override fun getItemId(position: Int): Long {
            return position.toLong()
        }


        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            var convertView = convertView
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_friend_list_row, parent, false)

            tvFirstName = convertView.findViewById(R.id.friend_list_name_txt)
            tvSurName = convertView.findViewById(R.id.friend_list_surname_txt)
            tvNickName = convertView.findViewById(R.id.friend_list_nickname_txt)
            tvLastNotion = convertView.findViewById(R.id.friend_list_last_notion_txt)
            tvPercentsProductive = convertView.findViewById(R.id.friend_list_percentages_productive_txt)
            tvPercentsInterest = convertView.findViewById(R.id.friend_list_percentages_interest_txt)
            tvPercentsOrdinary = convertView.findViewById(R.id.friend_list_percentages_ordinary_txt)

            tvFirstName.text = arrayList[position].stFirstName
            tvSurName.text = arrayList[position].stSurName
            tvNickName.text = arrayList[position].stNickName
            tvLastNotion.text = arrayList[position].stLastNotion
            tvPercentsProductive.text = arrayList[position].stPercentsProductive + "%"
            tvPercentsInterest.text = arrayList[position].stPercentsInterest + "%"
            tvPercentsOrdinary.text = arrayList[position].stPercentsOrdinary + "%"

            return convertView
        }
    }