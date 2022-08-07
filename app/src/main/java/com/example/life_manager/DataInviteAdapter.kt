package com.example.life_manager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class DataInviteAdapter  (private val context: Context, private val arrayList: java.util.ArrayList<inviteListDataHolder>) : BaseAdapter() {

    private lateinit var tvFirstName: TextView
    private lateinit var tvSurName : TextView
    private lateinit var tvNickName : TextView
    private lateinit var btnAccept : Button
    private lateinit var btnDecline : Button
    private var database = Firebase.database.reference


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
        convertView = LayoutInflater.from(context).inflate(R.layout.custom_invite_list_row, parent, false)

        tvFirstName = convertView.findViewById(R.id.invite_list_name_txt)
        tvSurName = convertView.findViewById(R.id.invite_list_surname_txt)
        tvNickName = convertView.findViewById(R.id.invite_list_nickname_txt)

        btnAccept = convertView.findViewById(R.id.invite_accept_btn)
        btnDecline = convertView.findViewById(R.id.invite_decline_btn)

        tvFirstName.text = arrayList[position].stFirstName
        tvSurName.text = arrayList[position].stSurName
        tvNickName.text = arrayList[position].stNickName

        btnAccept.setOnClickListener {
            database.child(arrayList[position].stEmailUser).child("invitelist").child(arrayList[position].stFriendEmail).get().addOnSuccessListener {
                database.child(arrayList[position].stEmailUser).child("friendlist").child(arrayList[position].stFriendEmail).setValue(1)
                database.child(arrayList[position].stEmailUser).child("invitelist").child(arrayList[position].stFriendEmail).delete()
            }
        }
        btnDecline.setOnClickListener {
            database.child(arrayList[position].stEmailUser).child("invitelist").child(arrayList[position].stFriendEmail).get().addOnSuccessListener{
                database.child(arrayList[position].stEmailUser).child("invitelist").child(arrayList[position].stFriendEmail).delete()
            }
        }

        return convertView
    }
}