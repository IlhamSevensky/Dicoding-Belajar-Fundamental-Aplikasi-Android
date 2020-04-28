package com.ilham.made.thirdsubmission.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilham.made.thirdsubmission.R
import com.ilham.made.thirdsubmission.models.MovieModel
import com.ilham.made.thirdsubmission.utils.Constants
import kotlinx.android.synthetic.main.item_row_data.view.*

class MovieAdapter(private var movies: MutableList<MovieModel>) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

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
            }
        }
    }
}