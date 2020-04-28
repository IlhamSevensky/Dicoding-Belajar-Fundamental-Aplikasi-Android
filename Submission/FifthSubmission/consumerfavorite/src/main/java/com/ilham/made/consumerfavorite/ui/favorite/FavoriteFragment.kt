package com.ilham.made.consumerfavorite.ui.favorite

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.ilham.made.consumerfavorite.R
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
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sectionPagerAdapter = FavoriteSectionPagerAdapter(view.context, childFragmentManager)
        view_pager_favorite.adapter = sectionPagerAdapter
        tab_favorite.setupWithViewPager(view_pager_favorite)

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.search)?.isVisible = false
    }

}
