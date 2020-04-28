package com.ilham.made.fourthsubmission.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns._ID
import com.ilham.made.fourthsubmission.database.DatabaseContract.FavoriteColumns.Companion.MOVIE_FAVORITE_TABLE_NAME
import com.ilham.made.fourthsubmission.database.DatabaseContract.FavoriteColumns.Companion.MOVIE_ID
import java.sql.SQLException

class MovieFavoriteHelper (context: Context) {

    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = MOVIE_FAVORITE_TABLE_NAME
        private var INSTANCE: MovieFavoriteHelper? = null

        fun getInstance(context: Context): MovieFavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MovieFavoriteHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    fun queryById(movie_id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$MOVIE_ID = ?",
            arrayOf(movie_id),
            null,
            null,
            null,
            null)
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteById(movie_id: String): Int {
        return database.delete(DATABASE_TABLE, "$MOVIE_ID = '$movie_id'", null)
    }
}