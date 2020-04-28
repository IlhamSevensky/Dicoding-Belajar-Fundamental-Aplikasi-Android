package com.ilham.made.fourthsubmission.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilham.made.fourthsubmission.R
import com.ilham.made.fourthsubmission.database.TvShowFavoriteHelper
import com.ilham.made.fourthsubmission.models.FavoriteTvShowModel
import com.ilham.made.fourthsubmission.utils.Constants
import kotlinx.android.synthetic.main.item_row_data.view.*

class TvShowFavoriteAdapter(private var tv_shows: MutableList<FavoriteTvShowModel>) : RecyclerView.Adapter<TvShowFavoriteAdapter.TvShowFavViewHolder>() {

    private lateinit var tvShowFavoriteHelper: TvShowFavoriteHelper

    // ItemClickCallback for MovieAdapter (interface untuk Onclick pada fragment, bukan di adapternya)
    private var onItemClickCallback: OnItemClickCallback? = null

    // Setter ItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    // Interface ItemClickCallback
    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteTvShowModel)
        fun onFavoriteClicked(data: Int)
    }

    fun setTvShow(fav: List<FavoriteTvShowModel>) {
        tv_shows.clear()
        tv_shows.addAll(fav)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowFavViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_data, parent, false)
        return TvShowFavViewHolder(view)
    }

    override fun onBindViewHolder(holder: TvShowFavViewHolder, position: Int) = holder.bind(tv_shows[position])

    override fun getItemCount(): Int = tv_shows.size

    inner class TvShowFavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tv_show: FavoriteTvShowModel) {
            with(itemView) {

                // check if movie favorite or not
                tvShowFavoriteHelper = TvShowFavoriteHelper.getInstance(this.context)
                tvShowFavoriteHelper.open()

                val cursors = tvShowFavoriteHelper.queryById(tv_show.tvshow_id!!)

                if (cursors.count > 0){
                    btn_favorite.setImageResource(R.drawable.ic_favorite_true)
                }

                tvShowFavoriteHelper.close()

                tv_item_id.text = tv_show.tvshow_id
                tv_movie_title.text = tv_show.title
                tv_description.text = tv_show.overview

                if (tv_show.poster != null){
                    Glide.with(context)
                        .load(Constants.API_IMAGE_ENDPOINT + Constants.ENDPOINT_POSTER_SIZE_W185 + tv_show.poster)
                        .into(img_movie)
                } else {
                    img_movie.setImageResource(R.drawable.ic_no_image_available)
                    img_movie.setBackgroundColor(Color.GRAY)
                }

                setOnClickListener {
                    onItemClickCallback?.onItemClicked(tv_show)
                }

                btn_favorite.setOnClickListener {
                    tvShowFavoriteHelper.open()

                    val cursor = tvShowFavoriteHelper.queryById(tv_show.tvshow_id.toString())
                    if (cursor.count > 0){
                        tvShowFavoriteHelper.deleteById(tv_show.tvshow_id.toString())
                        btn_favorite.setImageResource(R.drawable.ic_favorite_false)

                        val lastData = tvShowFavoriteHelper.queryAll()
                        onItemClickCallback?.onFavoriteClicked(lastData.count)
                    }

                    tvShowFavoriteHelper.close()
                }
            }
        }
    }
}