package com.ilham.made.fifthsubmission.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns
import androidx.room.OnConflictStrategy
import com.ilham.made.fifthsubmission.data.db.favorite.movie.getFavoriteMovieDatabase
import com.ilham.made.fifthsubmission.utils.Constants.Companion.COLUMN_MOVIE_ID


class FavoriteMovieContentProvider : ContentProvider() {

    companion object {
        /** The authority of this content provider.  */
        const val AUTHORITY = "com.ilham.made.fifthsubmission"
        const val TABLE_NAME = "favorite_movie_database"
        private const val SCHEME = "content"

        val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()
    }


    /** The match code for some items in the Movie table.  */
    private val CODE_MOVIE = 1

    /** The match code for an item in the Movie table.  */
    private val CODE_MOVIE_ID = 2

    /** The URI matcher.  */
    private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

    init {
        MATCHER.addURI(AUTHORITY, TABLE_NAME, CODE_MOVIE)
        MATCHER.addURI(AUTHORITY, "$TABLE_NAME/#", CODE_MOVIE_ID)
    }

    override fun onCreate(): Boolean = true

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (CODE_MOVIE_ID) {
            MATCHER.match(uri) -> getFavoriteMovieDatabase(context!!).openHelper.writableDatabase.delete(
                TABLE_NAME,
                "$COLUMN_MOVIE_ID = '${uri.lastPathSegment.toString()}'",
                null
            )
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (CODE_MOVIE) {
            MATCHER.match(uri) -> getFavoriteMovieDatabase(context!!).openHelper.writableDatabase.insert(
                TABLE_NAME,
                OnConflictStrategy.REPLACE,
                values
            )
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (MATCHER.match(uri)) {
            CODE_MOVIE -> getFavoriteMovieDatabase(context!!).favMovieDao.selectAll()
            CODE_MOVIE_ID -> getFavoriteMovieDatabase(context!!).favMovieDao.selectById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when (CODE_MOVIE_ID) {
            MATCHER.match(uri) -> getFavoriteMovieDatabase(context!!).openHelper.writableDatabase.update(
                TABLE_NAME,
                OnConflictStrategy.REPLACE,
                values,
                "${BaseColumns._ID} = ?",
                arrayOf(uri.lastPathSegment.toString())
            )
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return updated
    }
}
