package com.ilham.made.mysharedpreferences.utils

import android.content.Context
import com.ilham.made.mysharedpreferences.models.UserModel

internal class UserPreference(context: Context) {

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val AGE = "age"
        private const val PHONE_NUMBER = "phone"
        private const val LOVE_MU = "islove"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: UserModel) {
        val editor = preferences.edit()

        // pada metode put, parameter pertama adalah key dan parameter kedua adalah value
        editor.putString(NAME, value.name)
        editor.putString(EMAIL, value.email)
        editor.putInt(AGE, value.age)
        editor.putString(PHONE_NUMBER, value.phoneNumber)
        editor.putBoolean(LOVE_MU, value.isLove)
        // commit berjalan secara syncronus, apply berjalan secara asyncronus (recommended)
        //editor.commit()
        editor.apply()


    }

    fun getUser(): UserModel {
        val model = UserModel()
        // pada metode get, parameter pertama adalah key dan parameter kedua adalah default value (karena pada awal aplikasi dijalankan pasti datanya kosong)
        model.name = preferences.getString(NAME, "")
        model.email = preferences.getString(EMAIL, "")
        model.age = preferences.getInt(AGE, 0)
        model.phoneNumber = preferences.getString(PHONE_NUMBER, "")
        model.isLove = preferences.getBoolean(LOVE_MU, false)
        return model
    }
}