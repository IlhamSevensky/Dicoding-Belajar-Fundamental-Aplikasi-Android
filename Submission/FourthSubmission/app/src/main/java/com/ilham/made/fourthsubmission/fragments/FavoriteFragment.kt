package com.ilham.made.fourthsubmission.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ilham.made.fourthsubmission.R
import com.ilham.made.fourthsubmission.adapters.FavoriteSectionPagerAdapter
import kotlinx.android.synthetic.main.fragment_favorite.*

/**
 * A simple [Fragment] subclass.
 */
class FavoriteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val sectionPagerAdapter = FavoriteSectionPagerAdapter(view.context, childFragmentManager)
        view_pager_favorite.adapter = sectionPagerAdapter
        tab_favorite.setupWithViewPager(view_pager_favorite)
    }


}
