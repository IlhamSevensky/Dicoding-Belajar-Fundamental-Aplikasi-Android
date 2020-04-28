package com.ilham.made.mytablayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ilham.made.mytablayout.adapter.SectionPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionPagerAdapter
        tabs.setupWithViewPager(view_pager) // untuk hubungkan tablayout ke viewpager adapter (agar bisa swipe pada tab layout)

        supportActionBar?.elevation = 0f
    }
}
