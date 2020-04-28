package com.ilham.made.mylistview.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.ilham.made.mylistview.Model.Hero
import com.ilham.made.mylistview.R
import kotlinx.android.synthetic.main.item_hero.view.*

class HeroAdapter internal constructor(private val context: Context) : BaseAdapter(){
    internal var heroes = ArrayList<Hero>(

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        var itemView = view
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_hero, viewGroup, false)
        }

        val viewHolder = ViewHolder(itemView as View)

        val hero = getItem(position) as Hero
        viewHolder.bind(hero)

        return itemView

    }

    override fun getItem(position: Int): Any {
        return heroes[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return heroes.size
    }

    private inner class ViewHolder internal constructor(private val view: View){
        internal fun bind(hero: Hero){
            with(view) {
                txt_name.text = hero.name
                txt_description.text = hero.description
                img_photo.setImageResource(hero.photo)
            }
        }
    }

//    private inner class ViewHolder internal constructor(private val view: View){
//        internal fun bind(hero: Hero){
//                view.txt_name.text = hero.name
//                view.txt_description.text = hero.description
//                view.img_photo.setImageResource(hero.photo)
//        }
//    }
}