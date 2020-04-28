package com.ilham.made.consumerfavorite.ui.favorite.movie

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ilham.made.consumerfavorite.R
import com.ilham.made.consumerfavorite.models.FavoriteMovieModel
import com.ilham.made.consumerfavorite.utils.Constants
import com.ilham.made.consumerfavorite.utils.Helper
import kotlinx.android.synthetic.main.item_row_data.view.*

class FavoriteMovieAdapter(private var favMovies: MutableList<FavoriteMovieModel>) :
    RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewHolder>() {

    private lateinit var uriWithId: Uri
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteMovieModel)
    }

    inner class FavoriteMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favMovie: FavoriteMovieModel) {
            with(itemView) {

                uriWithId =
                    Uri.parse(Constants.CONTENT_URI.toString() + "/" + favMovie.movie_id)

                val cursor = context.contentResolver?.query(uriWithId, null,null, null, null)

                if (cursor?.count!! > 0) {
                    btn_favorite.setImageResource(R.drawable.ic_favorite_true)
                } else {
                    btn_favorite.setImageResource(R.drawable.ic_favorite_false)
                }

                tv_item_id.text = favMovie.movie_id
                tv_movie_title.text = favMovie.title
                tv_description.text = favMovie.overview

                when (favMovie.release_date) {
                    null -> tv_detail_release_date.text = resources.getString(R.string.empty_data)
                    else -> tv_detail_release_date.text = favMovie.release_date
                }

                when (Helper.isImagePathAvailable(favMovie.poster)) {
                    true ->
                        Helper.setImage(
                            context,
                            img_movie,
                            Constants.API_IMAGE_ENDPOINT + Constants.ENDPOINT_POSTER_SIZE_W185 + favMovie.poster
                        )

                    false -> Helper.setImageNotAvailable(img_movie)
                }

                setOnClickListener {
                    onItemClickCallback?.onItemClicked(favMovie)
                }

                btn_favorite.setOnClickListener {
                    context?.contentResolver?.delete(uriWithId, null, null)
                    Helper.updateFavoriteMovieWidget(it.context)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMovieViewHolder {
        return FavoriteMovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_data, parent, false)
        )
    }

    override fun getItemCount(): Int = favMovies.size

    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) =
        holder.bind(favMovies[position])

    fun setFavMovies(fm: List<FavoriteMovieModel>) {
        favMovies.clear()
        favMovies.addAll(fm)
        notifyDataSetChanged()
    }
}