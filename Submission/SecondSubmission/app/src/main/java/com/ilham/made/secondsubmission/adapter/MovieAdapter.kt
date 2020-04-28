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

class MovieAdapter(private val listMovie: ArrayList<ModelData>) :
    RecyclerView.Adapter<MovieAdapter.ListViewHolder>() {


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: ModelData) {
            with(itemView) {
                img_movie.setImageResource(movie.poster)
                tv_movie_title.text = movie.name
                tv_description.text = movie.desc
            }

            itemView.setOnClickListener {
                val director = itemView.resources.getString(R.string.label_director)
                val mBundle = Bundle()
                val data = ModelData(
                    movie.name,
                    movie.desc,
                    movie.poster,
                    movie.img_preview,
                    movie.director,
                    movie.duration,
                    movie.genre
                )

                mBundle.putParcelable(DetailActivity.EXTRA_DATA, data)
                mBundle.putString(DetailActivity.EXTRA_TITLE, director)
                itemView.findNavController()
                    .navigate(R.id.action_navigation_movie_to_detailActivity, mBundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_data, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listMovie.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listMovie[position])
    }

}