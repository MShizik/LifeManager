package com.example.life_manager


import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.FieldPosition
import java.util.concurrent.TimeUnit


class FragmentFriends : Fragment() {

    var stEmailUser : String = "aboba"

    private var alFriendList : ArrayList<DataHolder> = arrayListOf()
    private var alInviteList : ArrayList<inviteListDataHolder> = arrayListOf()

    private lateinit var adapterFriendsList : DataFriendAdapter
    private lateinit var adapterInviteList : DataInviteAdapter

    private lateinit var lvFriendListView : ListView
    private lateinit var lvInviteListView : ListView

    private lateinit var prbarFriends : ConstraintLayout

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

        lvFriendListView = view.findViewById(R.id.friend_list_view)
        lvInviteListView = view.findViewById(R.id.friend_invite_list_view)

        prbarFriends = view.findViewById(R.id.progress_layout) as ConstraintLayout
        prbarFriends.visibility = View.VISIBLE

        adapterFriendsList = DataFriendAdapter(requireContext(), alFriendList)
        adapterInviteList = DataInviteAdapter(requireContext(), alInviteList)

        lvFriendListView.adapter = adapterFriendsList
        lvInviteListView.adapter = adapterInviteList

        stEmailUser = arguments?.getString("email").toString()

        presetIntoFr()

        getFriendsData(object : callbackFriendList{
            override fun onCallback(iChildrenCount : Long, tmpData : DataHolder) {
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
        })

        btnInviteListOpen.setOnClickListener{
            presetFrtoIn()
            btnFriendListOpen.setTextAppearance(R.style.CustomTextButtonInactive)
            btnInviteListOpen.setTextAppearance(R.style.CustomTextButtonActive)
            getInviteData(object : callbackInviteList{
                override fun onCallback(iChildrenCount: Long, tmpData: inviteListDataHolder) {
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
            })
        }

        btnFriendListOpen.setOnClickListener {
            presetIntoFr()
            btnFriendListOpen.setTextAppearance(R.style.CustomTextButtonActive)
            btnInviteListOpen.setTextAppearance(R.style.CustomTextButtonInactive)
            getFriendsData(object : callbackFriendList{
                override fun onCallback(iChildrenCount : Long, tmpData : DataHolder) {
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
            })
        }

        btnInviteFriend.setOnClickListener {
            startDialogInviteFriend()
        }

        lvFriendListView.setOnItemLongClickListener { parent, view, position, id ->
            startDialogDeleteFriend(alFriendList[position].stFriendEmail, position)

            return@setOnItemLongClickListener(true)
        }
    }

    fun recheckAdapter(){
        adapterInviteList.notifyDataSetChanged()
        adapterFriendsList.notifyDataSetChanged()
    }

    fun getFriendsData(callbackFriendList: callbackFriendList){
        database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).child(resources.getString(R.string.db_friend_list_str)).get().addOnSuccessListener {
            alFriendList.clear()
            System.out.println("w're here")
            if (it.value == null){
                callbackFriendList.onCallback(0.toLong(), DataHolder("","","","","","","",""))
            }else {
                it.children.forEach { i ->
                    var tmpNameFriend: String = ""
                    var tmpSurNameFriend: String = ""
                    var tmpNickNameFriend: String = ""
                    var tmpLastNotion: String = ""

                    var tmpProductiveCount: Int = 0
                    var tmpInterestCount: Int = 0
                    var tmpOrdinaryCount: Int = 0
                    var percentesProductive: Int = 0
                    var percentesInterest: Int = 0
                    var percentesOrdinary: Int = 0

                    database.child(getActivity()?.getString(R.string.db_users_str).toString()).child(i.key.toString()).get()
                        .addOnSuccessListener { datatwo ->
                            tmpNameFriend = datatwo.child(resources.getString(R.string.db_name_str)).value.toString()
                            tmpSurNameFriend = datatwo.child(resources.getString(R.string.db_surname_str)).value.toString()
                            tmpNickNameFriend = datatwo.child(resources.getString(R.string.db_nickname_str)).value.toString()
                            tmpLastNotion = datatwo.child(resources.getString(R.string.db_last_notion_str)).value.toString()

                            tmpProductiveCount =
                                datatwo.child(resources.getString(R.string.db_count_prod_str)).value.toString().toInt()
                            tmpInterestCount =
                                datatwo.child(resources.getString(R.string.db_count_inter_str)).value.toString().toInt()
                            tmpOrdinaryCount =
                                datatwo.child(resources.getString(R.string.db_count_ordinary_str)).value.toString().toInt()
                            if (tmpProductiveCount == 0 && tmpInterestCount == 0 && tmpOrdinaryCount == 0) {
                                percentesProductive = 33
                                percentesInterest = 33
                                percentesOrdinary = 33
                            } else {
                                percentesProductive =
                                    tmpProductiveCount / (tmpProductiveCount + tmpInterestCount + tmpOrdinaryCount) * 100
                                percentesInterest =
                                    tmpInterestCount / (tmpProductiveCount + tmpInterestCount + tmpOrdinaryCount) * 100
                                percentesOrdinary =
                                    tmpInterestCount / (tmpProductiveCount + tmpInterestCount + tmpOrdinaryCount) * 100
                            }
                            callbackFriendList.onCallback(
                                it.childrenCount.toLong(),
                                DataHolder(
                                    i.key.toString(),
                                    tmpNameFriend,
                                    tmpSurNameFriend,
                                    tmpNickNameFriend,
                                    tmpLastNotion,
                                    percentesProductive.toString(),
                                    percentesInterest.toString(),
                                    percentesOrdinary.toString()
                                )
                            )
                        }

                }
            }
        }

        println("theFirstStep" + alFriendList.toString())

    }

    fun getInviteData(callbackInviteList: callbackInviteList){
        database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).child(resources.getString(R.string.db_invite_list_str)).get().addOnSuccessListener { it ->
            alInviteList.clear()
            if(it.value == null){
                callbackInviteList.onCallback(0L, inviteListDataHolder("","","","",""))
            }else {
                it.children.forEach { i ->
                    var tmpNameFriend: String = ""
                    var tmpSurNameFriend: String = ""
                    var tmpNickNameFriend: String = ""

                    database.child(resources.getString(R.string.db_users_str)).child(i.key.toString()).get()
                        .addOnSuccessListener { datatwo ->
                            System.out.println(datatwo.toString() + "data")
                            tmpNameFriend = datatwo.child(resources.getString(R.string.db_name_str)).value.toString()
                            tmpSurNameFriend = datatwo.child(resources.getString(R.string.db_surname_str)).value.toString()
                            tmpNickNameFriend = datatwo.child(resources.getString(R.string.db_nickname_str)).value.toString()
                            var tmpDataObj: inviteListDataHolder = inviteListDataHolder(
                                tmpNameFriend,
                                tmpSurNameFriend,
                                tmpNickNameFriend,
                                i.key.toString(),
                                stEmailUser
                            )

                            callbackInviteList.onCallback(it.childrenCount,tmpDataObj)
                        }
                }
            }
        }

    }

    fun startDialogInviteFriend(){
        Handler(Looper.getMainLooper()).post {
            val dialog_find_friend = Dialog(requireContext())
            dialog_find_friend.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog_find_friend.setContentView(R.layout.dialog_add_friend)
            dialog_find_friend.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val etEmailUser = dialog_find_friend.findViewById(R.id.add_text_email_field) as EditText
            val dialog_util_confirmation_btn = dialog_find_friend.findViewById(R.id.add_find_btn) as Button
            dialog_util_confirmation_btn.setOnClickListener {
                database.child(resources.getString(R.string.db_users_str)).child(etEmailUser.text.toString().replace(".","")).get().addOnSuccessListener {
                    if(it.value != null) {
                        database.child(resources.getString(R.string.db_users_str)).child(etEmailUser.text.toString().replace(".", ""))
                            .child(resources.getString(R.string.db_invite_list_str)).child(stEmailUser).setValue(1)
                    }
                }
                    dialog_find_friend.cancel()
            }
            dialog_find_friend.setCancelable(true)
            dialog_find_friend.show()
        }
    }

    fun startDialogDeleteFriend(stEmailFriend : String, position: Int){
        Handler(Looper.getMainLooper()).post {
            val dialog_delete_friend = Dialog(requireContext())
            dialog_delete_friend.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog_delete_friend.setContentView(R.layout.dialog_delete_friend)
            dialog_delete_friend.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val btnDeleteFriend =
                dialog_delete_friend.findViewById(R.id.delete_accept_btn) as Button
            val btnDeclineDelete =
                dialog_delete_friend.findViewById(R.id.delete_decline_btn) as Button

            btnDeclineDelete.setOnClickListener {
                dialog_delete_friend.cancel()
            }
            btnDeleteFriend.setOnClickListener {
                database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).child(resources.getString(R.string.db_friend_list_str)).child(stEmailFriend)
                    .removeValue()
                database.child(resources.getString(R.string.db_users_str)).child(stEmailFriend).child(resources.getString(R.string.db_friend_list_str)).child(stEmailUser)
                    .removeValue()
                alFriendList.removeAt(position)
                recheckAdapter()
                dialog_delete_friend.cancel()
            }
            dialog_delete_friend.setCancelable(true)
            dialog_delete_friend.show()
        }
    }

    interface callbackFriendList{
        fun onCallback(iChildrenCount : Long, tmpData : DataHolder)
    }

    interface callbackInviteList{
        fun onCallback(iChildrenCount : Long, tmpData : inviteListDataHolder)
    }

    fun changeFromInviteToFriends(){
        lvFriendListView.animate().alpha(1.0f).setDuration(1000L).setListener(null)
        lvInviteListView.animate().alpha(0.0f).setDuration(1000L).withEndAction(Runnable { lvInviteListView.visibility = View.INVISIBLE })
        prbarFriends.animate().alpha(0.0f).setDuration(1000L).withEndAction(Runnable { prbarFriends.visibility = View.GONE })
    }

    fun presetIntoFr(){
        lvFriendListView.visibility = View.VISIBLE
        lvFriendListView.alpha = 0.0f
        lvInviteListView.visibility = View.VISIBLE
        prbarFriends.visibility = View.VISIBLE
    }

    fun presetFrtoIn(){
        lvInviteListView.visibility = View.VISIBLE
        lvInviteListView.alpha= 0.0f
        lvFriendListView.visibility = View.VISIBLE
        prbarFriends.visibility = View.VISIBLE
    }

    fun changeFromFriendsToInvite(){
        lvFriendListView.animate().alpha(0.0f).setDuration(1000L).withEndAction(Runnable { lvFriendListView.visibility = View.INVISIBLE })
        lvInviteListView.animate().alpha(1.0f).setDuration(1000L).setListener(null)
        prbarFriends.animate().alpha(0.0f).setDuration(1000L).withEndAction(Runnable { prbarFriends.visibility = View.GONE })
    }


    companion object {
        @JvmStatic
        fun newInstance() = FragmentCurrentDay()
    }
}