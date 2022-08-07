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

        var adapterFriendsList : DataFriendAdapter = DataFriendAdapter(requireContext(), alFriendList)
        var adapterInviteList : DataInviteAdapter = DataInviteAdapter(requireContext(), alInviteList)

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
                var tmpNameFriend : String = ""
                var tmpSurNameFriend : String = ""
                var tmpNickNameFriend : String = ""
                var tmpLastNotion : String = ""

                var  tmpProductiveCount : Int = 0
                var tmpInterestCount : Int = 0
                var tmpOrdinaryCount : Int = 0
                var percentesProductive : Int = 0
                var percentesInterest : Int = 0
                var percentesOrdinary : Int = 0

                database.child("users").child(i.key.toString()).get().addOnSuccessListener {
                    tmpNameFriend = it.child("name").value.toString()
                    tmpSurNameFriend = it.child("surname").value.toString()
                    tmpNickNameFriend = it.child("nickname").value.toString()
                    tmpLastNotion = it.child("lastnotion").value.toString()

                    tmpProductiveCount = it.child("countProductive").value.toString().toInt()
                    tmpInterestCount = it.child("countInterest").value.toString().toInt()
                    tmpOrdinaryCount = it.child("countOrdinary").value.toString().toInt()
                }

                percentesProductive = tmpProductiveCount / (tmpProductiveCount + tmpInterestCount + tmpOrdinaryCount) * 100
                percentesInterest = tmpInterestCount / (tmpProductiveCount + tmpInterestCount + tmpOrdinaryCount) * 100
                percentesOrdinary = tmpInterestCount / (tmpProductiveCount + tmpInterestCount + tmpOrdinaryCount) * 100

                var tmpDataObj : DataHolder = DataHolder(i.key.toString(), tmpNameFriend, tmpSurNameFriend, tmpNickNameFriend, tmpLastNotion, percentesProductive.toString(), percentesInterest.toString(), percentesOrdinary.toString())
                alFriendList.add(tmpDataObj)
            }
        }
    }

    fun getInviteData(){
        database.child("users").child(stEmailuser).child("invitelist").get().addOnSuccessListener {
            for (i in it.children){
                var tmpNameFriend : String = ""
                var tmpSurNameFriend : String = ""
                var tmpNickNameFriend : String = ""

                database.child("users").child(i.key.toString()).get().addOnSuccessListener {
                    tmpNameFriend = it.child("name").value.toString()
                    tmpSurNameFriend = it.child("surname").value.toString()
                    tmpNickNameFriend = it.child("nickname").value.toString()
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