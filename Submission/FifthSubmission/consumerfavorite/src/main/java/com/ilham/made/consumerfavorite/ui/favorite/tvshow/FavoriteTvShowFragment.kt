package com.ilham.made.consumerfavorite.ui.favorite.tvshow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilham.made.consumerfavorite.R
import com.ilham.made.consumerfavorite.models.FavoriteTvShowModel
import com.ilham.made.consumerfavorite.ui.detail.DetailActivity
import com.ilham.made.consumerfavorite.utils.Constants
import kotlinx.android.synthetic.main.empty_state.*
import kotlinx.android.synthetic.main.fragment_favorite_tv_show.*

/**
 * A simple [Fragment] subclass.
 */
class FavoriteTvShowFragment : Fragment() {

    private val favoriteTvShowViewModel: FavoriteTvShowViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        ViewModelProvider(
            this@FavoriteTvShowFragment,
            FavoriteTvShowViewModel.Factory(activity.application)
        )
            .get(FavoriteTvShowViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        rv_favorite_tvshow.adapter.let { adapter ->
            if (adapter is FavoriteTvShowAdapter) {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_tv_show, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        showRecyclerList()

        favoriteTvShowViewModel.favoriteTvShows?.observe(
            viewLifecycleOwner,
            Observer { favoriteTvShows ->
                favoriteTvShows?.apply {
                    if (this.isEmpty()) {
                        emptyStateEmptyFavorite()
                    } else {
                        rv_favorite_tvshow.adapter.let { adapter ->
                            if (adapter is FavoriteTvShowAdapter) {
                                empty_state_favorite_tvshow.visibility = View.GONE
                                adapter.setFavTvShows(favoriteTvShows)
                                adapter.setOnItemClickCallback(object :
                                    FavoriteTvShowAdapter.OnItemClickCallback {
                                    override fun onItemClicked(data: FavoriteTvShowModel) {
                                        startActivity(
                                            Intent(context, DetailActivity::class.java)
                                                .putExtra(
                                                    DetailActivity.EXTRA_TYPE,
                                                    Constants.ACTIVITY_TYPE_TVSHOW
                                                )
                                                .putExtra(
                                                    DetailActivity.EXTRA_DATA,
                                                    data.tvshow_id?.toInt()
                                                )
                                        )
                                    }

                                    override fun onFavoriteClicked(data: FavoriteTvShowModel) {
                                        adapter.setFavTvShows(favoriteTvShows)

                                    }

                                })
                            }
                        }
                    }
                }
            })
    }

    private fun showRecyclerList() {
        rv_favorite_tvshow.apply {
            layoutManager = LinearLayoutManager(context)
            // Fill adapter with empty list
            adapter = FavoriteTvShowAdapter(mutableListOf())
        }
    }

    private fun emptyStateEmptyFavorite() {
        img_empty_state.setImageResource(R.drawable.ic_empty_state_favorite)
        btn_reconnect.visibility = View.GONE
        img_empty_state.contentDescription =
            resources.getString(R.string.empty_state_title_no_favorite_item)
        title_empty_state.text = resources.getString(R.string.empty_state_title_no_favorite_item)
        desc_empty_state.text =
            resources.getString(R.string.empty_state_desc_no_favorite_item_tvshow)
        empty_state_favorite_tvshow.visibility = View.VISIBLE
    }

}
