import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.pr18.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class last : AppCompatActivity() {

    private lateinit var spinnerCurrency: Spinner
    private lateinit var etAmount: EditText
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        spinnerCurrency = findViewById(R.id.spinnerCurrency)
        etAmount = findViewById(R.id.etAmount)
        sharedPreferences = getSharedPreferences("CurrencyDiaryPrefs", MODE_PRIVATE)
        username = intent.getStringExtra("username") ?: ""

        val currencies = resources.getStringArray(R.array.currency_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCurrency.adapter = adapter
    }

    fun handler(v: View) {
        when (v.id) {
            R.id.btnSave -> {
                val currency = spinnerCurrency.selectedItem.toString()
                val amount = etAmount.text.toString()
                if (amount.isEmpty()) {
                    Toast.makeText(this, "Введите сумму", Toast.LENGTH_SHORT).show()
                } else {
                    saveCurrencyData(currency, amount)
                }
            }

            R.id.btnView -> {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Загрузка данных")
                alertDialog.setMessage("Вы действительно хотите загрузить данные?")
                alertDialog.setPositiveButton("Да") { dialog, which ->
                    loadCurrencyData()
                }
                alertDialog.setNegativeButton("Нет", null)
                alertDialog.show()
            }
        }
    }

    private fun saveCurrencyData(currency: String, amount: String) {
        val gson = Gson()
        val json = sharedPreferences.getString("currencyData_$username", null)
        val type = object : TypeToken<MutableMap<String, String>>() {}.type
        val currencyData: MutableMap<String, String> = gson.fromJson(json, type) ?: mutableMapOf()

        currencyData[currency] = amount

        val editor = sharedPreferences.edit()
        editor.putString("currencyData_$username", gson.toJson(currencyData))
        editor.apply()

        Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show()
    }

    private fun loadCurrencyData() {
        val gson = Gson()
        val json = sharedPreferences.getString("currencyData_$username", null)
        if (json != null) {
            val type = object : TypeToken<MutableMap<String, String>>() {}.type
            val currencyData: MutableMap<String, String> = gson.fromJson(json, type)

            val builder = StringBuilder()
            for ((currency, amount) in currencyData) {
                builder.append("$currency: $amount\n")
            }

            AlertDialog.Builder(this)
                .setTitle("Сохраненные данные")
                .setMessage(builder.toString())
                .setPositiveButton("ОК", null)
                .show()
        } else {
            Toast.makeText(this, "Нет сохраненных данных", Toast.LENGTH_SHORT).show()
        }
    }
}