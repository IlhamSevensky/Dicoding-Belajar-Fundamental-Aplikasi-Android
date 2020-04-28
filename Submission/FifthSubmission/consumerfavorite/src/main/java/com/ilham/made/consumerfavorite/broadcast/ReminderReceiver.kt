package com.ilham.made.consumerfavorite.broadcast

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.ilham.made.consumerfavorite.BuildConfig
import com.ilham.made.consumerfavorite.R
import com.ilham.made.consumerfavorite.models.MovieModel
import com.ilham.made.consumerfavorite.ui.main.MainActivity
import com.ilham.made.consumerfavorite.utils.WrappedListResponse
import com.ilham.made.consumerfavorite.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class ReminderReceiver : BroadcastReceiver() {

    companion object {
        private val TAG = ReminderReceiver::class.java.simpleName

        const val EXTRA_TYPE = "type"

        const val TYPE_DAILY_APP_REMINDER = "daily_app_reminder"
        const val TYPE_DAILY_MOVIE_RELEASE_REMINDER = "daily_movie_release_reminder"

        private const val ID_DAILY_REMINDER = 100
        private var ID_DAILY_MOVIE_RELEASE_REMINDER = 101

        private const val TIME_DAILY_REMINDER = "07:00"
        private const val TIME_DAILY_MOVIE_RELEASE_REMINDER = "08:00"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)

        if (type.equals(TYPE_DAILY_APP_REMINDER, ignoreCase = true)) {
            val title = context.resources.getString(R.string.notif_title_daily_app_reminder)
            val message = context.resources.getString(R.string.message_daily_app_reminder)
            var notifId = ID_DAILY_REMINDER
            showAlarmNotification(context, title, message, notifId)
        } else {
            getMovieToday(context)
        }

    }

    private fun getMovieToday(context: Context) {
        val title = context.resources.getString(R.string.notif_title_movie_release_reminder)
        var notifId = ID_DAILY_MOVIE_RELEASE_REMINDER
        val dateNow = getDateNow()

        ApiClient.instance.getMovieReleaseToday(BuildConfig.TMDB_API_KEY, dateNow, dateNow)
            .enqueue(object : Callback<WrappedListResponse<MovieModel>> {
                override fun onFailure(call: Call<WrappedListResponse<MovieModel>>, t: Throwable) {
                    Log.d(TAG, "Failed to get movie release today with error : ${t.message}")
                }

                override fun onResponse(
                    call: Call<WrappedListResponse<MovieModel>>,
                    response: Response<WrappedListResponse<MovieModel>>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.results != null) {
                            Log.d(TAG, "Success Get Movie Release Today ($dateNow)")
                            Log.d(
                                TAG,
                                "Movie Today Result : " + response.body()!!.results?.get(0)?.title!!
                            )
                            for (items in response.body()!!.results?.indices!!){
                                showAlarmNotification(
                                    context,
                                    title,
                                    response.body()!!.results?.get(items)?.title!!,
                                    notifId++
                                )
                            }

                        } else {
                            Log.d(TAG, "Failed Get Movie Release Today ($dateNow)")
                        }
                    } else {
                        Log.d(TAG, "Fetch Movie Release Today ($dateNow) = Not Successful")
                    }
                }

            })
    }

    private fun getDateNow(): String {
        val locale = Locale.getDefault()

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", locale)
            Log.d(TAG, "SDK26+ DATE NOW : " + LocalDate.now().format(dateFormat))
            LocalDate.now().toString()
        } else {
            val getDate = Date()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", locale)
            val dateNow: String = dateFormat.format(getDate)
            Log.d(TAG, "DATE NOW : $dateNow")
            dateNow
        }
    }

    fun setRepeatingReminder(context: Context, type: String) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        intent.putExtra(EXTRA_TYPE, type)

        val time = if (type.equals(
                TYPE_DAILY_APP_REMINDER,
                ignoreCase = true
            )
        ) TIME_DAILY_REMINDER else TIME_DAILY_MOVIE_RELEASE_REMINDER

        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
            set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        }

        Log.d(TAG, "SET : $calendar")

        val requestCode = if (type.equals(
                TYPE_DAILY_APP_REMINDER,
                ignoreCase = true
            )
        ) ID_DAILY_REMINDER else ID_DAILY_MOVIE_RELEASE_REMINDER

        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Log.d(TAG, "TIME IN MILLS ${calendar.timeInMillis}")

        if (type.equals(TYPE_DAILY_APP_REMINDER, ignoreCase = true)) {
            Log.d(TAG, "Daily App Reminder Enabled")
        } else {
            Log.d(TAG, "Daily Movie Release Reminder Enabled")
        }

    }

    private fun showAlarmNotification(
        context: Context,
        title: String,
        message: String?,
        notifId: Int
    ) {
        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "reminder channel"

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_active)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(CHANNEL_ID)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()

        notificationManagerCompat.notify(notifId, notification)
    }

    fun cancelReminder(context: Context, type: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val requestCode = if (type.equals(
                TYPE_DAILY_MOVIE_RELEASE_REMINDER,
                ignoreCase = true
            )
        ) ID_DAILY_MOVIE_RELEASE_REMINDER else ID_DAILY_REMINDER

        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)

        if (type.equals(TYPE_DAILY_APP_REMINDER, ignoreCase = true)) {
            Log.d(TAG, "Daily Reminder Disabled")
        } else {
            Log.d(TAG, "Daily Movie Release Reminder Disable")
        }
    }

    fun isReminderSet(context: Context, type: String): Boolean {
        val intent = Intent(context, ReminderReceiver::class.java)
        val requestCode =
            if (type.equals(
                    TYPE_DAILY_APP_REMINDER,
                    ignoreCase = true
                )
            ) ID_DAILY_REMINDER else ID_DAILY_MOVIE_RELEASE_REMINDER

        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE
        ) != null
    }
}
