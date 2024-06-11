package com.example.pr18
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import last
import main3

class MainActivity2 : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        sharedPreferences = getSharedPreferences("CurrencyDiaryPrefs", MODE_PRIVATE)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Введите имя пользователя и пароль", Toast.LENGTH_SHORT).show()
            } else {
                checkUser(username, password)
            }
        }
    }

    private fun checkUser(username: String, password: String) {
        val usersJson = sharedPreferences.getString("users", "")
        val usersType = object : TypeToken<HashMap<String, String>>() {}.type
        val users: HashMap<String, String> = Gson().fromJson(usersJson, usersType) ?: hashMapOf()

        if (users.containsKey(username)) {
            if (users[username] == password) {
                navigateToMainActivity()
            } else {
                Toast.makeText(this, "Неверный пароль", Toast.LENGTH_SHORT).show()
            }
        } else {
            showRegistrationDialog(username, password)
        }
    }

    private fun showRegistrationDialog(username: String, password: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Регистрация")
        alertDialog.setMessage("Пользователь не найден. Зарегистрироваться?")
        alertDialog.setPositiveButton("Да") { dialog, which ->
            registerUser(username, password)
            navigateToMainActivity()
        }
        alertDialog.setNegativeButton("Нет", null)
        alertDialog.show()
    }

    private fun registerUser(username: String, password: String) {
        val usersJson = sharedPreferences.getString("users", "")
        val usersType = object : TypeToken<HashMap<String, String>>() {}.type
        val users: HashMap<String, String> = Gson().fromJson(usersJson, usersType) ?: hashMapOf()

        users[username] = password
        val editor = sharedPreferences.edit()
        editor.putString("users", Gson().toJson(users))
        editor.apply()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun navigateToMainActivity() {
        val intent = Intent(this, last::class.java)
            startActivity(intent)


    }
}