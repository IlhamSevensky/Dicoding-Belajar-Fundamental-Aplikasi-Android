package com.ilham.made.consumerfavorite.ui.favorite.movie

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilham.made.consumerfavorite.R
import com.ilham.made.consumerfavorite.models.FavoriteMovieModel
import com.ilham.made.consumerfavorite.ui.detail.DetailActivity
import com.ilham.made.consumerfavorite.utils.Constants
import com.ilham.made.consumerfavorite.utils.Constants.Companion.CONTENT_URI
import com.ilham.made.consumerfavorite.utils.Helper
import kotlinx.android.synthetic.main.empty_state.*
import kotlinx.android.synthetic.main.fragment_favorite_movie.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class FavoriteMovieFragment : Fragment() {

    override fun onResume() {
        super.onResume()
        rv_favorite_movie.adapter.let { adapter ->
            if (adapter is FavoriteMovieAdapter) {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavoriteMovieAsync()
                Helper.updateFavoriteMovieWidget(view.context)
            }
        }

        view.context.contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showRecyclerList()
        loadFavoriteMovieAsync()

        rv_favorite_movie.adapter.let { adapter ->
            if (adapter is FavoriteMovieAdapter) {
                adapter.setOnItemClickCallback(object : FavoriteMovieAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: FavoriteMovieModel) {
                        startActivity(
                            Intent(context, DetailActivity::class.java)
                                .putExtra(DetailActivity.EXTRA_TYPE, Constants.ACTIVITY_TYPE_MOVIE)
                                .putExtra(DetailActivity.EXTRA_DATA, data.movie_id?.toInt()))
                    }
                })
            }
        }
    }

    private fun loadFavoriteMovieAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = context?.contentResolver?.query(CONTENT_URI, null, null, null, null)
                Helper.mapCursorToArrayList(cursor)
            }
            val notes = deferredNotes.await()
            if (notes.size > 0) {
                rv_favorite_movie.adapter.let { adapter ->
                    if (adapter is FavoriteMovieAdapter) {
                        adapter.setFavMovies(notes)
                    }
                }
            } else {
                if (rv_favorite_movie != null){
                    rv_favorite_movie.adapter.let { adapter ->
                        if (adapter is FavoriteMovieAdapter) {
                            adapter.setFavMovies(mutableListOf())
                        }
                    }
                    emptyStateEmptyFavorite()
                }
            }
        }
    }

    private fun showRecyclerList() {
        rv_favorite_movie.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = FavoriteMovieAdapter(mutableListOf())
        }
    }

    private fun emptyStateEmptyFavorite() {
        img_empty_state.setImageResource(R.drawable.ic_empty_state_favorite)
        btn_reconnect.visibility = View.GONE
        img_empty_state.contentDescription =
            resources.getString(R.string.empty_state_desc_no_favorite_item_movie)
        title_empty_state.text = resources.getString(R.string.empty_state_title_no_favorite_item)
        desc_empty_state.text =
            resources.getString(R.string.empty_state_desc_no_favorite_item_movie)
        empty_state_favorite_movie.visibility = View.VISIBLE
    }

}
