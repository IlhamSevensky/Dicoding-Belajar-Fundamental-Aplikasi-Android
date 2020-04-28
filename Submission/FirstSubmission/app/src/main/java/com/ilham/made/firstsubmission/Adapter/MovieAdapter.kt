package com.ilham.made.firstsubmission.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.ilham.made.firstsubmission.Model.MovieModel
import com.ilham.made.firstsubmission.R
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter internal constructor(private val context: Context) : BaseAdapter(){
    internal var movies = ArrayList<MovieModel>()

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        var itemView = view
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_movie, viewGroup, false)
        }

        val viewHolder = ViewHolder(itemView as View)

        val movie= getItem(position) as MovieModel
        viewHolder.bind(movie)

        return itemView
    }

    override fun getItem(position: Int): Any {
        return movies[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return movies.size
    }

    private inner class ViewHolder internal constructor(private val view: View) {
        internal fun bind(movie: MovieModel) {
            with(view) {
                tv_movie_title.text = movie.movie_name
                tv_description.text = movie.movie_desc
                img_movie.setImageResource(movie.movie_image)
            }
        }
    }
}