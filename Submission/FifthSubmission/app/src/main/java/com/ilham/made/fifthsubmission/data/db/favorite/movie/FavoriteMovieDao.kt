package com.ilham.made.fifthsubmission.data.db.favorite.movie

import android.content.Context
import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface FavoriteMovieDao {

    @Query("SELECT * FROM favorite_movie_database")
    fun getFavoriteMovies(): LiveData<List<FavoriteMovieDatabaseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteMovies(favMovies: FavoriteMovieDatabaseEntity)

    @Query("DELETE FROM favorite_movie_database WHERE movie_id = :movieId")
    fun removeFavoriteMovies(movieId: String)

    @Query("SELECT COUNT(*) FROM favorite_movie_database WHERE movie_id = :movieId")
    fun checkFavoriteMovie(movieId: String): Int

    @Query("SELECT poster FROM favorite_movie_database")
    fun getFavoriteMoviePoster(): List<String>

    /**
     * Content Provider
     */

    @Query("SELECT * FROM favorite_movie_database")
    fun selectAll() : Cursor

    @Query("SELECT * FROM favorite_movie_database WHERE movie_id = :movieId")
    fun selectById(movieId: String) : Cursor

    @Insert
    fun insert(favMovies: FavoriteMovieDatabaseEntity): Long

    @Query("DELETE FROM favorite_movie_database WHERE movie_id = :movieId")
    fun remove(movieId: String): Int
}

@Database(entities = [FavoriteMovieDatabaseEntity::class], version = 1, exportSchema = false)
abstract class FavoriteMovieDatabase : RoomDatabase() {
    abstract val favMovieDao: FavoriteMovieDao
}

private lateinit var INSTANCE: FavoriteMovieDatabase

fun getFavoriteMovieDatabase(context: Context): FavoriteMovieDatabase {
    synchronized(FavoriteMovieDatabase::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteMovieDatabase::class.java,
                    "favorite_movie.db"
                )
                .allowMainThreadQueries()
                .build()
        }
    }
    return INSTANCE
}