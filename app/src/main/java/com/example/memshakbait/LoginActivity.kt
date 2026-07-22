package com.example.memshakbait

import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.sidzadaun.liquidglass.LiquidGlassView

class LoginActivity : BaseActivity() {
    private lateinit var login_IMG_logo: AppCompatImageView
    private lateinit var login_IMG_logo_shadow: AppCompatImageView

    private lateinit var login_GLASS_mail: LiquidGlassView
    private lateinit var login_EDIT_mail: AppCompatEditText
    private lateinit var login_GLASS_password: LiquidGlassView
    private lateinit var login_EDIT_password: AppCompatEditText

    private lateinit var login_BTN_submit: MaterialButton
    private lateinit var login_BTN_register: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        initViews()
    }

    private fun findViews() {
        login_IMG_logo = findViewById(R.id.login_IMG_logo)
        login_IMG_logo_shadow = findViewById(R.id.login_IMG_logo_shadow)

        login_GLASS_mail = findViewById(R.id.login_GLASS_mail)
        login_EDIT_mail = findViewById(R.id.login_EDIT_mail)
        login_GLASS_password = findViewById(R.id.login_GLASS_password)
        login_EDIT_password = findViewById(R.id.login_EDIT_password)

        login_BTN_submit = findViewById(R.id.login_BTN_submit)
        login_BTN_register = findViewById(R.id.login_BTN_register)
    }

    private fun initViews() {
        login_IMG_logo_shadow.setColorFilter(android.graphics.Color.BLACK)
        login_IMG_logo_shadow.setRenderEffect(RenderEffect.createBlurEffect(12f, 12f, Shader.TileMode.CLAMP))

        login_GLASS_mail.setupWithActivityRoot()
        login_GLASS_password.setupWithActivityRoot()

        login_BTN_submit.setOnClickListener {  }
        login_BTN_register.setOnClickListener { changeActivityToRegister() }
    }

    private fun changeActivityToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
}