package com.ilham.made.consumerfavorite.ui.movie

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ilham.made.consumerfavorite.R
import com.ilham.made.consumerfavorite.models.MovieModel
import com.ilham.made.consumerfavorite.utils.Constants
import com.ilham.made.consumerfavorite.utils.Helper
import kotlinx.android.synthetic.main.item_row_data.view.*


class MovieAdapter(private var movies: MutableList<MovieModel>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private lateinit var uriWithId: Uri
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: MovieModel)
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: MovieModel) {
            with(itemView) {

                uriWithId =
                    Uri.parse(Constants.CONTENT_URI.toString() + "/" + movie.movie_id)

                val cursor = context.contentResolver?.query(uriWithId, null,null, null, null)

                if (cursor?.count!! > 0) {
                    btn_favorite.setImageResource(R.drawable.ic_favorite_true)
                } else {
                    btn_favorite.setImageResource(R.drawable.ic_favorite_false)
                }

                tv_item_id.text = movie.movie_id.toString()
                tv_movie_title.text = movie.title
                tv_description.text = movie.overview

                when(movie.release_date){
                    null -> tv_detail_release_date.text = resources.getString(R.string.empty_data)
                    else -> tv_detail_release_date.text = movie.release_date
                }

                when(Helper.isImagePathAvailable(movie.poster)){
                    true -> Helper.setImage(
                        context,
                        img_movie,
                        Constants.API_IMAGE_ENDPOINT + Constants.ENDPOINT_POSTER_SIZE_W185 + movie.poster
                    )

                    false -> Helper.setImageNotAvailable(img_movie)
                }

                setOnClickListener {
                    onItemClickCallback?.onItemClicked(movie)
                }

                btn_favorite.setOnClickListener {
                    uriWithId =
                        Uri.parse(Constants.CONTENT_URI.toString() + "/" + movie.movie_id)

                    val cursors = it.context.contentResolver.query(uriWithId, null,null, null, null)
                    Log.d("<L<L", cursors?.count.toString())
                    if (cursors?.count!! > 0){
                        it.context.contentResolver?.delete(uriWithId, null, null)
                        btn_favorite.setImageResource(R.drawable.ic_favorite_false)
                    } else {
                        val values = ContentValues()
                        values.put(Constants.COLUMN_MOVIE_ID, movie.movie_id)
                        values.put(Constants.COLUMN_TITLE, movie.title)
                        values.put(Constants.COLUMN_OVERVIEW, movie.overview)
                        values.put(Constants.COLUMN_RELEASE_DATE, movie.release_date)
                        values.put(Constants.COLUMN_POSTER, movie.poster)
                        values.put(Constants.COLUMN_BACKDROP, movie.backdrop)
                        context?.contentResolver?.insert(Constants.CONTENT_URI, values)
                        Toast.makeText(
                                context,
                                resources.getString(R.string.toast_added_to_favorite, movie.title),
                                Toast.LENGTH_SHORT
                            )
                            .show()
                        btn_favorite.setImageResource(R.drawable.ic_favorite_true)
                    }

                    Helper.updateFavoriteMovieWidget(it.context)

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_data, parent, false)
        )
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) =
        holder.bind(movies[position])

    fun setMovies(m: List<MovieModel>) {
        movies.clear()
        movies.addAll(m)
        notifyDataSetChanged()
    }
}