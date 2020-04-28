package com.ilham.made.fourthsubmission.adapters

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ilham.made.fourthsubmission.R
import com.ilham.made.fourthsubmission.fragments.FavoriteMovieFragment
import com.ilham.made.fourthsubmission.fragments.FavoriteTvShowFragment

class FavoriteSectionPagerAdapter (private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.tab_title_movie, R.string.tab_title_tvshow)

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null

        when (position){
            0 -> fragment = FavoriteMovieFragment()
            1 -> fragment = FavoriteTvShowFragment()
        }

        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? = context.resources.getString(TAB_TITLES[position])

    override fun getCount(): Int = TAB_TITLES.size

}