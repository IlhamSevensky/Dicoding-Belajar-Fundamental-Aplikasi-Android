package com.ilham.made.mypreloaddata.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class AppPreference (context: Context) {

    companion object {
        private const val PREFS_NAME = "MahasiswaPref"
        private const val APP_FIRST_RUN = "app_first_run"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * Ada 2 metode pada kelas AppPreference, yaitu setFirstRun dan getFirstRun.

        Fungsi setFirstRun digunakan untuk mengganti nilai dengan kata kunci string dari app_first_run.
        Metode ini akan dipanggil setelah proses insert ke dalam database berhasil.

        Fungsi getFirstRun digunakan untuk mengambil nilai dengan kata kunci string dari app_first_run.
        Kedua metode ini digunakan sebagai parameter untuk memeriksa apakah data awal mahasiswa sudah dimasukkan ke database.
     */
    var firstRun: Boolean?
        get() = prefs.getBoolean(APP_FIRST_RUN, true)
        set(input) {
            prefs.edit {
                putBoolean(APP_FIRST_RUN, input as Boolean)
            }
        }
}