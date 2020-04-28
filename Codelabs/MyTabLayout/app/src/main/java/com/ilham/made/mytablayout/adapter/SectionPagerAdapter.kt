package com.ilham.made.mytablayout.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ilham.made.mytablayout.R
import com.ilham.made.mytablayout.fragment.HomeFragment

class SectionPagerAdapter (private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){ // <-- parameter behaviour

    /*
    Mulai API 27 ke atas, Anda perlu menambahkan behaviour pada parameter kedua,
    fungsinya yaitu supaya hanya fragment yang ditampilkan saja yang akan masuk ke lifecycle RESUMED,
    sedangkan yang lainnya masuk ke STARTED.
     */

    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3)

    override fun getItem(position: Int): Fragment {
        val fragment = HomeFragment.newInstance(position + 1)

        return fragment
//        var fragment: Fragment? = null
//
//        when (position){
//            0 -> fragment = HomeFragment()
//            1 -> fragment = ProfileFragment()
//        }
//
//        return fragment as Fragment
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }
}