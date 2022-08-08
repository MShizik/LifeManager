package com.example.life_manager

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private var etEmailUser: EditText? = null
    private var etPasswordUser: EditText? = null
    private var tvInfoApp: TextView? = null

    private var btnSignUser: Button? = null
    private var btnRegUser: Button? = null

    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = Firebase.database.reference

        etEmailUser = findViewById(R.id.main_text_email_field)
        etPasswordUser = findViewById(R.id.main_text_password_field)
        tvInfoApp = findViewById(R.id.main_text_greeting)

        btnSignUser = findViewById(R.id.main_sign_btn)
        btnRegUser = findViewById(R.id.main_reg_btn)

        btnRegUser?.setOnClickListener {
            val regIntent = Intent(this, RegistrationActivity::class.java)
            startActivity(regIntent)
        }


        var stEmailUser: String? = null
        var stNameUser: String? = null
        var stSurnameUser: String? = null
        var stNicknameUser: String? = null

        btnSignUser?.setOnClickListener {
            database.child("users").child(etEmailUser?.text.toString()).get().addOnSuccessListener {
                if(it.child("password").value.toString().equals(etPasswordUser?.text.toString())){
                    stEmailUser = etEmailUser?.text.toString()
                    stNameUser = it.child("name").value.toString()
                    stSurnameUser = it.child("surname").value.toString()
                    stNicknameUser = it.child("nickname").value.toString()
                    var intentToWorkActivity = Intent(this, WorkActivity::class.java)
                    intentToWorkActivity.putExtra("email", etEmailUser?.text.toString())
                    startActivity(intentToWorkActivity)
                }
                else{
                    tvInfoApp?.text = "Wrong password"
                    tvInfoApp?.setTextColor(Color.parseColor("#f0989f"))
                }
            }.addOnFailureListener{
                    tvInfoApp?.text = "Unknown user"
                    tvInfoApp?.setTextColor(Color.parseColor("#f0989f"))
            }
        }



    }
}

