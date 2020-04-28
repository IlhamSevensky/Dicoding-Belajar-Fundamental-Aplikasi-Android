package com.ilham.made.fourthsubmission.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilham.made.fourthsubmission.R
import com.ilham.made.fourthsubmission.database.MovieFavoriteHelper
import com.ilham.made.fourthsubmission.models.FavoriteMovieModel
import com.ilham.made.fourthsubmission.utils.Constants
import kotlinx.android.synthetic.main.item_row_data.view.*

class MovieFavoriteAdapter(private var movies: MutableList<FavoriteMovieModel>) : RecyclerView.Adapter<MovieFavoriteAdapter.MovieFavViewHolder>() {

    private lateinit var movieFavoriteHelper: MovieFavoriteHelper

    // ItemClickCallback for MovieAdapter (interface untuk Onclick pada fragment, bukan di adapternya)
    private var onItemClickCallback: OnItemClickCallback? = null

    // Setter ItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    // Interface ItemClickCallback
    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteMovieModel)
        fun onFavoriteClicked(data: Int)
    }

    fun setMovies(fav: List<FavoriteMovieModel>) {
        movies.clear()
        movies.addAll(fav)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieFavViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_data, parent, false)
        return MovieFavViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieFavViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    inner class MovieFavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(m: FavoriteMovieModel) {
            with(itemView) {

                // check if movie favorite or not
                movieFavoriteHelper = MovieFavoriteHelper.getInstance(this.context)
                movieFavoriteHelper.open()

                val cursors = movieFavoriteHelper.queryById(m.movie_id!!)

                if (cursors.count > 0){
                    btn_favorite.setImageResource(R.drawable.ic_favorite_true)
                }

                movieFavoriteHelper.close()

                tv_item_id.text = m.movie_id
                tv_movie_title.text = m.title
                tv_description.text = m.overview

                if (m.poster != null){
                    Glide.with(context)
                        .load(Constants.API_IMAGE_ENDPOINT + Constants.ENDPOINT_POSTER_SIZE_W185 + m.poster)
                        .into(img_movie)
                } else {
                    img_movie.setImageResource(R.drawable.ic_no_image_available)
                    img_movie.setBackgroundColor(Color.GRAY)
                }

                setOnClickListener {
                    onItemClickCallback?.onItemClicked(m)
                }

                btn_favorite.setOnClickListener {
                    movieFavoriteHelper.open()

                    val cursor = movieFavoriteHelper.queryById(m.movie_id.toString())
                    if (cursor.count > 0){
                        movieFavoriteHelper.deleteById(m.movie_id.toString())
                        btn_favorite.setImageResource(R.drawable.ic_favorite_false)

                        val lastData = movieFavoriteHelper.queryAll()
                        onItemClickCallback?.onFavoriteClicked(lastData.count)
                    }

                    movieFavoriteHelper.close()
                }
            }
        }
    }
}