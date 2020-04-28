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
import com.ilham.made.fourthsubmission.database.TvShowFavoriteHelper
import com.ilham.made.fourthsubmission.models.TvShowModel
import com.ilham.made.fourthsubmission.utils.Constants
import kotlinx.android.synthetic.main.item_row_data.view.*

class TvShowAdapter(private var tv_shows: MutableList<TvShowModel>) :
    RecyclerView.Adapter<TvShowAdapter.ViewHolder>() {

    private lateinit var tvShowFavoriteHelper: TvShowFavoriteHelper

    // ItemClickCallback for TVShowAdapter (interface untuk Onclick pada fragment, bukan di adapternya)
    private var onItemClickCallback: OnItemClickCallback? = null

    // Setter ItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    // Interface ItemClickCallback
    interface OnItemClickCallback {
        fun onItemClicked(data: TvShowModel)
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

    fun setTvShow(tv: List<TvShowModel>) {
        tv_shows.clear()
        tv_shows.addAll(tv)
        notifyDataSetChanged()
    }

    override fun getItemCount() = tv_shows.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(tv_shows[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tv_Show: TvShowModel) {
            with(itemView) {

                // check if movie favorite or not
                tvShowFavoriteHelper = TvShowFavoriteHelper.getInstance(this.context)
                tvShowFavoriteHelper.open()

                val cursors = tvShowFavoriteHelper.queryById(tv_Show.id.toString())

                if (cursors.count > 0){
                    btn_favorite.setImageResource(R.drawable.ic_favorite_true)
                }


                tv_item_id.text = tv_Show.id.toString()
                tv_movie_title.text = tv_Show.title
                tv_description.text = tv_Show.overview

                if(tv_Show.poster != null){
                    Glide.with(context)
                        .load(Constants.API_IMAGE_ENDPOINT + Constants.ENDPOINT_POSTER_SIZE_W185 + tv_Show.poster)
                        .into(img_movie)
                } else {
                    img_movie.setImageResource(R.drawable.ic_no_image_available)
                    img_movie.setBackgroundColor(Color.GRAY)
                }

                setOnClickListener {
                    onItemClickCallback?.onItemClicked(tv_Show)
                }

                btn_favorite.setOnClickListener {
                    tvShowFavoriteHelper.open()

                    val cursor = tvShowFavoriteHelper.queryById(tv_Show.id.toString())

                    if (cursor.count > 0){
                        tvShowFavoriteHelper.deleteById(tv_Show.id.toString())
                        btn_favorite.setImageResource(R.drawable.ic_favorite_false)
                    }else {
                        val values = ContentValues()
                        values.put(DatabaseContract.FavoriteColumns.TVSHOW_ID, tv_Show.id)
                        values.put(DatabaseContract.FavoriteColumns.TITLE, tv_Show.title)
                        values.put(DatabaseContract.FavoriteColumns.OVERVIEW, tv_Show.overview)
                        values.put(DatabaseContract.FavoriteColumns.POSTER, tv_Show.poster)
                        values.put(DatabaseContract.FavoriteColumns.BACKDROP, tv_Show.backdrop)
                        Log.d("INSERT", values.toString())
                        tvShowFavoriteHelper.insert(values)
                        btn_favorite.setImageResource(R.drawable.ic_favorite_true)
                    }

                    tvShowFavoriteHelper.close()
                }
            }
        }
    }
}