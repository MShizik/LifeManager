package com.example.life_manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var etEmailUser: EditText? = null
    private var etPasswordUser: EditText? = null
    private var tvInfoApp: TextView? = null

    private var btnSignUser: Button? = null
    private var btnRegUser: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etEmailUser = findViewById(R.id.main_text_email_field)
        etPasswordUser = findViewById(R.id.main_text_password_field)
        tvInfoApp = findViewById(R.id.main_text_greeting)

        btnSignUser = findViewById(R.id.main_sign_btn)
        btnRegUser = findViewById(R.id.main_reg_btn)



    }
}

