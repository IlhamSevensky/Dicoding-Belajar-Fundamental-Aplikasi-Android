package com.ilham.made.thirdsubmission.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilham.made.thirdsubmission.R
import com.ilham.made.thirdsubmission.models.TvShowModel
import com.ilham.made.thirdsubmission.utils.Constants
import kotlinx.android.synthetic.main.item_row_data.view.*

class TvShowAdapter(private var tv_shows: MutableList<TvShowModel>) :
    RecyclerView.Adapter<TvShowAdapter.ViewHolder>() {

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
            }
        }
    }
}