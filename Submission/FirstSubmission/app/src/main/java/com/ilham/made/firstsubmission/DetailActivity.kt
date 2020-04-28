package com.ilham.made.firstsubmission

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.ilham.made.firstsubmission.Model.MovieModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MOVIE = "extra_movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        toolbar_movie_detail.title = resources.getString(R.string.title_toolbar_detail)
        toolbar_movie_detail.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp)
        toolbar_movie_detail.setNavigationOnClickListener { finish() }

        val movie = intent.getParcelableExtra(EXTRA_MOVIE) as MovieModel

        tv_detail_title.text = movie.movie_name
        tv_detail_desc.text = movie.movie_desc
        img_detail_movie.setImageResource(movie.movie_image)
        tv_detail_director.text = movie.movie_director
        tv_detail_runtime.text = movie.movie_runtime
        tv_detail_genre.text =movie.movie_genre
        img_detail_movie_highlight.setImageResource(movie.movie_image_highlight)
    }

}
