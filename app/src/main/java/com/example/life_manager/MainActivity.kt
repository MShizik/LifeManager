package com.example.life_manager

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*


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


        btnSignUser?.setOnClickListener {
            database.child(resources.getString(R.string.db_users_str)).child(etEmailUser?.text.toString().replace(".", "")).get()
                .addOnSuccessListener {
                    if (it.value != null) {
                        if (it.child(resources.getString(R.string.db_password_str)).value.toString() == etPasswordUser?.text.toString()) {
                            stEmailUser = etEmailUser?.text.toString().replace(".","")

                            var intentToWorkActivity = Intent(this, WorkActivity::class.java)
                            intentToWorkActivity.putExtra("email", etEmailUser?.text.toString())
                            startActivity(intentToWorkActivity)
                        } else {
                            tvInfoApp?.text = resources.getString(R.string.wrong_password_message)
                            tvInfoApp?.setTextColor(resources.getColor(R.color.productive_color_light))
                        }
                    } else {
                        tvInfoApp?.text = resources.getString(R.string.unknown_user_message)
                        tvInfoApp?.setTextColor(resources.getColor(R.color.productive_color_light))
                    }
                }
                .addOnFailureListener {
                    tvInfoApp?.text = resources.getString(R.string.internet_connection_error_message)
                    tvInfoApp?.setTextColor(resources.getColor(R.color.productive_color_light))
                }
        }
    }
}

