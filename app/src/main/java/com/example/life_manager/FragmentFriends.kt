package com.example.life_manager

import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
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

        val btnFriendListOpen : Button = view.findViewById(R.id.friend_friend_list_btn)
        val btnInviteListOpen : Button = view.findViewById(R.id.friend_invite_list_btn)
        val btnInviteFriend : Button = view.findViewById(R.id.friend_friend_list_new_btn)

        val lvFriendListView : ListView = view.findViewById(R.id.friend_list_view)
        val lvInviteListView : ListView = view.findViewById(R.id.friend_list_view)

        getFriendsData()
        getInviteData()

        var adapterFriendsList : DataFriendAdapter = DataFriendAdapter(this, alFriendList)
        var adapterInviteList : DataInviteAdapter = DataInviteAdapter(this, alInviteList)

        lvFriendListView.adapter = adapterFriendsList
        lvInviteListView.adapter = adapterInviteList

        btnInviteListOpen.setOnClickListener{
            lvFriendListView.visibility = View.INVISIBLE
            lvInviteListView.visibility = View.VISIBLE
        }

        btnFriendListOpen.setOnClickListener {
            lvFriendListView.visibility = View.VISIBLE
            lvInviteListView.visibility = View.INVISIBLE
        }
    }

    fun getFriendsData(){
        database.child("users").child(stEmailuser).child("friendlist").get().addOnSuccessListener {
            for (i in it.children){
                var tmpNameFriend : String
                var tmpSurNameFriend : String
                var tmpNickNameFriend : String
                var tmpLastNotion : String

                var tmpProductiveCount : Int
                var tmpInterestCount : Int
                var tmpOrdinaryCount : Int
                var percentesProductive : Int
                var percentesInterest : Int
                var percentsOrdinary : Int

                database.child("users").child(i.key.toString()).get().addOnSuccessListener { dsFriend -> DataSnapshot
                    tmpNameFriend = dsFriend.child("name").value.toString()
                    tmpSurNameFriend = dsFriend.child("surname").value.toString()
                    tmpNickNameFriend = dsFriend.child("nickname").value.toString()
                    tmpLastNotion = dsFriend.child("lastnotion").value.toString()

                    tmpProductiveCount = dsFriend.child("countProductive").value.toString()
                    tmpInterestCount = dsFriend.child("countInterest").value.toString()
                    tmpOrdinaryCount = dsFriend.child("countOrdinary").value.toString()
                }

                percentesProductive = tmpProductiveCount / (tmpProductiveCount + tmpInterestCount + tmpOrdinaryCount) * 100
                percentesInterest = tmpInterestCount / (tmpProductiveCount + tmpInterestCount + tmpOrdinaryCount) * 100
                percentesOrdinary = tmpInterestCount / (tmpProductiveCount + tmpInterestCount + tmpOrdinaryCount) * 100

                var tmpDataObj : DataHolder = DataHolder(i.key.toString(), tmpNameFriend, tmpSurNameFriend, tmpNickNameFriend, tmpLastNotion, percentesProductive, percentesInterest, percentsOrdinary)
                alFriendList.add(tmpDataObj)
            }
        }
    }

    fun getInviteData(){
        database.child("users").child(stEmailuser).child("invitelist").get().addOnSuccessListener {
            for (i in it.children){
                var tmpNameFriend : String
                var tmpSurNameFriend : String
                var tmpNickNameFriend : String

                database.child("users").child(i.key.toString()).get().addOnSuccessListener { dsFriend -> DataSnapshot
                    tmpNameFriend = dsFriend.child("name").value.toString()
                    tmpSurNameFriend = dsFriend.child("surname").value.toString()
                    tmpNickNameFriend = dsFriend.child("nickname").value.toString()
                }

                var tmpDataObj : inviteListDataHolder = inviteListDataHolder(tmpNameFriend, tmpSurNameFriend, tmpNickNameFriend, i.key.toString(), stEmailuser)
                alInviteList.add(tmpDataObj)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentCurrentDay()
    }
}