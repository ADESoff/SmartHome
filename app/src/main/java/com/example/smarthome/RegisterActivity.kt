package com.example.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {
    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var btnRegister: Button
    private lateinit var tvLoginInstead: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvLoginInstead = findViewById(R.id.tvLoginInstead)

        btnRegister.setOnClickListener {
            if (validate()) {
                // Регистрация
                Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
            }
        }
        val tvLoginInstead = findViewById<TextView>(R.id.tvLoginInstead)
        tvLoginInstead.setOnClickListener {
            //val intent = Intent(this, LoginActivity::class.java)
            //startActivity(intent)
            finish()
        }
    }



    private fun validate(): Boolean {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        return when {
            name.isEmpty() -> {
                etName.error = "Введите имя"
                false
            }
            email.isEmpty() -> {
                etEmail.error = "Введите email"
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                etEmail.error = "Неверный формат email"
                false
            }
            password.isEmpty() -> {
                etPassword.error = "Введите пароль"
                false
            }
            password.length < 6 -> {
                etPassword.error = "Пароль должен быть не короче 6 символов"
                false
            }
            confirmPassword.isEmpty() -> {
                etConfirmPassword.error = "Подтвердите пароль"
                false
            }
            password != confirmPassword -> {
                etConfirmPassword.error = "Пароли не совпадают"
                false
            }
            else -> true
        }
    }
}