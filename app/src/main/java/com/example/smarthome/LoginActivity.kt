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
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var tvCreateAccount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Инициализация полей
        auth = FirebaseAuth.getInstance()

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvCreateAccount = findViewById(R.id.tvCreateAccount)

        // Обработка клика по кнопке входа
        btnLogin.setOnClickListener {
            signIn()
        }

        // 👇 Переход на экран регистрации
        tvCreateAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            // finish()
        }
    }

    private fun signIn() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (!validate(email, password)) return

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Успешный вход
                    Toast.makeText(this, "Добро пожаловать!", Toast.LENGTH_SHORT).show()
                    // Здесь можно перейти на HomeActivity
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Неверный email или пароль", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validate(email: String, password: String): Boolean {
        return when {
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
            else -> true
        }
    }
}