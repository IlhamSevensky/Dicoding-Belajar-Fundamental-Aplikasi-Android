package com.ilham.made.thirdsubmission.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilham.made.thirdsubmission.DetailActivity
import com.ilham.made.thirdsubmission.R
import com.ilham.made.thirdsubmission.adapters.TvShowAdapter
import com.ilham.made.thirdsubmission.models.TvShowModel
import com.ilham.made.thirdsubmission.utils.Constants
import com.ilham.made.thirdsubmission.viewmodels.TvShowState
import com.ilham.made.thirdsubmission.viewmodels.TvShowViewModel
import kotlinx.android.synthetic.main.empty_state.*
import kotlinx.android.synthetic.main.fragment_tv_show.*

/**
 * A simple [Fragment] subclass.
 */
class TvShowFragment : Fragment() {

    private lateinit var tvShowViewModel: TvShowViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show, container, false)
    }

    override fun onResume() {
        super.onResume()
        // Check if tv show data in ViewModel isNullOrEmpty
        if (tvShowViewModel.getAllTvShow().value.isNullOrEmpty()) {
            // if tv show data isNullOrEmpty, Fill up tv show data
            tvShowViewModel.fetchAllTvShow()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvShowViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(TvShowViewModel::class.java)

        showRecyclerList()

        tvShowViewModel.getAllTvShow().observe(this, Observer {
            rv_tvshow.adapter?.let { adapter ->
                if (adapter is TvShowAdapter) {
                    // Fill up data to recyclerview
                    adapter.setTvShow(it)
                    // On item click
                    adapter.setOnItemClickCallback(object : TvShowAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: TvShowModel) {
                            val mBundle = Bundle()
                            mBundle.putString(
                                DetailActivity.EXTRA_TYPE,
                                Constants.ACTIVITY_TYPE_TVSHOW
                            )
                            mBundle.putInt(DetailActivity.EXTRA_DATA, data.id!!)
                            findNavController().navigate(
                                R.id.action_navigation_tvshow_to_detailActivity,
                                mBundle
                            )
                        }
                    })
                }
            }
        })

        tvShowViewModel.getTvShowState().observer(this, Observer {
            handleUIState(it)
        })

    }

    private fun showRecyclerList() {
        rv_tvshow.apply {
            layoutManager = LinearLayoutManager(context)
            // Fill adapter with empty list
            adapter = TvShowAdapter(mutableListOf())
        }
    }

    private fun handleUIState(it: TvShowState) {
        when (it) {
            is TvShowState.IsLoading -> {
                isLoading(it.state)
            }
            is TvShowState.Error -> {
                isLoading(false)
                empty_state_tv_show.visibility = View.VISIBLE
                btn_reconnect.setOnClickListener {view ->
                    tvShowViewModel.fetchAllTvShow()
                }
            }
            is TvShowState.IsSuccess -> {
                empty_state_tv_show.visibility = View.GONE
            }
        }
    }

    private fun isLoading(state: Boolean) {
        if (state) {
            loading_tv_show.visibility = View.VISIBLE
        } else {
            loading_tv_show.visibility = View.GONE
        }
    }

}
