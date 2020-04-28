package com.ilham.made.myrecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilham.made.myrecyclerview.adapter.CardViewHeroAdapter
import com.ilham.made.myrecyclerview.adapter.GridHeroAdapter
import com.ilham.made.myrecyclerview.adapter.ListHeroAdapter
import com.ilham.made.myrecyclerview.model.Hero
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val list = ArrayList<Hero>()
    private var title = "Mode List"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_heroes.setHasFixedSize(true)

        list.addAll(getListHeroes())
        showRecyclerList()
        setActionBarTitle(title)
    }

    private fun setActionBarTitle(title: String?) { // Function untuk ganti title action bar
        supportActionBar?.title = title
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // Override method untuk inflate/tampilkan option menu
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // Override method untuk item yang di pilih / di klik
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode( selectedMode: Int) { // Function untuk pilihan pada option menu
        when(selectedMode){
            R.id.action_list -> {
                title = "Mode List"
                showRecyclerList()
            }

            R.id.action_grid -> {
                title = "Mode Grid"
                showRecyclerGrid()
            }

            R.id.action_cardview -> {
                title = "Mode CardView"
                showRecyclerCardView()
            }
        }
        setActionBarTitle(title)
    }

    private fun showRecyclerList() { // Function untuk menampilkan data dalam bentuk list dengan layout manager
        rv_heroes.layoutManager = LinearLayoutManager(this)
        val listHeroAdapter = ListHeroAdapter(list)
        rv_heroes.adapter = listHeroAdapter

        listHeroAdapter.setOnItemClickCallback(object : ListHeroAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Hero) {
                showSelectedHero(data)
            }
        })
    }

    private fun showRecyclerGrid() { // Function untuk menampilkan data dalam bentuk grid dengan layout manager
        rv_heroes.layoutManager = GridLayoutManager(this, 2)
        val gridHeroAdapter = GridHeroAdapter(list)
        rv_heroes.adapter = gridHeroAdapter

        gridHeroAdapter.setOnItemClickCallback(object : GridHeroAdapter.OnItemClickCallback{ // Implementasi interface onitemcallback yang ada pada adapter
            override fun onItemClicked(data: Hero) {
                showSelectedHero(data) // Panggil function untuk memunculkan toast pada selectedhero
            }
        })
    }

    private fun showRecyclerCardView() { // Function untuk menampilkan data dalam bentuk cardview dengan layout manager
        rv_heroes.layoutManager = LinearLayoutManager(this)
        val cardHeroAdapter = CardViewHeroAdapter(list)
        rv_heroes.adapter = cardHeroAdapter
    }
    private fun getListHeroes(): ArrayList<Hero> { // Function untuk mendapatkan data list dari resource string ke model dan disimpan dalam arraylist
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.getStringArray(R.array.data_photo)

        val listHero = ArrayList<Hero>()
        for (position in dataName.indices){
            val hero = Hero (
                dataName[position],
                dataDescription[position],
                dataPhoto[position]
            )
            listHero.add(hero)
        }

        return listHero
    }

    private fun showSelectedHero(hero: Hero) { // Function untuk menampilkan toast dari item yang di klik
        Toast.makeText(this, "Kamu memilih ${hero.name}", Toast.LENGTH_SHORT).show()
    }
}
