package com.ilham.made.barvolume

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var edtWidth: EditText
    private lateinit var edtLength: EditText
    private lateinit var edtHeight: EditText
    private lateinit var btnCalculate: Button
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtWidth = findViewById(R.id.edt_width)
        edtLength = findViewById(R.id.edt_length)
        edtHeight = findViewById(R.id.edt_height)
        btnCalculate = findViewById(R.id.btn_calculate)
        tvResult = findViewById(R.id.tv_result)

        btnCalculate.setOnClickListener(this)

        // Mengecek state activity dan mengembalikan result atau value jika activity restart
        if (savedInstanceState != null) {
            var result = savedInstanceState.getString(STATE_RESULT) as String
            tvResult.text = result
        }
    }

    override fun onClick(p0: View?) {
        if (p0?.id == R.id.btn_calculate) {
            // trim digunakan untuk menghiraukan spasi jika ada (untuk input tanpa spasi)
            val inputLength = edtLength.text.toString().trim()
            val inputWidth = edtWidth.text.toString().trim()
            val inputHeight = edtHeight.text.toString().trim()

            var isEmptyFields = false

            when {
                inputLength.isEmpty() -> {
                    isEmptyFields = true
                    edtLength.error = "Field ini tidak boleh kosong"
                }

                inputWidth.isEmpty() -> {
                    isEmptyFields = true
                    edtWidth.error = "Field ini tidak boleh kosong"
                }

                inputHeight.isEmpty() -> {
                    isEmptyFields = true
                    edtHeight.error = "Field ini tidak boleh kosong"
                }
            }

//            if (inputLength.isEmpty()){
//                isEmptyFields = true
//                edtLength.error = "Field ini tidak boleh kosong"
//            }
//
//            if (inputWidth.isEmpty()){
//                isEmptyFields = true
//                edtWidth.error = "Field ini tidak boleh kosong"
//            }
//
//            if (inputHeight.isEmpty()){
//                isEmptyFields = true
//                edtHeight.error = "Field ini tidak boleh kosong"
//            }

            if (!isEmptyFields){
                val volume = inputLength.toDouble() * inputWidth.toDouble() * inputHeight.toDouble()
                tvResult.text = volume.toString()
            }
        }
    }

    companion object {
        // Variabel untuk menyimpan state result
        private const val STATE_RESULT = "state_result"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // Method untuk menyimpan result / value saat activity restart (jangan simpan bitmap disini)
        super.onSaveInstanceState(outState)
        outState.putString(STATE_RESULT, tvResult.text.toString())
    }
}
