package com.ilham.made.consumerfavorite.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.ilham.made.consumerfavorite.R
import com.ilham.made.consumerfavorite.data.repository.MovieState
import com.ilham.made.consumerfavorite.data.repository.TvShowState
import com.ilham.made.consumerfavorite.models.MovieModel
import com.ilham.made.consumerfavorite.models.TvShowModel
import com.ilham.made.consumerfavorite.ui.detail.DetailActivity
import com.ilham.made.consumerfavorite.ui.main.MainActivity
import com.ilham.made.consumerfavorite.utils.Constants
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.empty_state.*

class SearchActivity : AppCompatActivity() {

    companion object {
        private val TAG = SearchActivity::class.java.simpleName
    }

    private val searchViewModel: SearchViewModel by lazy {
        val activity = requireNotNull(this@SearchActivity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        ViewModelProvider(this@SearchActivity, SearchViewModel.Factory(activity.application))
            .get(SearchViewModel::class.java)
    }

    private var TYPE_MOVIE = "type_movie"
    private var TYPE_TVSHOW = "type_tvshow"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        if (intent.getStringExtra(MainActivity.EXTRA_TYPE)
                .equals(resources.getString(R.string.title_movie_tab), ignoreCase = true)
        ) {
            setupRecyclerView(TYPE_MOVIE)
            setupSearchView(TYPE_MOVIE)
            searchViewModel.searchMovieState.observer(this@SearchActivity, Observer {
                handleUIStateMovie(it)
            })
        } else {
            setupRecyclerView(TYPE_TVSHOW)
            setupSearchView(TYPE_TVSHOW)
            searchViewModel.searchTvShowState.observer(this@SearchActivity, Observer {
                handleUIStateTvShow(it)
            })
        }
    }

    private fun setupSearchView(type: String) {
        with(search_view) {
            setSearchFocused(true)
            if (type.equals(TYPE_MOVIE, ignoreCase = true)) {
                setSearchHint(resources.getString(R.string.hint_search_movie))
            } else {
                setSearchHint(resources.getString(R.string.hint_search_tvshow))
            }

            setOnHomeActionClickListener {
                finish()
            }
            setOnSearchListener(object : FloatingSearchView.OnSearchListener {
                override fun onSearchAction(currentQuery: String?) {
                    if (type.equals(TYPE_MOVIE, ignoreCase = true)) {
                        if (currentQuery != null) {
                            if (currentQuery.isNotEmpty()) {
                                searchViewModel.getMovieResult(currentQuery)
                                    ?.observe(this@SearchActivity, Observer {
                                        when (it) {
                                            null -> {
                                                empty_state_search.visibility = View.VISIBLE
                                                btn_reconnect.visibility = View.GONE
                                            }

                                            else -> {
                                                if (it.isEmpty()) {
                                                    emptyStateSearchNotFound()
                                                } else {
                                                    rv_search_result.adapter.let { adapter ->
                                                        if (adapter is MovieResultAdapter) {
                                                            empty_state_search.visibility =
                                                                View.GONE
                                                            adapter.setMovies(it)
                                                            adapter.setOnItemClickCallback(object :
                                                                MovieResultAdapter.OnItemClickCallback {
                                                                override fun onItemClicked(data: MovieModel) {
                                                                    startActivity(
                                                                        Intent(
                                                                            context,
                                                                            DetailActivity::class.java
                                                                        )
                                                                            .putExtra(
                                                                                DetailActivity.EXTRA_TYPE,
                                                                                Constants.ACTIVITY_TYPE_MOVIE
                                                                            )
                                                                            .putExtra(
                                                                                DetailActivity.EXTRA_DATA,
                                                                                data.movie_id
                                                                            )
                                                                    )
                                                                }

                                                            })
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    })
                            }
                        }
                    } else {
                        if (currentQuery != null) {
                            if (currentQuery.isNotEmpty()) {
                                searchViewModel.getTvShowResult(currentQuery)
                                    .observe(this@SearchActivity, Observer {
                                        when (it) {
                                            null -> {
                                                empty_state_search.visibility = View.VISIBLE
                                                btn_reconnect.visibility = View.GONE
                                            }

                                            else -> {
                                                if (it.isEmpty()) {
                                                    emptyStateSearchNotFound()
                                                } else {
                                                    rv_search_result.adapter.let { adapter ->
                                                        if (adapter is TvShowResultAdapter) {
                                                            empty_state_search.visibility =
                                                                View.GONE
                                                            adapter.setTvShows(it)
                                                            adapter.setOnItemClickCallback(object :
                                                                TvShowResultAdapter.OnItemClickCallback {
                                                                override fun onItemClicked(data: TvShowModel) {
                                                                    startActivity(
                                                                        Intent(
                                                                            context,
                                                                            DetailActivity::class.java
                                                                        )
                                                                            .putExtra(
                                                                                DetailActivity.EXTRA_TYPE,
                                                                                Constants.ACTIVITY_TYPE_TVSHOW
                                                                            )
                                                                            .putExtra(
                                                                                DetailActivity.EXTRA_DATA,
                                                                                data.tv_show_id
                                                                            )
                                                                    )
                                                                }

                                                            })
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    })
                            }
                        }
                    }
                }

                override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
                    // Nothing
                }

            })
        }

    }

    override fun onResume() {
        super.onResume()
        rv_search_result.adapter.let { adapter ->
            when (adapter) {
                is MovieResultAdapter -> {
                    searchViewModel.recoverMovieResult().observe(this@SearchActivity, Observer {
                        adapter.setMovies(it)
                    })
                }
                is TvShowResultAdapter -> {
                    searchViewModel.recoverTvShowResult().observe(this@SearchActivity, Observer {
                        adapter.setTvShows(it)
                    })
                }
            }
        }
    }

    private fun setupRecyclerView(type: String) {
        if (type.equals(TYPE_MOVIE, ignoreCase = true)) {
            rv_search_result.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = MovieResultAdapter(mutableListOf())
            }
        } else {
            rv_search_result.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = TvShowResultAdapter(mutableListOf())
            }
        }
    }

    private fun handleUIStateMovie(it: MovieState) {
        when (it) {
            is MovieState.IsLoading -> {
                isLoading(it.state)
            }
            is MovieState.Error -> {
                isLoading(false)
                Log.d(TAG, "ERROR : ${it.error}")
                empty_state_search.visibility = View.VISIBLE
            }
            is MovieState.IsSuccess -> {
                empty_state_search.visibility = View.GONE
            }
        }
    }

    private fun handleUIStateTvShow(it: TvShowState) {
        when (it) {
            is TvShowState.IsLoading -> {
                isLoading(it.state)
            }
            is TvShowState.Error -> {
                isLoading(false)
                Log.d(TAG, "ERROR : ${it.error}")
                empty_state_search.visibility = View.VISIBLE
            }
            is TvShowState.IsSuccess -> {
                empty_state_search.visibility = View.GONE
            }
        }
    }

    private fun isLoading(state: Boolean) {
        if (state) {
            loading_search.visibility = View.VISIBLE
        } else {
            loading_search.visibility = View.GONE
        }
    }

    private fun emptyStateSearchNotFound() {
        img_empty_state.setImageResource(R.drawable.ic_empty_state_no_result_found)
        btn_reconnect.visibility = View.GONE
        img_empty_state.contentDescription =
            resources.getString(R.string.empty_state_title_not_found)
        title_empty_state.text = resources.getString(R.string.empty_state_title_not_found)
        desc_empty_state.text =
            resources.getString(R.string.empty_state_desc_not_found)
        empty_state_search.visibility = View.VISIBLE
    }
}
