package com.ilham.made.myflexiblefragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import com.ilham.made.myflexiblefragment.Fragment.HomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mFragmentManager = supportFragmentManager // seperti getSupportFragmentManager() di java
        val mHomeFragment = HomeFragment() // inisialisasi fragment
        val fragment = mFragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)

        if (fragment !is HomeFragment) {
            Log.d("MyFlexibleFragment", "Fragment name : " + HomeFragment::class.java.simpleName)

            mFragmentManager.commit {
                add(R.id.frame_container, mHomeFragment, HomeFragment::class.java.simpleName)
            }
        }
    }

    override fun onStart() {
        Log.d("ACTIVITY STATE : ", "ON START")
        super.onStart()
    }

    override fun onPause() {
        Log.d("ACTIVITY STATE : ", "ON PAUSE")
        super.onPause()
    }

    override fun onResume() {
        Log.d("ACTIVITY STATE : ", "ON RESUME")
        super.onResume()
    }
}
