package com.example.life_manager

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class RegistrationActivity : AppCompatActivity() {

    var etEmailUser : EditText? = null
    private var etNicknameUser : EditText? = null
    private var etNameUser : EditText? = null
    private var etSurnameUser : EditText? = null
    private var etPasswordUser : EditText? = null
    private var etPasswordRepeatUser : EditText? = null

    private var tvInfoApp : TextView? = null

    private var btnRegApp : Button? = null

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        etEmailUser = findViewById(R.id.reg_text_email_field)
        etNicknameUser = findViewById(R.id.reg_text_nickname_field)
        etNameUser = findViewById(R.id.reg_text_name_field)
        etSurnameUser = findViewById(R.id.reg_text_surname_field)
        etPasswordUser = findViewById(R.id.reg_text_password_field)
        etPasswordRepeatUser = findViewById(R.id.reg_text_password_repeat_field)

        tvInfoApp = findViewById(R.id.reg_text_greeting)

        btnRegApp = findViewById(R.id.reg_registration_btn)

        var iCounterClicks : Int = 0

        var stEmailUser = "aboba"
        database = Firebase.database.reference

        btnRegApp?.setOnClickListener {
            when (iCounterClicks){
                0->{
                    if(!checkEmailAddress()){
                        stEmailUser = etEmailUser?.text.toString().replace(".","")
                        database.child("users").child(stEmailUser).get().addOnSuccessListener {
                            if (it.child("password").value != null) {
                                tvInfoApp?.text = "User with this email already exist"
                                tvInfoApp?.setTextColor(Color.parseColor("#f0989f"))
                            }
                            else{
                                database.child("users").child(stEmailUser)
                                    .child("countProductive").setValue(0)
                                database.child("users").child(stEmailUser)
                                    .child("countOrdinary").setValue(0)
                                database.child("users").child(stEmailUser)
                                    .child("countInterest").setValue(0)
                                database.child("users").child(stEmailUser)
                                    .child("invitelist").child("base").setValue("basement")
                                database.child("users").child(stEmailUser)
                                    .child("friends").child("base").setValue("basement")
                                database.child("users").child(stEmailUser)
                                    .child("lastnotion").setValue("lastNotion!!!")
                                etEmailUser?.visibility = View.INVISIBLE
                                etNicknameUser?.visibility = View.VISIBLE
                                tvInfoApp?.text = "Become more productive"
                                tvInfoApp?.setTextColor(Color.parseColor("#FF000000"))
                                iCounterClicks++
                            }
                        }
                    }
                    else{
                        tvInfoApp?.text = "Wrong email, try again"
                        tvInfoApp?.setTextColor(Color.parseColor("#f0989f"))
                    }
                }
                1->{
                    if(etNicknameUser?.text.toString().length >2) {
                        database.child("users").child(stEmailUser).child("nickname").setValue(etNicknameUser?.text.toString())
                        etNicknameUser?.visibility = View.INVISIBLE
                        etNameUser?.visibility = View.VISIBLE
                        tvInfoApp?.text = "Become more productive"
                        tvInfoApp?.setTextColor(Color.parseColor("#FF000000"))
                        iCounterClicks++
                    }else{
                        tvInfoApp?.text = "Nickname should be at least 2 symbols"
                        tvInfoApp?.setTextColor(Color.parseColor("#f0989f"))
                    }
                }
                2->{
                    if(etNameUser?.text.toString().length >= 2) {
                        database.child("users").child(stEmailUser).child("name").setValue(etNameUser?.text.toString())
                        etNameUser?.visibility = View.INVISIBLE
                        etSurnameUser?.visibility = View.VISIBLE
                        tvInfoApp?.text = "Become more productive"
                        tvInfoApp?.setTextColor(Color.parseColor("#FF000000"))
                        iCounterClicks++
                    }else{
                        tvInfoApp?.text = "Name should be at least 2 symbols"
                        tvInfoApp?.setTextColor(Color.parseColor("#f0989f"))
                    }
                }
                3->{
                    if(etNameUser?.text.toString().length >= 2) {
                        database.child("users").child(stEmailUser).child("surname").setValue(etSurnameUser?.text.toString())

                        etSurnameUser?.visibility = View.INVISIBLE
                        etPasswordUser?.visibility = View.VISIBLE
                        etPasswordRepeatUser?.visibility = View.VISIBLE
                        btnRegApp?.text = "Registration"
                        tvInfoApp?.text = "Become more productive"
                        tvInfoApp?.setTextColor(Color.parseColor("#FF000000"))
                        iCounterClicks++
                    }else{
                        tvInfoApp?.text = "Surname should be at least 2 symbols"
                        tvInfoApp?.setTextColor(Color.parseColor("#f0989f"))
                    }
                }
                4->{
                    if(etPasswordUser?.text.toString() == etPasswordRepeatUser?.text.toString()){
                        if(etPasswordUser?.text.toString().length >= 8) {
                            database.child("users")
                                .child(etEmailUser?.text.toString().replace(".", ""))
                                .child("password").setValue(etPasswordUser?.text.toString())
                            var intentToWorkActivity = Intent(this, WorkActivity::class.java)
                            intentToWorkActivity.putExtra("email", stEmailUser)
                            startActivity(intentToWorkActivity)
                        }else{
                            tvInfoApp?.text = "Password should be at least 8 symbols"
                            tvInfoApp?.setTextColor(Color.parseColor("#f0989f"))
                        }
                    }else{
                        tvInfoApp?.text = "Your passwords are different"
                        tvInfoApp?.setTextColor(Color.parseColor("#f0989f"))
                    }
                }
            }
        }
    }

    fun checkEmailAddress(): Boolean{
        return !isEmpty(etEmailUser?.text.toString().replace(".","")) && android.util.Patterns.EMAIL_ADDRESS.matcher(etEmailUser?.text.toString().replace(".","")).matches()
    }
}