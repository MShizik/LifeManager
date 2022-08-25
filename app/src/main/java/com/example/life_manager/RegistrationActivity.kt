package com.example.life_manager

import android.app.*
import android.content.Context
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
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import java.util.*

class RegistrationActivity : AppCompatActivity() {

    var etEmailUser : EditText? = null
    private var etNicknameUser : EditText? = null
    private var etNameUser : EditText? = null
    private var etSurnameUser : EditText? = null
    private var etPasswordUser : EditText? = null
    private var etPasswordRepeatUser : EditText? = null

    private var tvInfoApp : TextView? = null

    private var btnRegApp : Button? = null
    private var btnPrevReg : AppCompatButton? = null

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
        btnPrevReg = findViewById(R.id.reg_prev_step_btn)

        var iCounterClicks = 0

        var stEmailUser = "default"
        database = Firebase.database.reference

        btnRegApp?.text = resources.getString(R.string.next_btn)

        btnRegApp?.setOnClickListener {
            when (iCounterClicks){
                0->{
                    if(checkEmailAddress()){
                        stEmailUser = etEmailUser?.text.toString().replace(".","")
                        database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).get().addOnSuccessListener {
                            if (it.child(resources.getString(R.string.db_password_str)).value != null) {
                                tvInfoApp?.text =resources.getString(R.string.reg_exist_user_message)
                                tvInfoApp?.setTextColor(resources.getColor(R.color.productive_color_light))
                            }
                            else{
                                btnPrevReg?.visibility = View.VISIBLE
                                database.child(resources.getString(R.string.db_users_str)).child(stEmailUser)
                                    .child(resources.getString(R.string.db_count_prod_str)).setValue(0)
                                database.child(resources.getString(R.string.db_users_str)).child(stEmailUser)
                                    .child(resources.getString(R.string.db_count_ordinary_str)).setValue(0)
                                database.child(resources.getString(R.string.db_users_str)).child(stEmailUser)
                                    .child(resources.getString(R.string.db_count_inter_str)).setValue(0)
                                etEmailUser?.visibility = View.INVISIBLE
                                etEmailUser?.isEnabled = false
                                etNicknameUser?.visibility = View.VISIBLE
                                etNicknameUser?.isEnabled = true
                                tvInfoApp?.text = resources.getString(R.string.nickname_greeting)
                                tvInfoApp?.setTextAppearance(R.style.StyleText)
                                iCounterClicks++
                            }
                        }
                    }
                    else{
                        tvInfoApp?.text = resources.getString(R.string.wrong_email_message)
                        tvInfoApp?.setTextColor(resources.getColor(R.color.productive_color_light))
                    }
                }
                1->{
                    if(etNicknameUser?.text.toString().length >2) {
                        database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).child(resources.getString(R.string.db_nickname_str)).setValue(etNicknameUser?.text.toString())
                        etNicknameUser?.visibility = View.GONE
                        etNicknameUser?.isEnabled = false
                        etNameUser?.visibility = View.VISIBLE
                        etNameUser?.isEnabled = true
                        tvInfoApp?.text = resources.getString(R.string.name_greeting)
                        tvInfoApp?.setTextAppearance(R.style.StyleText)
                        iCounterClicks++
                    }else{
                        tvInfoApp?.text = resources.getString(R.string.nickname_errror_message)
                        tvInfoApp?.setTextColor(resources.getColor(R.color.productive_color_light))
                    }
                }
                2->{
                    if(etNameUser?.text.toString().length >= 2) {
                        database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).child(resources.getString(R.string.db_name_str)).setValue(etNameUser?.text.toString())
                        etNameUser?.visibility = View.GONE
                        etNameUser?.isEnabled = false
                        etSurnameUser?.visibility = View.VISIBLE
                        etSurnameUser?.isEnabled = true
                        tvInfoApp?.text = resources.getString(R.string.surname_greeting)
                        tvInfoApp?.setTextAppearance(R.style.StyleText)
                        iCounterClicks++
                    }else{
                        tvInfoApp?.text = resources.getString(R.string.name_error_message)
                        tvInfoApp?.setTextColor(resources.getColor(R.color.productive_color_light))
                    }
                }
                3->{
                    if(etNameUser?.text.toString().length >= 2) {
                        database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).child(resources.getString(R.string.db_surname_str)).setValue(etSurnameUser?.text.toString())
                        etSurnameUser?.visibility = View.GONE
                        etSurnameUser?.isEnabled = false
                        etPasswordUser?.visibility = View.VISIBLE
                        etPasswordUser?.isEnabled = true
                        etPasswordRepeatUser?.visibility = View.VISIBLE
                        etPasswordRepeatUser?.isEnabled = true
                        btnRegApp?.text = resources.getString(R.string.sign_up_btn)
                        tvInfoApp?.text = resources.getString(R.string.password_greeting)
                        tvInfoApp?.setTextAppearance(R.style.StyleText)
                        btnRegApp?.text = resources.getString(R.string.reg_btn)
                        iCounterClicks++
                    }else{
                        tvInfoApp?.text = resources.getString(R.string.surname_error_message)
                        tvInfoApp?.setTextColor(resources.getColor(R.color.productive_color_light))
                    }
                }
                4->{
                    if(etPasswordUser?.text.toString() == etPasswordRepeatUser?.text.toString()){
                        if(etPasswordUser?.text.toString().length >= 8) {
                            database.child(resources.getString(R.string.db_users_str))
                                .child(stEmailUser)
                                .child(resources.getString(R.string.db_password_str)).setValue(etPasswordUser?.text.toString())

                            val preferencesUserData = getSharedPreferences("user",Context.MODE_PRIVATE)
                            if(!(preferencesUserData.contains("email") and preferencesUserData.contains("password"))){
                                val preferencesEditor = preferencesUserData.edit()
                                preferencesEditor.putString("email",etEmailUser?.text.toString().replace(".",""))
                                preferencesEditor.putString("password",etPasswordUser?.text.toString())
                                preferencesEditor.apply()
                            }
                            var intentToWorkActivity = Intent(this, WorkActivity::class.java)
                            intentToWorkActivity.putExtra("email", stEmailUser)
                            startActivity(intentToWorkActivity)
                        }else{
                            tvInfoApp?.text = resources.getString(R.string.password_error_message)
                            tvInfoApp?.setTextColor(resources.getColor(R.color.productive_color_light))
                        }
                    }else{
                        tvInfoApp?.text = resources.getString(R.string.different_passwords_message)
                        tvInfoApp?.setTextColor(resources.getColor(R.color.productive_color_light))
                    }
                }
            }
        }

        btnPrevReg?.setOnClickListener {
            when (iCounterClicks){
                1->{
                    tvInfoApp?.text = resources.getString(R.string.email_greeting)
                    database.child(resources.getString(R.string.db_users_str)).child(stEmailUser).removeValue()
                    etNicknameUser?.visibility = View.GONE
                    etNicknameUser?.isEnabled = false
                    etEmailUser?.isEnabled = true
                    etEmailUser?.visibility = View.VISIBLE
                    iCounterClicks--
                }
                2->{
                    tvInfoApp?.text = resources.getString(R.string.nickname_greeting)
                    etNicknameUser?.visibility = View.VISIBLE
                    etNameUser?.visibility = View.GONE
                    etNameUser?.isEnabled = false
                    etNicknameUser?.isEnabled = true
                    iCounterClicks--
                }
                3->{
                    tvInfoApp?.text = resources.getString(R.string.name_greeting)
                    etSurnameUser?.visibility = View.GONE
                    etNameUser?.visibility = View.VISIBLE
                    etSurnameUser?.isEnabled = false
                    etNameUser?.isEnabled = true
                    iCounterClicks--
                }
                4->{
                    tvInfoApp?.text = resources.getString(R.string.surname_greeting)
                    etPasswordUser?.visibility = View.GONE
                    etPasswordRepeatUser?.visibility = View.GONE
                    etPasswordRepeatUser?.isEnabled = false
                    etPasswordUser?.isEnabled = false
                    etSurnameUser?.visibility = View.VISIBLE
                    etSurnameUser?.isEnabled = true
                    btnRegApp?.text = resources.getString(R.string.next_btn)
                    iCounterClicks--
                }
            }
        }

    }

    override fun onStop() {
        super.onStop()

    }

    fun checkEmailAddress(): Boolean{
        return !isEmpty(etEmailUser?.text.toString().replace(".","")) && android.util.Patterns.EMAIL_ADDRESS.matcher(etEmailUser?.text.toString()).matches()
    }
}