package com.ilham.made.thirdsubmission.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilham.made.thirdsubmission.DetailActivity
import com.ilham.made.thirdsubmission.R
import com.ilham.made.thirdsubmission.adapters.MovieAdapter
import com.ilham.made.thirdsubmission.models.MovieModel
import com.ilham.made.thirdsubmission.utils.Constants
import com.ilham.made.thirdsubmission.viewmodels.MovieState
import com.ilham.made.thirdsubmission.viewmodels.MovieViewModel
import kotlinx.android.synthetic.main.empty_state.*
import kotlinx.android.synthetic.main.fragment_movie.*

/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : Fragment() {

    private lateinit var movieViewModel: MovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onResume() {
        super.onResume()
        // Check if movie data in ViewModel isNullOrEmpty
        if (movieViewModel.getMovies().value.isNullOrEmpty()) {
            // if movie data isNullOrEmpty, Fill up movie data
            movieViewModel.fetchAllMovies()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        movieViewModel =
            ViewModelProvider(this@MovieFragment, ViewModelProvider.NewInstanceFactory()).get(
                MovieViewModel::class.java
            )

        showRecyclerList()

        movieViewModel.getMovies().observe(viewLifecycleOwner, Observer {
            rv_movie.adapter?.let { adapter ->
                if (adapter is MovieAdapter) {
                    // Fill up data to recyclerview
                    adapter.setMovies(it)
                    // On item click
                    adapter.setOnItemClickCallback(object : MovieAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: MovieModel) {
                            val mBundle = Bundle()
                            mBundle.putString(
                                DetailActivity.EXTRA_TYPE,
                                Constants.ACTIVITY_TYPE_MOVIE
                            )
                            mBundle.putInt(DetailActivity.EXTRA_DATA, data.id!!)
                            findNavController().navigate(
                                R.id.action_navigation_movie_to_detailActivity,
                                mBundle
                            )
                        }

                    })
                }
            }

        })

        movieViewModel.getMovieState().observer(viewLifecycleOwner, Observer {
            handleUIState(it)
        })
    }

    private fun showRecyclerList() {
        rv_movie.apply {
            layoutManager = LinearLayoutManager(context)
            // Fill adapter with empty list
            adapter = MovieAdapter(mutableListOf())
        }

    }

    private fun handleUIState(it: MovieState) {
        when (it) {
            is MovieState.IsLoading -> {
                isLoading(it.state)
            }
            is MovieState.Error -> {
                isLoading(false)
                empty_state_movie.visibility = View.VISIBLE
                btn_reconnect.setOnClickListener { view ->
                    movieViewModel.fetchAllMovies()
                }
            }
            is MovieState.IsSuccess -> {
                empty_state_movie.visibility = View.GONE
            }
        }
    }

    private fun isLoading(state: Boolean) {
        if (state) {
            loading_movie.visibility = View.VISIBLE
        } else {
            loading_movie.visibility = View.GONE
        }
    }
}
