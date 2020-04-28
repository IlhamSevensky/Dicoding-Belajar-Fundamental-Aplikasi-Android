package com.ilham.made.consumerfavorite.data.db.favorite.tvshow

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteTvShowDao {

    @Query("SELECT * FROM favorite_tvshow_database")
    fun getFavoriteTvShow(): LiveData<List<FavoriteTvShowDatabaseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteTvShow(favTvShow: FavoriteTvShowDatabaseEntity)

    @Query("DELETE FROM favorite_tvshow_database WHERE tvshow_id = :tvshowId")
    fun removeFavoriteTvShow(tvshowId: String)

    @Query("SELECT COUNT(*) FROM favorite_tvshow_database WHERE tvshow_id = :tvshowId")
    fun checkFavoriteTvShow(tvshowId: String): Int
}

@Database(entities = [FavoriteTvShowDatabaseEntity::class], version = 1, exportSchema = false)
abstract class FavoriteTvShowDatabase : RoomDatabase() {
    abstract val favTvShowDao: FavoriteTvShowDao
}

private lateinit var INSTANCE: FavoriteTvShowDatabase

fun getFavoriteTvShowDatabase(context: Context): FavoriteTvShowDatabase {
    synchronized(FavoriteTvShowDatabase::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteTvShowDatabase::class.java,
                    "favorite_tvshow.db"
                )
                .allowMainThreadQueries()
                .build()
        }
    }
    return INSTANCE
}