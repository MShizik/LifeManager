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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FragmentFriends : Fragment() {

    var stEmailUser : String = "default"

    private var alFriendList : ArrayList<DataHolder> = arrayListOf()
    private var alInviteList : ArrayList<inviteListDataHolder> = arrayListOf()

    private lateinit var adapterFriendsList : DataFriendAdapter
    private lateinit var adapterInviteList : DataInviteAdapter

    private lateinit var lvFriendListView : ListView
    private lateinit var lvInviteListView : ListView

    private lateinit var btnFriendListOpen : Button
    private lateinit var btnInviteListOpen : Button
    private lateinit var btnInviteFriend : Button

    private lateinit var progressbarFriends : ConstraintLayout

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

        btnFriendListOpen = view.findViewById(R.id.friend_friend_list_btn)
        btnInviteListOpen = view.findViewById(R.id.friend_invite_list_btn)
        btnInviteFriend = view.findViewById(R.id.friend_friend_list_new_btn)

        lvFriendListView = view.findViewById(R.id.friend_list_view)
        lvInviteListView = view.findViewById(R.id.friend_invite_list_view)

        progressbarFriends = view.findViewById(R.id.progress_layout) as ConstraintLayout
        progressbarFriends.visibility = View.VISIBLE

        adapterFriendsList = DataFriendAdapter(requireContext(), alFriendList)
        adapterInviteList = DataInviteAdapter(requireContext(), alInviteList)

        lvFriendListView.adapter = adapterFriendsList
        lvInviteListView.adapter = adapterInviteList

        stEmailUser = arguments?.getString("email").toString()

        presetInToFr()

        getFriendsData()

        btnInviteListOpen.setOnClickListener{
            presetFrToIn()
            getInviteData()
        }

        btnFriendListOpen.setOnClickListener {
            presetInToFr()
            getFriendsData()
        }

        btnInviteFriend.setOnClickListener {
            startDialogInviteFriend()
        }

        lvFriendListView.setOnItemLongClickListener { _, _, position, _ ->
            startDialogDeleteFriend(alFriendList[position].stFriendEmail, position)

            return@setOnItemLongClickListener(true)
        }
    }

    private fun recheckAdapter(){
        adapterInviteList.notifyDataSetChanged()
        adapterFriendsList.notifyDataSetChanged()
    }

    private fun getFriendsData(){
        database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).child(resources.getString(R.string.db_friend_list_str)).get().addOnSuccessListener {
            alFriendList.clear()
            if (it.value == null){
                setFriendListInfo(0.toLong(), DataHolder("","","","","","","",""))
            }else {
                it.children.forEach { i ->
                    database.child(getActivity()?.getString(R.string.db_users_str).toString()).child(i.key.toString()).get()
                        .addOnSuccessListener { datatwo ->

                            val tmpProductiveCount =
                                datatwo.child(resources.getString(R.string.db_count_prod_str)).value.toString().toFloat()
                            val tmpInterestCount =
                                datatwo.child(resources.getString(R.string.db_count_inter_str)).value.toString().toFloat()
                            val tmpOrdinaryCount =
                                datatwo.child(resources.getString(R.string.db_count_ordinary_str)).value.toString().toFloat()

                            var percentsOrdinary = 33
                            var percentsInterest = 33
                            var percentsProductive = 33

                            if (tmpProductiveCount != 0F || tmpInterestCount != 0F || tmpOrdinaryCount != 0F) {
                                percentsProductive =
                                    (tmpProductiveCount / (tmpProductiveCount + tmpInterestCount + tmpOrdinaryCount) * 100).toInt()
                                percentsInterest =
                                    (tmpInterestCount / (tmpProductiveCount + tmpInterestCount + tmpOrdinaryCount) * 100).toInt()
                                percentsOrdinary =
                                    (tmpOrdinaryCount / (tmpProductiveCount + tmpInterestCount + tmpOrdinaryCount) * 100).toInt()
                            }
                            setFriendListInfo(
                                it.childrenCount,
                                DataHolder(
                                    i.key.toString(),
                                    datatwo.child(resources.getString(R.string.db_name_str)).value.toString(),
                                    datatwo.child(resources.getString(R.string.db_surname_str)).value.toString(),
                                    datatwo.child(resources.getString(R.string.db_nickname_str)).value.toString(),
                                    datatwo.child(resources.getString(R.string.db_last_notion_str)).value.toString(),
                                    percentsProductive.toString(),
                                    percentsInterest.toString(),
                                    percentsOrdinary.toString()
                                )
                            )
                        }

                }
            }
        }
    }

    private fun getInviteData(){
        database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).child(resources.getString(R.string.db_invite_list_str)).get().addOnSuccessListener { it ->
            alInviteList.clear()
            if(it.value == null){
                setInviteListInfo(0L, inviteListDataHolder("","","","",""))
            }else {
                it.children.forEach { i ->

                    database.child(resources.getString(R.string.db_users_str)).child(i.key.toString()).get()
                        .addOnSuccessListener { datatwo ->
                            val tmpDataObj = inviteListDataHolder(
                                datatwo.child(resources.getString(R.string.db_name_str)).value.toString(),
                                datatwo.child(resources.getString(R.string.db_surname_str)).value.toString(),
                                datatwo.child(resources.getString(R.string.db_nickname_str)).value.toString(),
                                i.key.toString(),
                                stEmailUser
                            )

                            setInviteListInfo(it.childrenCount,tmpDataObj)
                        }
                }
            }
        }

    }

    private fun startDialogInviteFriend(){
        Handler(Looper.getMainLooper()).post {
            val dialogFindFriend = Dialog(requireContext())
            dialogFindFriend.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogFindFriend.setContentView(R.layout.dialog_add_friend)
            dialogFindFriend.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val etEmailUser = dialogFindFriend.findViewById(R.id.add_text_email_field) as EditText
            val dialog_util_confirmation_btn = dialogFindFriend.findViewById(R.id.add_find_btn) as Button
            dialog_util_confirmation_btn.setOnClickListener {
                database.child(resources.getString(R.string.db_users_str)).child(etEmailUser.text.toString().replace(".","")).get().addOnSuccessListener {
                    if(it.value != null) {
                        database.child(resources.getString(R.string.db_users_str)).child(etEmailUser.text.toString().replace(".", ""))
                            .child(resources.getString(R.string.db_invite_list_str)).child(stEmailUser).setValue(1)
                    }
                }
                    dialogFindFriend.cancel()
            }
            dialogFindFriend.setCancelable(true)
            dialogFindFriend.show()
        }
    }

    private fun startDialogDeleteFriend(stEmailFriend : String, position: Int){
        Handler(Looper.getMainLooper()).post {
            val dialogDeleteFriend = Dialog(requireContext())
            dialogDeleteFriend.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogDeleteFriend.setContentView(R.layout.dialog_delete_friend)
            dialogDeleteFriend.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val btnDeleteFriend =
                dialogDeleteFriend.findViewById(R.id.delete_accept_btn) as Button
            val btnDeclineDelete =
                dialogDeleteFriend.findViewById(R.id.delete_decline_btn) as Button

            btnDeclineDelete.setOnClickListener {
                dialogDeleteFriend.cancel()
            }
            btnDeleteFriend.setOnClickListener {
                database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).child(resources.getString(R.string.db_friend_list_str)).child(stEmailFriend)
                    .removeValue()
                database.child(resources.getString(R.string.db_users_str)).child(stEmailFriend).child(resources.getString(R.string.db_friend_list_str)).child(stEmailUser)
                    .removeValue()
                alFriendList.removeAt(position)
                recheckAdapter()
                dialogDeleteFriend.cancel()
            }
            dialogDeleteFriend.setCancelable(true)
            dialogDeleteFriend.show()
        }
    }

    private fun setFriendListInfo(iChildrenCount : Long, tmpData : DataHolder){
        if(iChildrenCount != 0.toLong()) {
            alFriendList.add(tmpData)
            if (alFriendList.size >= iChildrenCount) {
                recheckAdapter()
                changeFromInviteToFriends()
            }
        }else{
            changeFromInviteToFriends()
        }
    }

    private fun setInviteListInfo(iChildrenCount : Long, tmpData : inviteListDataHolder){

        if(iChildrenCount == 0L){
            changeFromFriendsToInvite()
        }
        else {
            alInviteList.add(tmpData)
            if(alInviteList.size.toLong() == iChildrenCount) {
                recheckAdapter()
                changeFromFriendsToInvite()
            }
        }

    }

    private fun changeFromInviteToFriends(){
        lvFriendListView.animate().alpha(1.0f).setDuration(1000L).setListener(null)
        lvInviteListView.animate().alpha(0.0f).setDuration(1000L).withEndAction{ lvInviteListView.visibility = View.INVISIBLE }
        progressbarFriends.animate().alpha(0.0f).setDuration(1000L).withEndAction{ progressbarFriends.visibility = View.GONE }
    }

    private fun presetInToFr(){

        lvFriendListView.visibility = View.VISIBLE
        lvFriendListView.alpha = 0.0f

        lvInviteListView.visibility = View.VISIBLE
        progressbarFriends.visibility = View.VISIBLE

        btnFriendListOpen.setTextAppearance(R.style.CustomTextButtonActive)
        btnInviteListOpen.setTextAppearance(R.style.CustomTextButtonInactive)

    }

    private fun presetFrToIn(){

        lvInviteListView.visibility = View.VISIBLE
        lvInviteListView.alpha= 0.0f

        lvFriendListView.visibility = View.VISIBLE
        progressbarFriends.visibility = View.VISIBLE

        btnFriendListOpen.setTextAppearance(R.style.CustomTextButtonInactive)
        btnInviteListOpen.setTextAppearance(R.style.CustomTextButtonActive)

    }

    private fun changeFromFriendsToInvite(){
        lvFriendListView.animate().alpha(0.0f).setDuration(1000L).withEndAction {
            lvFriendListView.visibility = View.INVISIBLE
        }
        lvInviteListView.animate().alpha(1.0f).setDuration(1000L).setListener(null)
        progressbarFriends.animate().alpha(0.0f).setDuration(1000L).withEndAction {
            progressbarFriends.visibility = View.GONE
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = FragmentCurrentDay()
    }
}