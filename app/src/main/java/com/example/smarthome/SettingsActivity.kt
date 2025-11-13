package com.example.smarthome

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class SettingsActivity : AppCompatActivity() {
    private lateinit var swNightMode: SwitchMaterial
    private lateinit var swNotifications: SwitchMaterial
    private lateinit var tvCurrentLanguage: TextView
    private lateinit var auth: FirebaseAuth

    // Поддерживаемые языки (языковой код → отображаемое имя)
    private val languages = mapOf(
        "ru" to "Русский",
        "en" to "English",
        "es" to "Español",
        "fr" to "Français",
        "de" to "Deutsch",
        "zh" to "中文",
        "ar" to "العربية"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        auth = FirebaseAuth.getInstance()
        swNightMode = findViewById(R.id.swNightMode)
        swNotifications = findViewById(R.id.swNotifications)
        tvCurrentLanguage = findViewById(R.id.tvCurrentLanguage)

        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)

        // Загрузка настроек
        val isDarkMode = prefs.getBoolean("dark_mode", false)
        val areNotificationsEnabled = prefs.getBoolean("notifications", true)
        val currentLang = prefs.getString("language", "ru") ?: "ru"

        swNightMode.isChecked = isDarkMode
        swNotifications.isChecked = areNotificationsEnabled
        tvCurrentLanguage.text = languages[currentLang] ?: "Русский"

        // Применить тему
        if (isDarkMode) {
            setDarkTheme()
        } else {
            setLightTheme()
        }

        // Переключение темы
        swNightMode.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("dark_mode", isChecked).apply()
            if (isChecked) setDarkTheme() else setLightTheme()
        }

        // Уведомления
        swNotifications.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notifications", isChecked).apply()
        }

        // Выбор языка
        findViewById<LinearLayout>(R.id.layoutLanguage).setOnClickListener {
            showLanguageDialog(prefs)
        }

        // Кнопка "назад"
        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }

        // Кнопка "выйти"
        findViewById<ImageView>(R.id.ivLogout).setOnClickListener {
            showLogoutConfirmation()
        }
    }

    private fun showLanguageDialog(prefs: android.content.SharedPreferences) {
        val items = languages.values.toTypedArray()
        val currentLangCode = prefs.getString("language", "ru") ?: "ru"
        val currentIndex = languages.keys.indexOf(currentLangCode)

        AlertDialog.Builder(this)
            .setTitle("Выберите язык")
            .setSingleChoiceItems(items, currentIndex) { _, which ->
                val selectedLang = languages.keys.elementAt(which)
                prefs.edit().putString("language", selectedLang).apply()
                setAppLanguage(selectedLang)
                tvCurrentLanguage.text = items[which]
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun setAppLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            config.setLocales(LocaleList(locale))
        } else {
            config.locale = locale
        }

        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        // Перезапуск активности для применения языка
        recreate()
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Выход из аккаунта")
            .setMessage("Вы уверены, что хотите выйти?")
            .setPositiveButton("Да") { _, _ ->
                auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                finish()
            }
            .setNegativeButton("Нет", null)
            .show()
    }

    private fun setDarkTheme() {
        delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        window.decorView.setBackgroundColor(getColorCompat(R.color.dark_background))
    }

    private fun setLightTheme() {
        delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.decorView.setBackgroundColor(getColorCompat(R.color.white))
    }

    // Совместимость с Android < 10
    private fun getColorCompat(resId: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources.getColor(resId, theme)
        } else {
            resources.getColor(resId)
        }
    }
}