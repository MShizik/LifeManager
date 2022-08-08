package com.example.life_manager

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.isEmpty
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

        btnRegApp?.setOnClickListener {
            if(checkEmailAddress()){
                if(etPasswordUser?.text.toString().equals(etPasswordRepeatUser?.text.toString())){
                    database = Firebase.database.reference
                    database.child("users").child((etEmailUser?.text.toString())).get().addOnSuccessListener {
                        tvInfoApp?.text = "User with this email already exist"
                        tvInfoApp?.setTextColor(Color.parseColor("#f0989f"))
                    }
                        .addOnFailureListener {
                            database.child("users").child(etEmailUser?.text.toString())
                                .child("countProductive").setValue(0)
                            database.child("users").child(etEmailUser?.text.toString())
                                .child("countOrdinary").setValue(0)
                            database.child("users").child(etEmailUser?.text.toString())
                                .child("countInterest").setValue(0)
                            database.child("users").child(etEmailUser?.text.toString())
                                .child("password").setValue(etPasswordUser?.text.toString())
                            database.child("users").child(etEmailUser?.text.toString())
                                .child("name").setValue(etNameUser?.text.toString())
                            database.child("users").child(etEmailUser?.text.toString())
                                .child("surname").setValue(etSurnameUser?.text.toString())
                            database.child("users").child(etEmailUser?.text.toString())
                                .child("nickname").setValue(etNicknameUser?.text.toString())
                            database.child("users").child(etEmailUser?.text.toString())
                                .child("invitelist").child("base").setValue("basement")
                            database.child("users").child(etEmailUser?.text.toString())
                                .child("friends").child("base").setValue("basement")
                            database.child("users").child(etEmailUser?.text.toString())
                                .child("lastnotion").setValue("lastNotion!!!")
                            var intentToWorkActivity = Intent(applicationContext, WorkActivity::class.java)
                            intentToWorkActivity.putExtra("email",etEmailUser?.text.toString())
                            startActivity(intentToWorkActivity)
                        }

                }
                else{
                    tvInfoApp?.text = "Your passwords are different"
                    tvInfoApp?.setTextColor(Color.parseColor("#f0989f"))
                }
            }
            else{
                tvInfoApp?.text = "Wrong email, try again"
                tvInfoApp?.setTextColor(Color.parseColor("#f0989f"))
            }
        }
    }

    fun checkEmailAddress(): Boolean{
        return !isEmpty(etEmailUser?.text.toString()) && android.util.Patterns.EMAIL_ADDRESS.matcher(etEmailUser?.text.toString()).matches()
    }
}