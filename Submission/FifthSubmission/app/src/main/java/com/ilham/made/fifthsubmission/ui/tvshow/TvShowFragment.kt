package com.ilham.made.fifthsubmission.ui.tvshow

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilham.made.fifthsubmission.R
import com.ilham.made.fifthsubmission.data.repository.TvShowState
import com.ilham.made.fifthsubmission.models.TvShowModel
import com.ilham.made.fifthsubmission.ui.detail.DetailActivity
import com.ilham.made.fifthsubmission.utils.Constants
import kotlinx.android.synthetic.main.empty_state.*
import kotlinx.android.synthetic.main.fragment_tv_show.*

/**
 * A simple [Fragment] subclass.
 */
class TvShowFragment : Fragment() {

    companion object {
        private val TAG = TvShowFragment::class.java.simpleName
    }

    private val tvShowViewModel: TvShowViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        ViewModelProvider(this@TvShowFragment, TvShowViewModel.Factory(activity.application))
            .get(TvShowViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        rv_tvshow.adapter.let {
            when(it){
                is TvShowAdapter -> it.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        showRecyclerList()
        viewModelSetup()

        btn_reconnect.setOnClickListener {
            tvShowViewModel.refreshTvShowFromRepository()
        }
    }

    private fun viewModelSetup() {
        tvShowViewModel.tvShows.observe(viewLifecycleOwner, Observer { tvshow ->
            tvshow?.apply {
                rv_tvshow.adapter?.let { adapter ->
                    if (adapter is TvShowAdapter) {
                        adapter.setTvShows(this)
                        adapter.setOnItemClickCallback(object : TvShowAdapter.OnItemClickCallback {
                            override fun onItemClicked(data: TvShowModel) {
                                startActivity(
                                    Intent(context, DetailActivity::class.java)
                                    .putExtra(DetailActivity.EXTRA_TYPE, Constants.ACTIVITY_TYPE_TVSHOW)
                                    .putExtra(DetailActivity.EXTRA_DATA, data.tv_show_id))
                            }
                        })
                    }
                }
            }
        })

        tvShowViewModel.state.observer(viewLifecycleOwner, Observer {
            handleUIState(it)
        })
    }

    private fun showRecyclerList() {
        rv_tvshow.apply {
            layoutManager = LinearLayoutManager(context)
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
                Log.d(TAG, "ERROR : ${it.error}")
                empty_state_tv_show.visibility = View.VISIBLE
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
