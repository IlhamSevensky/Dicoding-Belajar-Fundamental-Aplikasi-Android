package com.ilham.made.firstsubmission

import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ilham.made.firstsubmission.Adapter.MovieAdapter
import com.ilham.made.firstsubmission.Model.MovieModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var movieAdapter: MovieAdapter
    private lateinit var movieName: Array<String>
    private lateinit var movieDesc: Array<String>
    private lateinit var movieImage: TypedArray
    private lateinit var movieDirector: Array<String>
    private lateinit var movieRuntime: Array<String>
    private lateinit var movieGenre: Array<String>
    private lateinit var movieImageHightlight: TypedArray
    private var movies = arrayListOf<MovieModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movieAdapter = MovieAdapter(this)
        lv_movie.adapter = movieAdapter

        prepare()
        addItem()

        lv_movie.setOnItemClickListener {_,_,position,_ ->
            val movie = MovieModel (
                movieName[position],
                movieDesc[position],
                movieImage.getResourceId(position, -1),
                movieDirector[position],
                movieRuntime[position],
                movieGenre[position],
                movieImageHightlight.getResourceId(position, -1)
            )
            val mIntent = Intent(this@MainActivity, DetailActivity::class.java)
                .putExtra(DetailActivity.EXTRA_MOVIE, movie)
            startActivity(mIntent)
        }
    }

    private fun prepare() {
        movieName = resources.getStringArray(R.array.data_movie_name)
        movieDesc = resources.getStringArray(R.array.data_movie_description)
        movieImage = resources.obtainTypedArray(R.array.data_movie_photo)
        movieDirector = resources.getStringArray(R.array.data_movie_director)
        movieRuntime = resources.getStringArray(R.array.data_movie_runtime)
        movieGenre = resources.getStringArray(R.array.data_movie_genre)
        movieImageHightlight = resources.obtainTypedArray(R.array.data_movie_photo_highlight)
    }

    private fun addItem() {
        for (position in movieName.indices) {
            val movie = MovieModel (
                movieName[position],
                movieDesc[position],
                movieImage.getResourceId(position, -1),
                movieDirector[position],
                movieRuntime[position],
                movieGenre[position],
                movieImageHightlight.getResourceId(position,-1)
            )
            movies.add(movie)
        }
        movieAdapter.movies = movies
    }
}
