package com.ilham.made.fourthsubmission.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns._ID
import com.ilham.made.fourthsubmission.database.DatabaseContract.FavoriteColumns.Companion.TVSHOW_ID
import java.sql.SQLException

class TvShowFavoriteHelper(context: Context) {

    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE =
            DatabaseContract.FavoriteColumns.TVSHOW_FAVORITE_TABLE_NAME
        private var INSTANCE: TvShowFavoriteHelper? = null

        fun getInstance(context: Context): TvShowFavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: TvShowFavoriteHelper(context)
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

    fun queryById(tvshow_id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$TVSHOW_ID = ?",
            arrayOf(tvshow_id),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteById(tvshow_id: String): Int {
        return database.delete(DATABASE_TABLE, "$TVSHOW_ID = '$tvshow_id'", null)
    }
}