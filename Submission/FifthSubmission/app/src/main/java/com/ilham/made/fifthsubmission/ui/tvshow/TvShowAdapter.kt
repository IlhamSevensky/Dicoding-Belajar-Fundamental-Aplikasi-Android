package com.ilham.made.fifthsubmission.ui.tvshow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ilham.made.fifthsubmission.R
import com.ilham.made.fifthsubmission.data.db.favorite.tvshow.getFavoriteTvShowDatabase
import com.ilham.made.fifthsubmission.data.repository.FavoriteTvShowRepository
import com.ilham.made.fifthsubmission.models.TvShowModel
import com.ilham.made.fifthsubmission.utils.Constants
import com.ilham.made.fifthsubmission.utils.Helper
import kotlinx.android.synthetic.main.item_row_data.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TvShowAdapter(private var tvshows: MutableList<TvShowModel>) :
    RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: TvShowModel)
    }

    inner class TvShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tvshow: TvShowModel) {
            with(itemView) {
                lb_detail_release_date.text = resources.getString(R.string.label_first_air_date)
                val favTvShowRepository =
                    FavoriteTvShowRepository(
                        getFavoriteTvShowDatabase(this.context)
                    )

                if (favTvShowRepository.checkFavorite(tvshow.tv_show_id.toString()) > 0) {
                    btn_favorite.setImageResource(R.drawable.ic_favorite_true)
                } else {
                    btn_favorite.setImageResource(R.drawable.ic_favorite_false)
                }

                tv_item_id.text = tvshow.tv_show_id.toString()
                tv_movie_title.text = tvshow.title
                tv_description.text = tvshow.overview

                when(tvshow.first_air_date){
                    null -> tv_detail_release_date.text = resources.getString(R.string.empty_data)
                    else -> tv_detail_release_date.text = tvshow.first_air_date
                }

                when(Helper.isImagePathAvailable(tvshow.poster)){
                    true -> Helper.setImage(
                        context,
                        img_movie,
                        Constants.API_IMAGE_ENDPOINT + Constants.ENDPOINT_POSTER_SIZE_W185 + tvshow.poster
                    )

                    false -> Helper.setImageNotAvailable(img_movie)
                }

                setOnClickListener {
                    onItemClickCallback?.onItemClicked(tvshow)
                }

                btn_favorite.setOnClickListener {
                    if (favTvShowRepository.checkFavorite(tvshow.tv_show_id.toString()) > 0) {
                        favTvShowRepository.removeFavorite(tvshow.tv_show_id.toString())
                        btn_favorite.setImageResource(R.drawable.ic_favorite_false)
                    } else {
                        GlobalScope.launch(Dispatchers.IO) {
                            favTvShowRepository.addFavoriteTvShow(tvshow)
                        }
                        Toast.makeText(
                                context,
                                resources.getString(R.string.toast_added_to_favorite, tvshow.title),
                                Toast.LENGTH_SHORT
                            )
                            .show()
                        btn_favorite.setImageResource(R.drawable.ic_favorite_true)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowViewHolder {
        return TvShowViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_data, parent, false)
        )
    }

    override fun getItemCount(): Int = tvshows.size

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) =
        holder.bind(tvshows[position])

    fun setTvShows(tvs: List<TvShowModel>) {
        tvshows.clear()
        tvshows.addAll(tvs)
        notifyDataSetChanged()
    }
}