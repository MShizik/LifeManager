package com.example.life_manager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FragmentFriends : Fragment() {

    val stEmailuser : String = "aboba"

    private var alFriendList : ArrayList<DataHolder> = arrayListOf()
    private var alInviteList : ArrayList<inviteListDataHolder> = arrayListOf()

    val database  = Firebase.database.reference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_current_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnFriendListOpen : button = view.findViewById(R.id.friend_friend_list_btn)
        val btnInviteListOpen : Button = view.findViewById(R.id.friend_invite_list_btn)
        val btnInviteFriend : Button = view.findViewById(R.id.friend_friend_list_new_btn)

        val lvFriendListView : ListView = view.findViewById(R.id.friend_list_view)
        val lvInviteListView : ListView = view.findViewById(R.id.friend_list_view)







    }

    fun getFirendsData(){
        database.child("users").child(stEmailuser).child("friendlist").get().addOnSuccessListener {
            for (i in it.children){
                var tmpNameFriend : String = database.child("users").child(i.key).child("name").getValue().toString()
                var tmpSurNameFriend : String = database.child("users").child(i.key).child("surname").getValue().toString()
                var tmpNickNameFriend : String = database.child("users").child(i.key).child("nickname").getValue().toString()
                var tmpProductiveCount : Int = database.child("users").child(i.key).child("countProductive").getValue().toString().toInt()
                var tmpInterestCount : Int = database.child("users").child(i.key).child("countInterest").getValue().toString().toInt()
                var tmpOrdinaryCount : Int = database.child("users").child(i.key).child("countOrdinary").getValue().toString().toInt()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentCurrentDay()
    }
}