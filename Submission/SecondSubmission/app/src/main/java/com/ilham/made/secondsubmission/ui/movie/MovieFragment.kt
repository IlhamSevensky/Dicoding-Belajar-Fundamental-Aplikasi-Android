package com.ilham.made.secondsubmission.ui.movie


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilham.made.secondsubmission.R
import com.ilham.made.secondsubmission.adapter.MovieAdapter
import com.ilham.made.secondsubmission.model.ModelData
import kotlinx.android.synthetic.main.fragment_movie.*


/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : Fragment() {

    private val list = ArrayList<ModelData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_movie.setHasFixedSize(true)

        list.addAll(getListMovie())
        showRecyclerList()

    }

    private fun showRecyclerList() {
        rv_movie.layoutManager = LinearLayoutManager(view?.context)
        val listMovieAdapter = MovieAdapter(list)
        rv_movie.adapter = listMovieAdapter
    }

    private fun getListMovie(): ArrayList<ModelData> {
        val movieName = resources.getStringArray(R.array.data_movie_name)
        val movieDescription = resources.getStringArray(R.array.data_movie_description)
        val moviePoster = resources.obtainTypedArray(R.array.data_movie_photo)

        val movieDirector = resources.getStringArray(R.array.data_movie_director)
        val movieDuration = resources.getStringArray(R.array.data_movie_runtime)
        val movieGenre = resources.getStringArray(R.array.data_movie_genre)
        val movieImagePreview = resources.obtainTypedArray(R.array.data_movie_photo_highlight)

        val listMovie = ArrayList<ModelData>()
        for (position in movieName.indices) {
            val movie = ModelData(
                movieName[position],
                movieDescription[position],
                moviePoster.getResourceId(position, -1),
                movieImagePreview.getResourceId(position, -1),
                movieDirector[position],
                movieDuration[position],
                movieGenre[position]
            )
            listMovie.add(movie)
        }
        return listMovie
    }
}
