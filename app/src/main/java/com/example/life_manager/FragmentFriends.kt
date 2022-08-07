package com.example.life_manager


import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FragmentFriends : Fragment() {

    val stEmailUser : String = "aboba"

    private var alFriendList : ArrayList<DataHolder> = arrayListOf()
    private var alInviteList : ArrayList<inviteListDataHolder> = arrayListOf()

    val database  = Firebase.database.reference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnFriendListOpen : Button = view.findViewById(R.id.friend_friend_list_btn)
        val btnInviteListOpen : Button = view.findViewById(R.id.friend_invite_list_btn)
        val btnInviteFriend : Button = view.findViewById(R.id.friend_friend_list_new_btn)

        val lvFriendListView : ListView = view.findViewById(R.id.friend_list_view)
        val lvInviteListView : ListView = view.findViewById(R.id.friend_list_view)

        getFriendsData()

        var adapterFriendsList : DataFriendAdapter = DataFriendAdapter(requireContext(), alFriendList)
        var adapterInviteList : DataInviteAdapter = DataInviteAdapter(requireContext(), alInviteList)

        lvFriendListView.adapter = adapterFriendsList
        lvInviteListView.adapter = adapterInviteList

        btnInviteListOpen.setOnClickListener{
            getInviteData()
            lvFriendListView.visibility = View.INVISIBLE
            lvInviteListView.visibility = View.VISIBLE
        }

        btnFriendListOpen.setOnClickListener {
            getFriendsData()
            lvFriendListView.visibility = View.VISIBLE
            lvInviteListView.visibility = View.INVISIBLE
        }

        btnInviteFriend.setOnClickListener {
            startDialogInviteFriend()
        }
        lvFriendListView.setOnItemLongClickListener { parent, view, position, id ->
            startDialogDeleteFriend(alFriendList[position].stFriendEmail)
            return@setOnItemLongClickListener(true)
        }
    }

    fun getFriendsData(){
        database.child("users").child(stEmailUser).child("friendlist").get().addOnSuccessListener {
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
        database.child("users").child(stEmailUser).child("invitelist").get().addOnSuccessListener {
            for (i in it.children){
                var tmpNameFriend : String = ""
                var tmpSurNameFriend : String = ""
                var tmpNickNameFriend : String = ""

                database.child("users").child(i.key.toString()).get().addOnSuccessListener {
                    tmpNameFriend = it.child("name").value.toString()
                    tmpSurNameFriend = it.child("surname").value.toString()
                    tmpNickNameFriend = it.child("nickname").value.toString()
                }

                var tmpDataObj : inviteListDataHolder = inviteListDataHolder(tmpNameFriend, tmpSurNameFriend, tmpNickNameFriend, i.key.toString(), stEmailUser)
                alInviteList.add(tmpDataObj)
            }
        }
    }

    fun startDialogInviteFriend(){
        Handler(Looper.getMainLooper()).post {
            val dialog_find_friend = Dialog(requireContext())
            dialog_find_friend.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog_find_friend.setContentView(R.layout.dialog_add_friend)
            val etEmailUser = dialog_find_friend.findViewById(R.id.add_text_email_field) as EditText
            val dialog_util_confirmation_btn = dialog_find_friend.findViewById(R.id.add_find_btn) as Button
            dialog_util_confirmation_btn.setOnClickListener {
                database.child("users").child(etEmailUser.text.toString()).child("invitelist").child(stEmailUser).setValue(1)
                dialog_find_friend.cancel()
            }
            dialog_find_friend.setCancelable(true)
            dialog_find_friend.show()
        }
    }

    fun startDialogDeleteFriend(stEmailFriend : String) {
        Handler(Looper.getMainLooper()).post {
            val dialog_delete_friend = Dialog(requireContext())
            dialog_delete_friend.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog_delete_friend.setContentView(R.layout.dialog_delete_friend)
            val btnDeleteFriend =
                dialog_delete_friend.findViewById(R.id.delete_accept_btn) as Button
            val btnDeclineDelete =
                dialog_delete_friend.findViewById(R.id.delete_decline_btn) as Button

            btnDeclineDelete.setOnClickListener {
                dialog_delete_friend.cancel()
            }
            btnDeleteFriend.setOnClickListener {
                database.child("users").child(stEmailUser).child("friendlist").child(stEmailFriend)
                    .removeValue()
                database.child("users").child(stEmailFriend).child("friendlist").child(stEmailUser)
                    .removeValue()
                dialog_delete_friend.cancel()
            }
            dialog_delete_friend.setCancelable(true)
            dialog_delete_friend.show()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = FragmentCurrentDay()
    }
}