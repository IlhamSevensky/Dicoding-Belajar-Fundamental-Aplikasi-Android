package com.ilham.made.fifthsubmission.ui.favorite.tvshow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ilham.made.fifthsubmission.R
import com.ilham.made.fifthsubmission.data.db.favorite.tvshow.getFavoriteTvShowDatabase
import com.ilham.made.fifthsubmission.data.repository.FavoriteTvShowRepository
import com.ilham.made.fifthsubmission.models.FavoriteTvShowModel
import com.ilham.made.fifthsubmission.utils.Constants
import com.ilham.made.fifthsubmission.utils.Helper
import kotlinx.android.synthetic.main.item_row_data.view.*

class FavoriteTvShowAdapter(private var favTvShows: MutableList<FavoriteTvShowModel>) :
    RecyclerView.Adapter<FavoriteTvShowAdapter.FavoriteTvShowViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteTvShowModel)
        fun onFavoriteClicked(data: FavoriteTvShowModel)
    }

    inner class FavoriteTvShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favTvShow: FavoriteTvShowModel) {
            with(itemView) {
                lb_detail_release_date.text = resources.getString(R.string.label_first_air_date)
                val favTvShowRepository =
                    FavoriteTvShowRepository(
                        getFavoriteTvShowDatabase(this.context)
                    )

                if (favTvShowRepository.checkFavorite(favTvShow.tvshow_id.toString()) > 0) {
                    btn_favorite.setImageResource(R.drawable.ic_favorite_true)
                } else {
                    btn_favorite.setImageResource(R.drawable.ic_favorite_false)
                }

                tv_item_id.text = favTvShow.tvshow_id
                tv_movie_title.text = favTvShow.title
                tv_description.text = favTvShow.overview

                when (favTvShow.first_air_date) {
                    null -> tv_detail_release_date.text = resources.getString(R.string.empty_data)
                    else -> tv_detail_release_date.text = favTvShow.first_air_date
                }

                when (Helper.isImagePathAvailable(favTvShow.poster)) {
                    true -> Helper.setImage(
                        context,
                        img_movie,
                        Constants.API_IMAGE_ENDPOINT + Constants.ENDPOINT_POSTER_SIZE_W185 + favTvShow.poster
                    )

                    false -> Helper.setImageNotAvailable(img_movie)
                }

                setOnClickListener {
                    onItemClickCallback?.onItemClicked(favTvShow)
                }

                btn_favorite.setOnClickListener {
                    favTvShowRepository.removeFavorite(favTvShow.tvshow_id.toString())
                    onItemClickCallback?.onFavoriteClicked(favTvShow)
                    favTvShows.remove(favTvShow)
                    notifyItemRemoved(adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteTvShowViewHolder {
        return FavoriteTvShowViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_data, parent, false)
        )
    }

    override fun getItemCount(): Int = favTvShows.size

    override fun onBindViewHolder(holder: FavoriteTvShowViewHolder, position: Int) =
        holder.bind(favTvShows[position])

    fun setFavTvShows(ftv: List<FavoriteTvShowModel>) {
        favTvShows.clear()
        favTvShows.addAll(ftv)
        notifyDataSetChanged()
    }
}