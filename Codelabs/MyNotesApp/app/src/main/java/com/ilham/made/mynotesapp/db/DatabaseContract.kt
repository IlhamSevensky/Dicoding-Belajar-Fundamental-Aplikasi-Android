package com.ilham.made.mynotesapp.db

import android.provider.BaseColumns

internal class DatabaseContract {

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
        }
    }
}