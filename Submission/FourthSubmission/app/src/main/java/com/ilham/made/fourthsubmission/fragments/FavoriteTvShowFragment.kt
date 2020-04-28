package com.ilham.made.fourthsubmission.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilham.made.fourthsubmission.DetailActivity
import com.ilham.made.fourthsubmission.R
import com.ilham.made.fourthsubmission.adapters.TvShowFavoriteAdapter
import com.ilham.made.fourthsubmission.models.FavoriteTvShowModel
import com.ilham.made.fourthsubmission.utils.Constants
import com.ilham.made.fourthsubmission.viewmodels.FavoriteTvShowState
import com.ilham.made.fourthsubmission.viewmodels.FavoriteTvShowViewModel
import kotlinx.android.synthetic.main.empty_state.*
import kotlinx.android.synthetic.main.fragment_favorite_tv_show.*

/**
 * A simple [Fragment] subclass.
 */
class FavoriteTvShowFragment : Fragment() {

    private lateinit var favoriteTvShowViewModel: FavoriteTvShowViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_tv_show, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        favoriteTvShowViewModel = ViewModelProvider(
            this@FavoriteTvShowFragment,
            ViewModelProvider.NewInstanceFactory()
        ).get(FavoriteTvShowViewModel::class.java)

        setupRecyclerView()

        favoriteTvShowViewModel.getTvShowFav().observe(viewLifecycleOwner, Observer {
            rv_favorite_tvshow.adapter.let { adapter ->
                if (adapter is TvShowFavoriteAdapter) {
                    adapter.setTvShow(it)

                    adapter.setOnItemClickCallback(object :
                        TvShowFavoriteAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: FavoriteTvShowModel) {
                            val mBundle = Bundle()
                            mBundle.putString(
                                DetailActivity.EXTRA_TYPE,
                                Constants.ACTIVITY_TYPE_TVSHOW
                            )
                            mBundle.putInt(DetailActivity.EXTRA_DATA, data.tvshow_id!!.toInt())
                            findNavController().navigate(
                                R.id.detailActivity,
                                mBundle
                            )
                        }

                        override fun onFavoriteClicked(data: Int) {
                            if (data == 0) {
                                adapter.setTvShow(mutableListOf())
                                emptyStateEmptyFavorite()
                            } else {
                                favoriteTvShowViewModel.fetchAllTvShowFav(context!!)

                                adapter.setTvShow(it)
                            }
                        }

                    })
                }
            }
        })

        favoriteTvShowViewModel.getTvShowFavState().observer(viewLifecycleOwner, Observer {
            handleUIState(it)
        })
    }

    override fun onResume() {
        super.onResume()
        if (favoriteTvShowViewModel.getTvShowFav().value.isNullOrEmpty()){
            favoriteTvShowViewModel.fetchAllTvShowFav(context!!)
        }

    }

    private fun setupRecyclerView() {
        rv_favorite_tvshow.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TvShowFavoriteAdapter(mutableListOf())
        }
    }

    private fun handleUIState(it: FavoriteTvShowState) {
        when (it) {
            is FavoriteTvShowState.IsLoading -> {
                isLoading(it.state)
            }
            is FavoriteTvShowState.IsEmpty -> {
                isLoading(false)
                emptyStateEmptyFavorite()
            }
            is FavoriteTvShowState.IsSuccess -> {
                isLoading(false)
                empty_state_favorite_tvshow.visibility = View.GONE
            }
        }
    }

    // set empty state error page
    private fun emptyStateEmptyFavorite() {
        img_empty_state.setImageResource(R.drawable.ic_empty_state_favorite)
        btn_reconnect.visibility = View.GONE
        img_empty_state.contentDescription =
            resources.getString(R.string.empty_state_desc_no_favorite_item_tvshow)
        title_empty_state.text = resources.getString(R.string.empty_state_title_no_favorite_item)
        desc_empty_state.text = resources.getString(R.string.empty_state_desc_no_favorite_item_tvshow)
        empty_state_favorite_tvshow.visibility = View.VISIBLE
    }

    private fun isLoading(state: Boolean) {
        if (state) {
            loading_favorite_tvshow.visibility = View.VISIBLE
        } else {
            loading_favorite_tvshow.visibility = View.GONE
        }
    }


}
