package com.ilham.made.fifthsubmission.ui.movie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilham.made.fifthsubmission.R
import com.ilham.made.fifthsubmission.data.repository.MovieState
import com.ilham.made.fifthsubmission.models.MovieModel
import com.ilham.made.fifthsubmission.ui.detail.DetailActivity
import com.ilham.made.fifthsubmission.utils.Constants
import kotlinx.android.synthetic.main.empty_state.*
import kotlinx.android.synthetic.main.fragment_movie.*

/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : Fragment() {

    companion object {
        private val TAG = MovieFragment::class.java.simpleName
    }

    private val movieViewModel: MovieViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        ViewModelProvider(this@MovieFragment, MovieViewModel.Factory(activity.application))
            .get(MovieViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onResume() {
        super.onResume()
        rv_movie.adapter.let {
            when(it){
                is MovieAdapter -> it.notifyDataSetChanged()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showRecyclerList()
        viewModelSetup()

        btn_reconnect.setOnClickListener {
            movieViewModel.refreshMovieFromRepository()
        }
    }

    private fun viewModelSetup() {
        movieViewModel.movies.observe(viewLifecycleOwner, Observer { movies ->
            movies?.apply {
                rv_movie.adapter?.let { adapter ->
                    if (adapter is MovieAdapter) {
                        adapter.setMovies(this)
                        adapter.setOnItemClickCallback(object : MovieAdapter.OnItemClickCallback {
                            override fun onItemClicked(data: MovieModel) {
                                startActivity(Intent(context,DetailActivity::class.java)
                                    .putExtra(DetailActivity.EXTRA_TYPE, Constants.ACTIVITY_TYPE_MOVIE)
                                    .putExtra(DetailActivity.EXTRA_DATA, data.movie_id))

                            }
                        })
                    }
                }
            }
        })

        movieViewModel.state.observer(viewLifecycleOwner, Observer {
            handleUIState(it)
        })
    }

    private fun showRecyclerList() {
        rv_movie.apply {
            layoutManager = LinearLayoutManager(context)
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
                Log.d(TAG, "ERROR : ${it.error}")
                empty_state_movie.visibility = View.VISIBLE
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
