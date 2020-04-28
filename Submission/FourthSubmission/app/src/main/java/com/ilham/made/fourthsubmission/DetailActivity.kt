package com.ilham.made.fourthsubmission

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ilham.made.fourthsubmission.database.MovieFavoriteHelper
import com.ilham.made.fourthsubmission.database.TvShowFavoriteHelper
import com.ilham.made.fourthsubmission.utils.Constants
import com.ilham.made.fourthsubmission.viewmodels.MovieState
import com.ilham.made.fourthsubmission.viewmodels.MovieViewModel
import com.ilham.made.fourthsubmission.viewmodels.TvShowState
import com.ilham.made.fourthsubmission.viewmodels.TvShowViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.empty_state.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TYPE = "extra_type"
        const val EXTRA_DATA = "extra_data"
    }

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var tvShowViewModel: TvShowViewModel
    private lateinit var movieFavoriteHelper: MovieFavoriteHelper
    private lateinit var tvShowFavoriteHelper: TvShowFavoriteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Toolbar
        toolbar_movie_detail.elevation = 8F
        toolbar_movie_detail.title = resources.getString(R.string.title_toolbar_detail)
        toolbar_movie_detail.navigationIcon =
            ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_24dp)
        toolbar_movie_detail.setNavigationOnClickListener { finish() }


        val type = intent.getStringExtra(EXTRA_TYPE)
        val id = intent.getIntExtra(EXTRA_DATA, 0)

        // Check if type isNullOrNot
        if (type != null) {
            // If Not, Check if type is movie or not
            if (type == Constants.ACTIVITY_TYPE_MOVIE) {
                movieViewModel = ViewModelProvider(
                    this@DetailActivity,
                    ViewModelProvider.NewInstanceFactory()
                ).get(MovieViewModel::class.java)

                if (movieViewModel.getMovieDetail().value == null || movieViewModel.getMovieCredits().value == null) {
                    movieViewModel.fetchMovieDetail(id)
                    movieViewModel.fetchMovieCrew(id)
                }

                movieViewModel.getMovieDetail().observe(this, Observer {
                    // Set movie detail from viewmodel data
                    tv_detail_title.text = it.title
                    tv_detail_desc.text = it.overview

                    tv_detail_runtime.text = minuteToHour(it.duration)

                    // check if movie favorite or not
                    movieFavoriteHelper = MovieFavoriteHelper.getInstance(this)
                    movieFavoriteHelper.open()

                    val cursors = movieFavoriteHelper.queryById(it.id.toString())

                    if (cursors.count > 0) {
                        btn_favorite_detail.setImageResource(R.drawable.ic_favorite_badge)
                        btn_favorite_detail.visibility = View.VISIBLE
                    } else {
                        btn_favorite_detail.visibility = View.GONE
                    }

                    movieFavoriteHelper.close()

                    // Check if poster path is null
                    if (it.poster != null) {
                        setImage(
                            this,
                            it.poster,
                            img_detail_movie
                        )
                    } else {
                        img_detail_movie.setImageResource(R.drawable.ic_no_image_available)
                        img_detail_movie.setBackgroundColor(Color.GRAY)
                    }

                    // Check if backdrop path is null
                    if (it.backdrop != null) {
                        setImage(
                            this,
                            it.backdrop,
                            img_detail_movie_highlight,
                            Constants.ENDPOINT_POSTER_SIZE_W780
                        )
                    } else {
                        img_detail_movie_highlight.setImageResource(R.drawable.ic_no_image_available)
                        img_detail_movie_highlight.setBackgroundColor(Color.GRAY)
                    }

                    /*
                     Check the amount of data (genre)
                     0 = null
                     1 = only 1 data
                     2 = more than 1 data
                     */

                    when (validateLongData(it.genre!!.size)) {
                        0 -> tv_detail_genre.text = resources.getString(R.string.empty_data)
                        1 -> tv_detail_genre.text = it.genre!![0].name
                        2 -> {
                            var i = 0
                            while (i < it.genre!!.size) {
                                if (i == it.genre!!.size - 1) {
                                    tv_detail_genre.append(it.genre!![i].name)
                                    break
                                }
                                tv_detail_genre.append(it.genre!![i].name + ", ")
                                i++
                            }
                        }
                    }

                })

                movieViewModel.getMovieCredits().observe(this, Observer {
                    // Set movie crew data from viewmodel data
                    var j = 0
                    val director = StringBuilder()
                    // Check the amount of data crew with job as a Director
                    while (j < it.director!!.size) {
                        if (it.director!![j].job.equals(Constants.JOB_DIRECTOR)) {
                            director.append(it.director!![j].name + ", ")
                        }
                        j++
                    }
                    // Check if director/creator not found
                    if (director.toString().isEmpty()) {
                        tv_detail_director.text = resources.getString(R.string.empty_data)
                    } else {
                        tv_detail_director.text =
                            director.substring(0, director.length - 2).toString()
                    }

                })

                movieViewModel.getMovieState().observer(this, Observer {
                    handleUIState(it, null, id)
                })


                // If Not, Check if type is tv show or not
            } else if (type == Constants.ACTIVITY_TYPE_TVSHOW) {

                lb_detail_director.text = resources.getString(R.string.label_creator)

                tvShowViewModel =
                    ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                        TvShowViewModel::class.java
                    )

                if (tvShowViewModel.getOneTvShow().value == null) {
                    tvShowViewModel.fetchTvShowDetail(id)
                }

                tvShowViewModel.getOneTvShow().observe(this, Observer {
                    // Set tv show detail from viewmodel data
                    tv_detail_title.text = it.title
                    tv_detail_desc.text = it.overview

                    // check if movie favorite or not
                    tvShowFavoriteHelper = TvShowFavoriteHelper.getInstance(this)
                    tvShowFavoriteHelper.open()

                    val cursors = tvShowFavoriteHelper.queryById(it.id.toString())

                    if (cursors.count > 0) {
                        btn_favorite_detail.setImageResource(R.drawable.ic_favorite_badge)
                        btn_favorite_detail.visibility = View.VISIBLE
                    } else {
                        btn_favorite_detail.visibility = View.GONE
                    }

                    tvShowFavoriteHelper.close()

                    // Check if poster path not null
                    if (it.poster != null) {
                        setImage(
                            this,
                            it.poster,
                            img_detail_movie
                        )
                    } else {
                        img_detail_movie.setImageResource(R.drawable.ic_no_image_available)
                        img_detail_movie.setBackgroundColor(Color.GRAY)
                    }

                    // Check if backdrop path not null
                    if (it.backdrop != null) {
                        setImage(
                            this,
                            it.backdrop,
                            img_detail_movie_highlight,
                            Constants.ENDPOINT_POSTER_SIZE_W780
                        )
                    } else {
                        img_detail_movie_highlight.setImageResource(R.drawable.ic_no_image_available)
                        img_detail_movie_highlight.setBackgroundColor(Color.GRAY)
                    }

                    when (validateLongData(it.genre!!.size)) {
                        0 -> tv_detail_genre.text = resources.getString(R.string.empty_data)
                        1 -> tv_detail_genre.text = it.genre!![0].name
                        2 -> {
                            var i = 0
                            while (i < it.genre!!.size) {
                                if (i == it.genre!!.size - 1) {
                                    tv_detail_genre.append(it.genre!![i].name)
                                    break
                                }
                                tv_detail_genre.append(it.genre!![i].name + ", ")
                                i++
                            }
                        }
                    }

                    when (validateLongData(it.created_by!!.size)) {
                        0 -> tv_detail_director.text = resources.getString(R.string.empty_data)
                        1 -> tv_detail_director.text = it.created_by!![0].name
                        2 -> {
                            var j = 0
                            while (j < it.created_by!!.size) {
                                if (j == it.created_by!!.size - 1) {
                                    tv_detail_director.append(it.created_by!![j].name)
                                    break
                                }
                                tv_detail_director.append(it.created_by!![j].name + ", ")
                                j++
                            }
                        }
                    }

                    when (validateLongData(it.runtime!!.size)) {
                        0 -> tv_detail_runtime.text = resources.getString(R.string.empty_data)
                        1 -> tv_detail_runtime.text = resources.getString(R.string.minutess, it.runtime!![0])
                        2 -> {
                            var h = 0
                            while (h < it.runtime!!.size) {
                                if (h == it.runtime!!.size - 1) {
                                    tv_detail_runtime.append(
                                        it.runtime!![h].toString() + resources.getString(
                                            R.string.minutes
                                        )
                                    )
                                    break
                                }
                                tv_detail_runtime.append(
                                    it.runtime!![h].toString() + resources.getString(
                                        R.string.minutes
                                    ) + ", "
                                )
                                h++
                            }
                        }
                    }

                })

                tvShowViewModel.getTvShowState().observer(this, Observer {
                    handleUIState(null, it, id)
                })

            } else {
                // Error page not found
                emptyStateErrorPage()
                empty_state_detail.visibility = View.VISIBLE
            }
        } else {
            // Error page not found
            emptyStateErrorPage()
            empty_state_detail.visibility = View.VISIBLE
        }


    }

    // set empty state error page
    private fun emptyStateErrorPage() {
        img_empty_state.setImageResource(R.drawable.ic_empty_state_error)
        btn_reconnect.visibility = View.GONE
        img_empty_state.contentDescription =
            resources.getString(R.string.empty_state_desc_error_page)
        title_empty_state.text = resources.getString(R.string.empty_state_title_error_page)
        desc_empty_state.text = resources.getString(R.string.empty_state_desc_error_page)
    }

    private fun minuteToHour(minutes: Int?): String {
        if (minutes != null && minutes > 60) {
            val hour = minutes / 60
            val minute = minutes % 60

            return hour.toString() + resources.getString(R.string.hours) + " " + minute.toString() + resources.getString(
                R.string.minutes
            )
        } else if (minutes != null && minutes <= 60) {
            return minutes.toString() + resources.getString(R.string.minutes)
        } else {
            return resources.getString(R.string.empty_data)
        }

    }

    private fun validateLongData(dataSize: Int?): Int {
        if (dataSize != null && dataSize == 1) {
            return 1 // 1 == only 1 data
        } else if (dataSize != null && dataSize >= 2) {
            return 2 // 2 == more than 1 data
        } else
            return 0 // Empty data
    }

    private fun setImage(
        context: Context,
        url: String?,
        imageView: ImageView,
        size: String = Constants.ENDPOINT_POSTER_SIZE_W185
    ) {
        Glide.with(context)
            .load(Constants.API_IMAGE_ENDPOINT + size + url)
            .into(imageView)
    }

    private fun handleUIState(itMovie: MovieState?, itTvShow: TvShowState?, id: Int? = 0) {

        if (itTvShow == null) {
            when (itMovie) {
                is MovieState.IsLoading -> {
                    isLoading(itMovie.state)
                }
                is MovieState.Error -> {
                    isLoading(false)
                    empty_state_detail.visibility = View.VISIBLE
                    btn_reconnect.setOnClickListener {
                        movieViewModel.fetchMovieDetail(id!!)
                        movieViewModel.fetchMovieCrew(id)
                    }
                }
                is MovieState.IsSuccess -> {
                    empty_state_detail.visibility = View.GONE
                }
            }
        } else {
            when (itTvShow) {
                is TvShowState.IsLoading -> {
                    isLoading(itTvShow.state)
                }
                is TvShowState.Error -> {
                    isLoading(false)
                    empty_state_detail.visibility = View.VISIBLE
                    btn_reconnect.setOnClickListener {
                        tvShowViewModel.fetchTvShowDetail(id!!)
                    }
                }
                is TvShowState.IsSuccess -> {
                    empty_state_detail.visibility = View.GONE
                }
            }
        }

    }

    private fun isLoading(state: Boolean) {
        if (state) {

            loading_detail.visibility = View.VISIBLE
        } else {
            loading_detail.visibility = View.GONE
        }
    }
}

