package com.example.memshakbait

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.sidzadaun.liquidglass.LiquidGlassView

class RegisterActivity : BaseActivity() {

    private lateinit var register_GLASS_photo: LiquidGlassView
    private lateinit var register_BTN_photo: MaterialButton

    private lateinit var register_GLASS_name: LiquidGlassView
    private lateinit var register_EDIT_name: AppCompatEditText
    private lateinit var register_GLASS_mail: LiquidGlassView
    private lateinit var register_EDIT_mail: AppCompatEditText
    private lateinit var register_GLASS_password: LiquidGlassView
    private lateinit var register_EDIT_password: AppCompatEditText
    private lateinit var register_GLASS_password_conf: LiquidGlassView
    private lateinit var register_EDIT_password_conf: AppCompatEditText

    private lateinit var register_GLASS_gender_male: LiquidGlassView
    private lateinit var register_BTN_gender_male: MaterialButton
    private lateinit var register_GLASS_gender_female: LiquidGlassView
    private lateinit var register_BTN_gender_female: MaterialButton
    private lateinit var register_GLASS_gender_other: LiquidGlassView
    private lateinit var register_BTN_gender_other: MaterialButton

    private lateinit var register_BTN_submit: MaterialButton

    private lateinit var register_BTN_login: MaterialButton

    private lateinit var genderButtons: Array<MaterialButton>

    private var selectedGender: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        initViews()
    }
    private fun findViews() {
        register_GLASS_photo = findViewById(R.id.register_GLASS_photo)
        register_BTN_photo = findViewById(R.id.register_BTN_photo)

        register_GLASS_name = findViewById(R.id.register_GLASS_name)
        register_EDIT_name = findViewById(R.id.register_EDIT_name)
        register_GLASS_mail = findViewById(R.id.register_GLASS_mail)
        register_EDIT_mail = findViewById(R.id.register_EDIT_mail)
        register_GLASS_password = findViewById(R.id.register_GLASS_password)
        register_EDIT_password = findViewById(R.id.register_EDIT_password)
        register_GLASS_password_conf = findViewById(R.id.register_GLASS_password_conf)
        register_EDIT_password_conf = findViewById(R.id.register_EDIT_password_conf)

        register_GLASS_gender_male = findViewById(R.id.register_GLASS_gender_male)
        register_BTN_gender_male = findViewById(R.id.register_BTN_gender_male)
        register_GLASS_gender_female = findViewById(R.id.register_GLASS_gender_female)
        register_BTN_gender_female = findViewById(R.id.register_BTN_gender_female)
        register_GLASS_gender_other = findViewById(R.id.register_GLASS_gender_other)
        register_BTN_gender_other = findViewById(R.id.register_BTN_gender_other)

        genderButtons = arrayOf(register_BTN_gender_male, register_BTN_gender_female, register_BTN_gender_other)

        register_BTN_submit = findViewById(R.id.register_BTN_submit)
        register_BTN_login = findViewById(R.id.register_BTN_login)
    }

    private fun initViews() {
        register_GLASS_photo.setupWithActivityRoot()
        register_GLASS_name.setupWithActivityRoot()
        register_GLASS_mail.setupWithActivityRoot()
        register_GLASS_password.setupWithActivityRoot()
        register_GLASS_password_conf.setupWithActivityRoot()
        register_GLASS_gender_male.setupWithActivityRoot()
        register_GLASS_gender_female.setupWithActivityRoot()
        register_GLASS_gender_other.setupWithActivityRoot()

        register_BTN_gender_male.setOnClickListener { selectGender(0) }
        register_BTN_gender_female.setOnClickListener { selectGender(1) }
        register_BTN_gender_other.setOnClickListener { selectGender(2) }

        register_BTN_submit.setOnClickListener {  }
        register_BTN_login.setOnClickListener { changeActivityToLogin() }

        selectGender(0)
    }

    fun selectGender(gender: Int) {
        selectedGender = gender
        for (i in 0..2) {
            if (i != selectedGender) {
                genderButtons[i].backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.liquid_glass_inner_20)
                )
                genderButtons[i].setTextColor(ContextCompat.getColor(this, R.color.white))
            }
            else {
                genderButtons[i].backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.white)
                )
                genderButtons[i].setTextColor(ContextCompat.getColor(this, R.color.turquoise))
            }
        }
    }

    private fun changeActivityToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}