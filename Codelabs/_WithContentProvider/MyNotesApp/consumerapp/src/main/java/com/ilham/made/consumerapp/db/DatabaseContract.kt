package com.ilham.made.consumerapp.db

import android.net.Uri
import android.provider.BaseColumns

internal class DatabaseContract {

    companion object {
        const val AUTHORITY = "com.ilham.made.mynotesapp"
        const val SCHEME = "content"
    }

    internal class NoteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "note"
            const val _ID = "_id"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val DATE = "date"
            /*
                Jika Anda perhatikan di dalam koe Java, tidak ada kolom id di dalam kelas contract.
                Alasannya, kolom id sudah ada secara otomatis di dalam kelas BaseColumns.
                Pada kode kelas selanjutnya perhatikan bagaimana pemanggilan id dengan menggunakan identifier _ID.
             */

            // untuk membuat URI content://com.ilham.made.mynotesapp/note
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}