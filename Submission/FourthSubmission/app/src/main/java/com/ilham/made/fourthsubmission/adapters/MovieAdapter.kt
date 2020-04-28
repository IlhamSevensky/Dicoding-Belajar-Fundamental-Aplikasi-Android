package com.ilham.made.fourthsubmission.adapters

import android.content.ContentValues
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilham.made.fourthsubmission.R
import com.ilham.made.fourthsubmission.database.DatabaseContract
import com.ilham.made.fourthsubmission.database.DatabaseContract.FavoriteColumns.Companion.MOVIE_ID
import com.ilham.made.fourthsubmission.database.MovieFavoriteHelper
import com.ilham.made.fourthsubmission.models.MovieModel
import com.ilham.made.fourthsubmission.utils.Constants
import kotlinx.android.synthetic.main.item_row_data.view.*

class MovieAdapter(private var movies: MutableList<MovieModel>) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private lateinit var movieFavoriteHelper: MovieFavoriteHelper

    // ItemClickCallback for MovieAdapter (interface untuk Onclick pada fragment, bukan di adapternya)
    private var onItemClickCallback: OnItemClickCallback? = null

    // Setter ItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    // Interface ItemClickCallback
    interface OnItemClickCallback {
        fun onItemClicked(data: MovieModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row_data,
                parent,
                false
            )
        )
    }

    fun setMovies(m: List<MovieModel>) {
        movies.clear()
        movies.addAll(m)
        notifyDataSetChanged()
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(movies[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: MovieModel) {
            with(itemView) {

                // check if movie favorite or not
                movieFavoriteHelper = MovieFavoriteHelper.getInstance(this.context)
                movieFavoriteHelper.open()

                val cursors = movieFavoriteHelper.queryById(movie.id.toString())

                if (cursors.count > 0){
                    btn_favorite.setImageResource(R.drawable.ic_favorite_true)
                }

                movieFavoriteHelper.close()

                // Set view with items
                tv_item_id.text = movie.id.toString()
                tv_movie_title.text = movie.title
                tv_description.text = movie.overview

                if (movie.poster != null){
                    Glide.with(context)
                        .load(Constants.API_IMAGE_ENDPOINT + Constants.ENDPOINT_POSTER_SIZE_W185 + movie.poster)
                        .into(img_movie)
                } else {
                    img_movie.setImageResource(R.drawable.ic_no_image_available)
                    img_movie.setBackgroundColor(Color.GRAY)
                }

                setOnClickListener {
                    onItemClickCallback?.onItemClicked(movie)
                }

                btn_favorite.setOnClickListener {
                    movieFavoriteHelper.open()

                    val cursor = movieFavoriteHelper.queryById(movie.id.toString())
                    if (cursor.count > 0){
                        movieFavoriteHelper.deleteById(movie.id.toString())
                        btn_favorite.setImageResource(R.drawable.ic_favorite_false)
                    }else {
                        val values = ContentValues()
                        values.put(MOVIE_ID, movie.id)
                        values.put(DatabaseContract.FavoriteColumns.TITLE, movie.title)
                        values.put(DatabaseContract.FavoriteColumns.OVERVIEW, movie.overview)
                        values.put(DatabaseContract.FavoriteColumns.POSTER, movie.poster)
                        values.put(DatabaseContract.FavoriteColumns.BACKDROP, movie.backdrop)
                        Log.d("INSERT", values.toString())
                        movieFavoriteHelper.insert(values)
                        btn_favorite.setImageResource(R.drawable.ic_favorite_true)
                    }

                    movieFavoriteHelper.close()
                }
            }
        }
    }
}