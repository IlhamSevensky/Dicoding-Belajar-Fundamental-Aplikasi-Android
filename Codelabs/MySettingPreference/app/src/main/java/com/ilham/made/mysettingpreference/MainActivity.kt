package com.ilham.made.mysettingpreference

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ilham.made.mysettingpreference.fragment.MyPreferenceFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.setting_holder, MyPreferenceFragment())
            .commit()

    }
}
