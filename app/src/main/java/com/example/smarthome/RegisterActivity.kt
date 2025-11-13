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
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var btnRegister: Button
    private lateinit var tvLoginInstead: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvLoginInstead = findViewById(R.id.tvLoginInstead)

        btnRegister.setOnClickListener {
                createAccount()
        }

        findViewById<TextView>(R.id.tvLoginInstead).setOnClickListener {
            finish() // вернуться на экран входа
        }

        val tvLoginInstead = findViewById<TextView>(R.id.tvLoginInstead)
        tvLoginInstead.setOnClickListener {
            //val intent = Intent(this, LoginActivity::class.java)
            //startActivity(intent)
            finish()
        }
    }

    private fun createAccount() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        if (!validate(email, password, confirmPassword)) return

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Успешная регистрация
                    Toast.makeText(this, "Аккаунт создан!", Toast.LENGTH_SHORT).show()
                    finish() // возврат на логин
                } else {
                    // Ошибка
                    Toast.makeText(this, "Ошибка: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validate(email: String, password: String, confirmPassword: String): Boolean {
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