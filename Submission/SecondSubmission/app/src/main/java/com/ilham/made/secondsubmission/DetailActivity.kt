package com.ilham.made.secondsubmission

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ilham.made.secondsubmission.model.ModelData
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_TITLE = "extra_title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        toolbar_movie_detail.elevation = 8F
        toolbar_movie_detail.title = resources.getString(R.string.title_toolbar_detail)
        toolbar_movie_detail.navigationIcon =
            ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_24dp)
        toolbar_movie_detail.setNavigationOnClickListener { finish() }

        val data = intent.getParcelableExtra(EXTRA_DATA) as ModelData

        when (intent.getStringExtra(EXTRA_TITLE)) {
            resources.getString(R.string.label_creator) -> {
                lb_detail_director.text = resources.getString(R.string.label_creator)
            }

            resources.getString(R.string.label_director) -> {
                lb_detail_director.text = resources.getString(R.string.label_director)
            }
        }

        tv_detail_title.text = data.name
        tv_detail_desc.text = data.desc
        img_detail_movie.setImageResource(data.poster)
        tv_detail_director.text = data.director
        tv_detail_runtime.text = data.duration
        tv_detail_genre.text = data.genre
        img_detail_movie_highlight.setImageResource(data.img_preview)
    }
}
