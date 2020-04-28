package com.ilham.made.secondsubmission.ui.tv_show


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilham.made.secondsubmission.R
import com.ilham.made.secondsubmission.adapter.TvShowAdapter
import com.ilham.made.secondsubmission.model.ModelData
import kotlinx.android.synthetic.main.fragment_tv_show.*

/**
 * A simple [Fragment] subclass.
 */
class TvShowFragment : Fragment() {

    private val list = ArrayList<ModelData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_tvshow.setHasFixedSize(true)

        list.addAll(getListTvShow())
        showRecyclerList()
    }

    private fun showRecyclerList() {
        rv_tvshow.layoutManager = LinearLayoutManager(view?.context)
        val listTvShowAdapter = TvShowAdapter(list)
        rv_tvshow.adapter = listTvShowAdapter
    }

    private fun getListTvShow(): ArrayList<ModelData> {
        val tvShowName = resources.getStringArray(R.array.data_tvshow_name)
        val tvShowDescription = resources.getStringArray(R.array.data_tvshow_description)
        val tvShowPoster = resources.obtainTypedArray(R.array.data_tvshow_photo)

        val tvShowCreator = resources.getStringArray(R.array.data_tvshow_creator)
        val tvShowDuration = resources.getStringArray(R.array.data_tvshow_runtime)
        val tvShowGenre = resources.getStringArray(R.array.data_tvshow_genre)
        val tvShowImagePreview = resources.obtainTypedArray(R.array.data_tvshow_photo_highlight)

        val listMovie = ArrayList<ModelData>()
        for (position in tvShowName.indices) {
            val movie = ModelData(
                tvShowName[position],
                tvShowDescription[position],
                tvShowPoster.getResourceId(position, -1),
                tvShowImagePreview.getResourceId(position, -1),
                tvShowCreator[position],
                tvShowDuration[position],
                tvShowGenre[position]
            )
            listMovie.add(movie)
        }
        return listMovie
    }

}
