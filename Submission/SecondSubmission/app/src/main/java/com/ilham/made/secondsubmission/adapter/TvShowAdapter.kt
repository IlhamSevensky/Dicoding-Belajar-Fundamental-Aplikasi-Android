package com.ilham.made.secondsubmission.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ilham.made.secondsubmission.DetailActivity
import com.ilham.made.secondsubmission.R
import com.ilham.made.secondsubmission.model.ModelData
import kotlinx.android.synthetic.main.item_row_data.view.*

class TvShowAdapter(private val listTvShow: ArrayList<ModelData>) :
    RecyclerView.Adapter<TvShowAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tvshow: ModelData) {
            with(itemView) {
                img_movie.setImageResource(tvshow.poster)
                tv_movie_title.text = tvshow.name
                tv_description.text = tvshow.desc
            }

            itemView.setOnClickListener {
                val creator = itemView.resources.getString(R.string.label_creator)
                val mBundle = Bundle()
                val data = ModelData(
                    tvshow.name,
                    tvshow.desc,
                    tvshow.poster,
                    tvshow.img_preview,
                    tvshow.director,
                    tvshow.duration,
                    tvshow.genre
                )

                mBundle.putParcelable(DetailActivity.EXTRA_DATA, data)
                mBundle.putString(DetailActivity.EXTRA_TITLE, creator)
                itemView.findNavController()
                    .navigate(R.id.action_navigation_tvshow_to_detailActivity, mBundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_data, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listTvShow.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listTvShow[position])
    }
}