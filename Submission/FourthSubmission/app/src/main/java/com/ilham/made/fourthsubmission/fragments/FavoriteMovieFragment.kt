package com.ilham.made.fourthsubmission.fragments


import android.os.Bundle
import android.util.Log
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
import com.ilham.made.fourthsubmission.adapters.MovieFavoriteAdapter
import com.ilham.made.fourthsubmission.models.FavoriteMovieModel
import com.ilham.made.fourthsubmission.utils.Constants
import com.ilham.made.fourthsubmission.viewmodels.FavoriteMovieState
import com.ilham.made.fourthsubmission.viewmodels.FavoriteMovieViewModel
import kotlinx.android.synthetic.main.empty_state.*
import kotlinx.android.synthetic.main.fragment_favorite_movie.*

/**
 * A simple [Fragment] subclass.
 */
class FavoriteMovieFragment : Fragment() {

    private lateinit var favoriteMovieViewModel: FavoriteMovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        favoriteMovieViewModel = ViewModelProvider(
            this@FavoriteMovieFragment,
            ViewModelProvider.NewInstanceFactory()
        ).get(FavoriteMovieViewModel::class.java)

        setupRecyclerView()

        favoriteMovieViewModel.getMoviesFav().observe(viewLifecycleOwner, Observer {
            Log.d("LL", it.size.toString())
            rv_favorite_movie.adapter.let { adapter ->
                if (adapter is MovieFavoriteAdapter) {
                    Log.d("LAST", it.toString())
                    adapter.setMovies(it)

                    adapter.setOnItemClickCallback(object :
                        MovieFavoriteAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: FavoriteMovieModel) {
                            val mBundle = Bundle()
                            mBundle.putString(
                                DetailActivity.EXTRA_TYPE,
                                Constants.ACTIVITY_TYPE_MOVIE
                            )
                            mBundle.putInt(DetailActivity.EXTRA_DATA, data.movie_id!!.toInt())
                            findNavController().navigate(
                                R.id.detailActivity,
                                mBundle
                            )
                        }

                        override fun onFavoriteClicked(data: Int) {
                            if (data == 0) {
                                adapter.setMovies(mutableListOf())
                                emptyStateEmptyFavorite()
                            } else {
                                favoriteMovieViewModel.fetchAllMovieFav(context!!)

                                adapter.setMovies(it)
                            }
                        }

                    })
                }
            }
        })

        favoriteMovieViewModel.getMovieFavState().observer(viewLifecycleOwner, Observer {
            handleUIState(it)
        })
    }

    override fun onResume() {
        super.onResume()
        if (favoriteMovieViewModel.getMoviesFav().value.isNullOrEmpty()){
            favoriteMovieViewModel.fetchAllMovieFav(context!!)
        }
    }

    private fun setupRecyclerView() {
        rv_favorite_movie.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = MovieFavoriteAdapter(mutableListOf())
        }
    }

    private fun handleUIState(it: FavoriteMovieState) {
        when (it) {
            is FavoriteMovieState.IsLoading -> {
                isLoading(it.state)
            }
            is FavoriteMovieState.IsEmpty -> {
                isLoading(false)
                emptyStateEmptyFavorite()
            }
            is FavoriteMovieState.IsSuccess -> {
                isLoading(false)
                empty_state_favorite_movie.visibility = View.GONE
            }
        }
    }

    // set empty state error page
    private fun emptyStateEmptyFavorite() {
        img_empty_state.setImageResource(R.drawable.ic_empty_state_favorite)
        btn_reconnect.visibility = View.GONE
        img_empty_state.contentDescription =
            resources.getString(R.string.empty_state_desc_no_favorite_item_movie)
        title_empty_state.text = resources.getString(R.string.empty_state_title_no_favorite_item)
        desc_empty_state.text = resources.getString(R.string.empty_state_desc_no_favorite_item_movie)
        empty_state_favorite_movie.visibility = View.VISIBLE
    }

    private fun isLoading(state: Boolean) {
        if (state) {
            loading_favorite_movie.visibility = View.VISIBLE
        } else {
            loading_favorite_movie.visibility = View.GONE
        }
    }

}
