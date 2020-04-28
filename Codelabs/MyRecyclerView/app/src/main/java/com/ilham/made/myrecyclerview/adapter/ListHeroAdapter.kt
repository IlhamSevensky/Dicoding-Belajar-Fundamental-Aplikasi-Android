package com.ilham.made.myrecyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ilham.made.myrecyclerview.R
import com.ilham.made.myrecyclerview.model.Hero
import kotlinx.android.synthetic.main.item_row_hero.view.*
import kotlin.collections.ArrayList

class ListHeroAdapter(private val listhero: ArrayList<Hero>) : RecyclerView.Adapter<ListHeroAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null // Inisialisasi ItemClickCallback agar onclick listener bisa diimplementasikan di activity, bukan di adapter

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) { // Function setter onitemclickcallback
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback { // interface untuk implementasi onclicklistener di activity (agar bisa menggunakan onclicklistener pada activity, bukan di adapter)
        fun onItemClicked(data: Hero)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(hero: Hero){
            with(itemView){
                Glide.with(itemView.context)
                    .load(hero.photo)
                    .apply(RequestOptions().override(55,55))
                    .into(img_item_photo)

                tv_item_name.text = hero.name
                tv_item_description.text = hero.description

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(hero) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_hero, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listhero.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listhero[position])
    }

}