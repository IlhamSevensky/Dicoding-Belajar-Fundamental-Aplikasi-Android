package com.ilham.made.consumerfavorite.data.db.tvshow

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TvShowDao {

    @Query("SELECT * FROM tvshow_database")
    fun getTvShow(): LiveData<List<TvShowDatabaseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTvShow(tvShow: List<TvShowDatabaseEntity>)
}

@Database(entities = [TvShowDatabaseEntity::class], version = 1, exportSchema = false)
abstract class TvShowDatabase : RoomDatabase() {
    abstract val tvshowDao: TvShowDao
}

private lateinit var INSTANCE: TvShowDatabase

fun getTvShowDatabase(context: Context): TvShowDatabase {
    synchronized(TvShowDatabase::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    TvShowDatabase::class.java,
                    "tvshow.db"
                )
                .build()
        }
    }
    return INSTANCE
}