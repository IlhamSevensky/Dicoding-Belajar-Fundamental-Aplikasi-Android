package com.ilham.made.fourthsubmission.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID
import com.ilham.made.fourthsubmission.database.DatabaseContract.FavoriteColumns.Companion.BACKDROP
import com.ilham.made.fourthsubmission.database.DatabaseContract.FavoriteColumns.Companion.MOVIE_FAVORITE_TABLE_NAME
import com.ilham.made.fourthsubmission.database.DatabaseContract.FavoriteColumns.Companion.MOVIE_ID
import com.ilham.made.fourthsubmission.database.DatabaseContract.FavoriteColumns.Companion.OVERVIEW
import com.ilham.made.fourthsubmission.database.DatabaseContract.FavoriteColumns.Companion.POSTER
import com.ilham.made.fourthsubmission.database.DatabaseContract.FavoriteColumns.Companion.TITLE
import com.ilham.made.fourthsubmission.database.DatabaseContract.FavoriteColumns.Companion.TVSHOW_FAVORITE_TABLE_NAME
import com.ilham.made.fourthsubmission.database.DatabaseContract.FavoriteColumns.Companion.TVSHOW_ID

internal class DatabaseHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "db_favorite"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_MOVIE_FAVORITE = "CREATE TABLE $MOVIE_FAVORITE_TABLE_NAME" +
                " ($_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $MOVIE_ID TEXT NOT NULL," +
                " $TITLE TEXT NOT NULL," +
                " $OVERVIEW TEXT NOT NULL," +
                " $POSTER TEXT," +
                " $BACKDROP TEXT)"

        private const val SQL_CREATE_TABLE_TVSHOW_FAVORITE = "CREATE TABLE $TVSHOW_FAVORITE_TABLE_NAME" +
                " ($_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $TVSHOW_ID TEXT NOT NULL," +
                " $TITLE TEXT NOT NULL," +
                " $OVERVIEW TEXT NOT NULL," +
                " $POSTER TEXT," +
                " $BACKDROP TEXT)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_MOVIE_FAVORITE)
        db?.execSQL(SQL_CREATE_TABLE_TVSHOW_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $MOVIE_FAVORITE_TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS $TVSHOW_FAVORITE_TABLE_NAME")
        onCreate(db)
    }
}