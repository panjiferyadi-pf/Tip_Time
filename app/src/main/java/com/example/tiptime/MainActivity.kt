package com.example.tiptime

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calculateButton.setOnClickListener {
            calculateTip()
        }

        binding.costOfServiceEditText.setOnKeyListener {
            view, keyCode, _ -> handleKeyEvent(view, keyCode)
        }
    }

    private fun calculateTip() {
        // TODO: Menambahkan .text di akhir berarti mengambil hasil dari EditText dgn id cost_of_service, dan mendapatkan properti text darinya.
        //  Ini namanya pola Chaining (perantaian).
        //  Atribut .text pada EditText adalah EDITABLE,
        //  karena mewakili teks yang dapat diubah,
        //  Sehingga teks yg editable itu perlu dikonversi dulu ke tipe String, maka gunakan atribut .toString()
        val stringInTextField = binding.costOfServiceEditText.text.toString()

        // TODO: Karena kebutuhan datanya bertipe Double (angka desimal),
        //  Sedangkan hasil properti yang didapat dari EditText adalah bertipe String,
        //  Maka perlu dikonversi menjadi Double.
        // TODO: Seringkali input yg dimasukkan bukan hanya string, walaupun di xml sudah dinyatakan dgn (android:inputType="numberDecimal")
        //  input yg dimasukkan bisa saja kosong,akibatnya bisa bikin aplikasi crash.
        //  maka ubah metode .toDouble menjadi .toDoubleOrNull
        //  lalu buatkan if condition kalau cost nya null maka langsung keluar dari method .toDoubleOrNull nya.
        //  (return) yang kosong maksudnya keluar dari method tsb tanpa melakukan eksekusi instruksi lain,
        //  (return) dengan instruksi lain, maka tambahkan ekspresi setelah kata return,
        //  atau bisa memberikan instruksi sebelum method return.
        val cost = stringInTextField.toDoubleOrNull()
        if (cost == null || cost == 0.0) {
            displayTip(0.0)
            return
        }

        // TODO: Sekarang ketahui dulu option yang dipilih apa
        //  kodenya seperti ini: ( binding.tipOptions.checkedRadioButtonId )
        // TODO: Setelah tahu option apa yg dipilih,
        //  tentukan nilai persentase yang sesuai dengan tampilan yg ada.
        val tipPersentage = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_fifteen_percent -> 0.15
            R.id.option_eighteen_percent -> 0.18
            else -> 0.20
        }

        //TODO: Setelah nilai persentasenya ditetapkan,
        // Buatkan rumus untuk menghitung tip sesuai cost_of_service dan nilai persentase tip pada option RadioButton.
        // gunakan var instead val karena hasil tip nya akan dibulatkan sehingga memerlukan perubahan nilai pada variabel tip.
        var tip = tipPersentage * cost

        //TODO: pikirkan soal membulatkan tip.
        // buatkan pernyataan kondisional jika switch dalam kondisi checked,
        // maka nilai hasil tip dibulatkan ke atas menggunakan library math kotlin.
        if (binding.roundUpSwitch.isChecked) {
            tip = kotlin.math.ceil(tip)
        }

        //TODO: Siapa yg tahu bentuk nilai setiap mata uang?
        // oleh karena itu kita panggil saja method getCurrencyInstance() pada abstract class NumberFormat.
        // Sistem akan otomatis memformat mata uang berdasarkan bahasa dan setelan lain yang telah dipilih pengguna di ponsel mereka.
        NumberFormat.getCurrencyInstance()

        //TODO: lalu kita gunakan pemformat angka itu ( NumberFormat.getCurrencyInstance() )
        // gunakan method format() untuk menghubungkannya dengan variabel tip
        //val formattedTip = NumberFormat.getCurrencyInstance().format(0.0)

        //TODO: Tampilkan hasil tip yang telah dihitung dan diformat ke dalam textView dgn id tip_result
        //binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
        displayTip(tip)
    }

    private fun displayTip(tip: Double) {
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
    }

    private fun handleKeyEvent(view: View, keyCode: Int) : Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            //SEMBUNYIKAN KEYBOARD NYA
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}