package com.ilham.made.myviewandviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        if (supportActionBar != null) {
//            (supportActionBar as ActionBar).title = "Google Pixel"
//        }

        supportActionBar?.title = "Google Pixel"
    }
}
