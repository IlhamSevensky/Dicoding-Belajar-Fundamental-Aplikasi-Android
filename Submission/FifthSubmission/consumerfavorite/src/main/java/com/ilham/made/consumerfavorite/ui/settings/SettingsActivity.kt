package com.ilham.made.consumerfavorite.ui.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.ilham.made.consumerfavorite.R
import com.ilham.made.consumerfavorite.broadcast.ReminderReceiver
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        setupToolbar()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
    }

    companion object {
        private const val REMINDER_DEFAULT_VALUE = false
        private val TAG = SettingsActivity::class.java.simpleName
    }

    class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {

        private lateinit var KEY_REMINDER: String
        private lateinit var KEY_MOVIE_RELEASE_REMINDER: String
        private lateinit var KEY_LANGUAGE: String

        private lateinit var reminderReceiver: ReminderReceiver
        private lateinit var dailyReminderPreference: SwitchPreferenceCompat
        private lateinit var movieReleaseTodayReminderPreference: SwitchPreferenceCompat
        private lateinit var changeLanguagePreference: Preference

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            init()
            setSummaries()
        }

        private fun setSummaries() {
            val sh = preferenceManager.sharedPreferences

            dailyReminderPreference.isChecked = sh.getBoolean(KEY_REMINDER, REMINDER_DEFAULT_VALUE)
            movieReleaseTodayReminderPreference.isChecked =
                sh.getBoolean(KEY_MOVIE_RELEASE_REMINDER, REMINDER_DEFAULT_VALUE)
        }

        private fun init() {
            reminderReceiver = ReminderReceiver()

            KEY_REMINDER = resources.getString(R.string.key_reminder)
            KEY_MOVIE_RELEASE_REMINDER = resources.getString(R.string.key_movie_release_reminder)
            KEY_LANGUAGE = resources.getString(R.string.key_language)

            dailyReminderPreference =
                findPreference<SwitchPreferenceCompat>(KEY_REMINDER) as SwitchPreferenceCompat
            movieReleaseTodayReminderPreference =
                findPreference<SwitchPreferenceCompat>(KEY_MOVIE_RELEASE_REMINDER) as SwitchPreferenceCompat
            changeLanguagePreference = findPreference<Preference>(KEY_LANGUAGE) as Preference
            changeLanguagePreference.setOnPreferenceClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {

            if (key == KEY_REMINDER) {
                dailyReminderPreference.isChecked =
                    sharedPreferences.getBoolean(KEY_REMINDER, REMINDER_DEFAULT_VALUE)
                if (dailyReminderPreference.isChecked) {
                    context?.let {
                        if (!reminderReceiver.isReminderSet(
                                it,
                                ReminderReceiver.TYPE_DAILY_APP_REMINDER
                            )
                        ) {
                            reminderReceiver.setRepeatingReminder(
                                it,
                                ReminderReceiver.TYPE_DAILY_APP_REMINDER
                            )
                        }
                    }
                } else {
                    context?.let {
                        if (reminderReceiver.isReminderSet(
                                it,
                                ReminderReceiver.TYPE_DAILY_APP_REMINDER
                            )
                        ) {
                            reminderReceiver.cancelReminder(
                                it,
                                ReminderReceiver.TYPE_DAILY_APP_REMINDER
                            )
                        }
                    }
                }
                Log.d(TAG, "App Reminder Checked : " + dailyReminderPreference.isChecked.toString())
            }

            if (key == KEY_MOVIE_RELEASE_REMINDER) {
                movieReleaseTodayReminderPreference.isChecked =
                    sharedPreferences.getBoolean(KEY_MOVIE_RELEASE_REMINDER, REMINDER_DEFAULT_VALUE)
                if (movieReleaseTodayReminderPreference.isChecked) {
                    context?.let {
                        if (!reminderReceiver.isReminderSet(
                                it,
                                ReminderReceiver.TYPE_DAILY_MOVIE_RELEASE_REMINDER
                            )
                        ) {
                            reminderReceiver.setRepeatingReminder(
                                it,
                                ReminderReceiver.TYPE_DAILY_MOVIE_RELEASE_REMINDER
                            )
                        }
                    }
                } else {
                    context?.let {
                        if (reminderReceiver.isReminderSet(
                                it,
                                ReminderReceiver.TYPE_DAILY_MOVIE_RELEASE_REMINDER
                            )
                        ) {
                            reminderReceiver.cancelReminder(
                                it,
                                ReminderReceiver.TYPE_DAILY_MOVIE_RELEASE_REMINDER
                            )
                        }
                    }
                }
                Log.d(
                    TAG,
                    "Movie Release Reminder Checked : " + movieReleaseTodayReminderPreference.isChecked.toString()
                )
            }
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(settings_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        settings_toolbar.elevation = 8F
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}