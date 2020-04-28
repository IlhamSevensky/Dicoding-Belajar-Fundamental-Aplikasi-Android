package com.ilham.made.consumerfavorite.ui.detail

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ilham.made.consumerfavorite.R
import com.ilham.made.consumerfavorite.data.db.favorite.tvshow.getFavoriteTvShowDatabase
import com.ilham.made.consumerfavorite.data.repository.FavoriteTvShowRepository
import com.ilham.made.consumerfavorite.data.repository.MovieState
import com.ilham.made.consumerfavorite.data.repository.TvShowState
import com.ilham.made.consumerfavorite.utils.Constants
import com.ilham.made.consumerfavorite.utils.Helper
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.empty_state.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TYPE = "extra_type"
        const val EXTRA_DATA = "extra_data"
        private val TAG = DetailActivity::class.java.simpleName
    }

    private lateinit var uriWithId: Uri

    private val detailViewModel: DetailViewModel by lazy {
        val activity = requireNotNull(this@DetailActivity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        ViewModelProvider(
            this@DetailActivity,
            DetailViewModel.Factory(activity.application)
        )
            .get(DetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setupToolbar()

        val type = intent.getStringExtra(EXTRA_TYPE)
        val id = intent.getIntExtra(EXTRA_DATA, 0)

        if (type != null) {
            if (type.equals(Constants.ACTIVITY_TYPE_MOVIE, ignoreCase = true)) {

                detailViewModel.getMovieInformation(id)

                detailViewModel.getMovieDetail()
                    ?.observe(this@DetailActivity, Observer { movieDetail ->
                        if (movieDetail == null) {
                            scrollViewDetail.visibility = View.GONE
                            empty_state_detail.visibility = View.VISIBLE
                        } else {
                            uriWithId =
                                Uri.parse(Constants.CONTENT_URI.toString() + "/" + movieDetail.movie_id)

                            tv_detail_title.text = movieDetail.title
                            tv_detail_desc.text = movieDetail.overview
                            tv_detail_runtime.text = minuteToHour(movieDetail.runtime)

                            val cursor = contentResolver?.query(uriWithId, null,null, null, null)

                            if (cursor?.count!! > 0){
                                btn_favorite_detail.setImageResource(R.drawable.ic_favorite_true)
                            } else {
                                btn_favorite_detail.setImageResource(R.drawable.ic_favorite_false)
                            }

                            when (Helper.isImagePathAvailable(movieDetail.poster)) {
                                true -> Helper.setImage(
                                    this@DetailActivity,
                                    img_detail_movie,
                                    Constants.API_IMAGE_ENDPOINT + Constants.ENDPOINT_POSTER_SIZE_W185 + movieDetail.poster
                                )


                                false -> Helper.setImageNotAvailable(img_detail_movie)
                            }

                            when (Helper.isImagePathAvailable(movieDetail.backdrop)) {
                                true -> Helper.setImage(
                                    this@DetailActivity,
                                    img_detail_movie_highlight,
                                    Constants.API_IMAGE_ENDPOINT + Constants.ENDPOINT_POSTER_SIZE_W780 + movieDetail.backdrop
                                )

                                false -> Helper.setImageNotAvailable(img_detail_movie_highlight)
                            }

                            when (validateLongData(movieDetail.genres!!.size)) {
                                0 -> tv_detail_genre.text = resources.getString(R.string.empty_data)
                                1 -> tv_detail_genre.text = movieDetail.genres[0].name
                                2 -> {
                                    var i = 0
                                    while (i < movieDetail.genres.size) {
                                        if (i == movieDetail.genres.size - 1) {
                                            tv_detail_genre.append(movieDetail.genres[i].name)
                                            break
                                        }
                                        tv_detail_genre.append(movieDetail.genres[i].name + ", ")
                                        i++
                                    }
                                }
                            }

                            btn_favorite_detail.setOnClickListener {

                                if (cursor.count > 0){
                                    contentResolver.delete(uriWithId, null, null)
                                    btn_favorite_detail.setImageResource(R.drawable.ic_favorite_false)
                                } else {
                                    val values = ContentValues()
                                    values.put(Constants.COLUMN_MOVIE_ID, movieDetail.movie_id)
                                    values.put(Constants.COLUMN_TITLE, movieDetail.title)
                                    values.put(Constants.COLUMN_OVERVIEW, movieDetail.overview)
                                    values.put(Constants.COLUMN_RELEASE_DATE, movieDetail.release_date)
                                    values.put(Constants.COLUMN_POSTER, movieDetail.poster)
                                    values.put(Constants.COLUMN_BACKDROP, movieDetail.backdrop)
                                    contentResolver.insert(Constants.CONTENT_URI, values)
                                    Toast.makeText(
                                            this@DetailActivity,
                                            resources.getString(R.string.toast_added_to_favorite, movieDetail.title),
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                    btn_favorite_detail.setImageResource(R.drawable.ic_favorite_true)
                                }

                                Helper.updateFavoriteMovieWidget(it.context)
                            }
                        }

                    })

                detailViewModel.getMovieCrew()?.observe(this@DetailActivity, Observer { movieCrew ->
                    if (movieCrew == null) {
                        scrollViewDetail.visibility = View.GONE
                        empty_state_detail.visibility = View.VISIBLE
                    } else {
                        var j = 0
                        val director = StringBuilder()
                        // Check the amount of data crew with job as a Director
                        while (j < movieCrew.director!!.size) {
                            if (movieCrew.director[j].job.equals(Constants.JOB_DIRECTOR)) {
                                director.append(movieCrew.director[j].name + ", ")
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
                    }
                })

                detailViewModel.movieState.observe(this@DetailActivity, Observer {
                    handleUIState(it, null, id)
                })

            } else {
                lb_detail_director.text = resources.getString(R.string.label_creator)
                val favTvShowRepository =
                    FavoriteTvShowRepository(
                        getFavoriteTvShowDatabase(this@DetailActivity)
                    )

                detailViewModel.getTvShowInformation(id)

                detailViewModel.getTvShowDetail()
                    ?.observe(this@DetailActivity, Observer { tvshowDetail ->
                        if (tvshowDetail == null) {
                            scrollViewDetail.visibility = View.GONE
                            empty_state_detail.visibility = View.VISIBLE
                        } else {
                            tv_detail_title.text = tvshowDetail.title
                            tv_detail_desc.text = tvshowDetail.overview

                            if (favTvShowRepository.checkFavorite(tvshowDetail.tv_show_id.toString()) > 0) {
                                btn_favorite_detail.setImageResource(R.drawable.ic_favorite_true)
                            } else {
                                btn_favorite_detail.setImageResource(R.drawable.ic_favorite_false)
                            }

                            when (Helper.isImagePathAvailable(tvshowDetail.poster)) {
                                true -> Helper.setImage(
                                    this@DetailActivity,
                                    img_detail_movie,
                                    Constants.API_IMAGE_ENDPOINT + Constants.ENDPOINT_POSTER_SIZE_W185 + tvshowDetail.poster
                                )

                                false -> Helper.setImageNotAvailable(img_detail_movie)
                            }

                            when (Helper.isImagePathAvailable(tvshowDetail.backdrop)) {
                                true -> Helper.setImage(
                                    this@DetailActivity,
                                    img_detail_movie_highlight,
                                    Constants.API_IMAGE_ENDPOINT + Constants.ENDPOINT_POSTER_SIZE_W780 + tvshowDetail.backdrop
                                )

                                false -> Helper.setImageNotAvailable(img_detail_movie_highlight)
                            }

                            when (validateLongData(tvshowDetail.genre!!.size)) {
                                0 -> tv_detail_genre.text = resources.getString(R.string.empty_data)
                                1 -> tv_detail_genre.text = tvshowDetail.genre[0].name
                                2 -> {
                                    var i = 0
                                    while (i < tvshowDetail.genre.size) {
                                        if (i == tvshowDetail.genre.size - 1) {
                                            tv_detail_genre.append(tvshowDetail.genre[i].name)
                                            break
                                        }
                                        tv_detail_genre.append(tvshowDetail.genre[i].name + ", ")
                                        i++
                                    }
                                }
                            }

                            when (validateLongData(tvshowDetail.created_by!!.size)) {
                                0 -> tv_detail_director.text =
                                    resources.getString(R.string.empty_data)
                                1 -> tv_detail_director.text = tvshowDetail.created_by[0].name
                                2 -> {
                                    var j = 0
                                    while (j < tvshowDetail.created_by.size) {
                                        if (j == tvshowDetail.created_by.size - 1) {
                                            tv_detail_director.append(tvshowDetail.created_by[j].name)
                                            break
                                        }
                                        tv_detail_director.append(tvshowDetail.created_by[j].name + ", ")
                                        j++
                                    }
                                }
                            }

                            when (validateLongData(tvshowDetail.runtime!!.size)) {
                                0 -> tv_detail_runtime.text =
                                    resources.getString(R.string.empty_data)
                                1 -> tv_detail_runtime.text =
                                    resources.getString(R.string.minutess, tvshowDetail.runtime[0])
                                2 -> {
                                    var h = 0
                                    while (h < tvshowDetail.runtime.size) {
                                        if (h == tvshowDetail.runtime.size - 1) {
                                            tv_detail_runtime.append(
                                                tvshowDetail.runtime[h].toString() + resources.getString(
                                                    R.string.minutes
                                                )
                                            )
                                            break
                                        }
                                        tv_detail_runtime.append(
                                            tvshowDetail.runtime[h].toString() + resources.getString(
                                                R.string.minutes
                                            ) + ", "
                                        )
                                        h++
                                    }
                                }
                            }

                            btn_favorite_detail.setOnClickListener {
                                if (favTvShowRepository.checkFavorite(tvshowDetail.tv_show_id.toString()) > 0) {
                                    favTvShowRepository.removeFavorite(tvshowDetail.tv_show_id.toString())
                                    btn_favorite_detail.setImageResource(R.drawable.ic_favorite_false)
                                } else {
                                    GlobalScope.launch(Dispatchers.IO) {
                                        favTvShowRepository.addFavoriteTvShow(tvshowDetail)
                                    }
                                    Toast.makeText(
                                            this@DetailActivity,
                                            resources.getString(R.string.toast_added_to_favorite, tvshowDetail.title),
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                    btn_favorite_detail.setImageResource(R.drawable.ic_favorite_true)
                                }
                            }

                        }

                    })

                detailViewModel.tvshowState.observe(this@DetailActivity, Observer {
                    handleUIState(null, it, id)
                })

            }
        } else {
            scrollViewDetail.visibility = View.GONE
            emptyStateErrorPage()
        }
    }

    private fun setupToolbar() {
        toolbar_movie_detail.elevation = 8F
        toolbar_movie_detail.title = resources.getString(R.string.title_toolbar_detail)
        toolbar_movie_detail.navigationIcon =
            ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_24dp)
        toolbar_movie_detail.setNavigationOnClickListener { finish() }
    }

    private fun minuteToHour(minutes: Int?): String {
        return if (minutes != null && minutes > 60) {
            val hour = minutes / 60
            val minute = minutes % 60

            hour.toString() + resources.getString(R.string.hours) + " " + minute.toString() + resources.getString(
                R.string.minutes
            )
        } else if (minutes != null && minutes <= 60) {
            minutes.toString() + resources.getString(R.string.minutes)
        } else {
            resources.getString(R.string.empty_data)
        }

    }

    /**
     * Check the amount of data (genres)
     * @return is amount of data
     * @return 0 = null
     * @return 1 = 1
     * @return 2 = more than 1
     */
    private fun validateLongData(dataSize: Int?): Int {
        return if (dataSize != null && dataSize == 1) {
            1
        } else if (dataSize != null && dataSize >= 2) {
            2
        } else
            0
    }

    private fun handleUIState(itMovie: MovieState?, itTvShow: TvShowState?, id: Int = 0) {

        if (itTvShow == null) {
            when (itMovie) {
                is MovieState.IsLoading -> {
                    isLoading(itMovie.state)
                }
                is MovieState.Error -> {
                    isLoading(false)
                    Log.d(TAG, "Error : ${itMovie.error}")
                    scrollViewDetail.visibility = View.GONE
                    empty_state_detail.visibility = View.VISIBLE
                    btn_reconnect.setOnClickListener {
                        detailViewModel.getMovieInformation(id)
                    }
                }
                is MovieState.IsSuccess -> {
                    scrollViewDetail.visibility = View.VISIBLE
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
                    scrollViewDetail.visibility = View.GONE
                    empty_state_detail.visibility = View.VISIBLE
                    btn_reconnect.setOnClickListener {
                        detailViewModel.getTvShowInformation(id)
                    }
                }
                is TvShowState.IsSuccess -> {
                    scrollViewDetail.visibility = View.VISIBLE
                    empty_state_detail.visibility = View.GONE
                }
            }
        }

    }

    private fun isLoading(state: Boolean) {
        if (state) {
            scrollViewDetail.visibility = View.GONE
            loading_detail.visibility = View.VISIBLE
        } else {
            scrollViewDetail.visibility = View.VISIBLE
            loading_detail.visibility = View.GONE
        }
    }

    private fun emptyStateErrorPage() {
        img_empty_state.setImageResource(R.drawable.ic_empty_state_error)
        btn_reconnect.visibility = View.GONE
        img_empty_state.contentDescription =
            resources.getString(R.string.empty_state_desc_error_page)
        title_empty_state.text = resources.getString(R.string.empty_state_title_error_page)
        desc_empty_state.text = resources.getString(R.string.empty_state_desc_error_page)
    }
}
