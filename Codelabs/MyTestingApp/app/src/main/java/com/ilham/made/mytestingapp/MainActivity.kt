package com.ilham.made.mytestingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import java.lang.StringBuilder

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var btnSetValue: Button
    private lateinit var tvText: TextView
    private lateinit var imgPreview: ImageView

    private var names = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvText = findViewById(R.id.tv_text)
        btnSetValue = findViewById(R.id.btn_set_value)
        btnSetValue.setOnClickListener(this)
        imgPreview = findViewById(R.id.img_preview)
//        imgPreview.setImageResource(R.drawable.fronalpstock_big) // error image too large
        Glide.with(this)
            .load(R.drawable.fronalpstock_big)
            .override(1000,500)
            .into(imgPreview)

        names.add("Ilham Budi Prasetyo")
        names.add("Fadil Bumantara")
        names.add("Rafi Asilah")
    }

    override fun onClick(p0: View?) {
        if (p0?.id == R.id.btn_set_value) {
//            tvText.text = "19"
            val name = StringBuilder()
            for (i in 0..2) {
                name.append(names[i]).append("\n")
            }
            tvText.text = name.toString()

        }
    }
}
