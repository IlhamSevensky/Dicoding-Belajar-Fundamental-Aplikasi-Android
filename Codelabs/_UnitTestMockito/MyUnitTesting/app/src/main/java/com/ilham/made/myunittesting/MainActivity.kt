package com.ilham.made.myunittesting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.ilham.made.myunittesting.Model.CuboidModel
import com.ilham.made.myunittesting.ViewModel.MainViewModel

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var edtWidth: EditText
    private lateinit var edtHeight: EditText
    private lateinit var edtLength: EditText
    private lateinit var tvResult: TextView
    private lateinit var btnCalculateVolume: Button
    private lateinit var btnCalculateCircumference: Button
    private lateinit var btnCalculateSurfaceArea: Button
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = MainViewModel(CuboidModel())

        edtHeight = findViewById(R.id.edt_height)
        edtWidth = findViewById(R.id.edt_width)
        edtLength = findViewById(R.id.edt_length)
        tvResult = findViewById(R.id.tv_result)
        btnCalculateCircumference = findViewById(R.id.btn_calculate_circumference)
        btnCalculateSurfaceArea = findViewById(R.id.btn_calculate_surface_area)
        btnCalculateVolume = findViewById(R.id.btn_calculate_volume)
        btnSave = findViewById(R.id.btn_save)

        btnCalculateVolume.setOnClickListener(this)
        btnCalculateSurfaceArea.setOnClickListener(this)
        btnCalculateCircumference.setOnClickListener(this)
        btnSave.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        val length = edtLength.text.toString().trim()
        val height = edtHeight.text.toString().trim()
        val width = edtWidth.text.toString().trim()

        when {
            length.isEmpty() -> edtLength.error = "Field ini tidak boleh kosong"
            width.isEmpty() -> edtWidth.error = "Field ini tidak boleh kosong"
            height.isEmpty() -> edtHeight.error = "Field ini tidak boleh kosong"

            else -> {
                val l = length.toDouble()
                val w = width.toDouble()
                val h = height.toDouble()

                when {
                    v?.id == R.id.btn_save -> {
                        mainViewModel.save(l,w,h)
                        visible()
                    }

                    v?.id == R.id.btn_calculate_volume -> {
                        tvResult.text =  mainViewModel.getVolume().toString()
                        gone()
                    }

                    v?.id == R.id.btn_calculate_surface_area -> {
                        tvResult.text =  mainViewModel.getSurfaceArea().toString()
                        gone()
                    }

                    v?.id == R.id.btn_calculate_circumference -> {
                        tvResult.text =  mainViewModel.getCircumference().toString()
                        gone()
                    }
                }
            }
        }
    }

    private fun visible() {
        btnSave.visibility = View.GONE
        btnCalculateCircumference.visibility = View.VISIBLE
        btnCalculateSurfaceArea.visibility = View.VISIBLE
        btnCalculateVolume.visibility = View.VISIBLE
    }

    private fun gone() {
        btnSave.visibility = View.VISIBLE
        btnCalculateCircumference.visibility = View.GONE
        btnCalculateSurfaceArea.visibility = View.GONE
        btnCalculateVolume.visibility = View.GONE
    }
}
