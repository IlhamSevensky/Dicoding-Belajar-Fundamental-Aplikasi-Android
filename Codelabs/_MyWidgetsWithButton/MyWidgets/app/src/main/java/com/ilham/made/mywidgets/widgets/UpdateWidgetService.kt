package com.ilham.made.mywidgets.widgets

import android.app.job.JobParameters
import android.app.job.JobService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.widget.RemoteViews
import com.ilham.made.mywidgets.R

class UpdateWidgetService : JobService() {
    override fun onStopJob(jobParameters: JobParameters): Boolean {
        val manager = AppWidgetManager.getInstance(this)
        val view = RemoteViews(packageName, R.layout.random_number_widget)
        val theWidget =  ComponentName(this, RandomNumberWidget::class.java)
        val lastUpdate = "Random : " + NumberGenerator.generate(100)
        view.setTextViewText(R.id.appwidget_text, lastUpdate)
        manager.updateAppWidget(theWidget, view)
        jobFinished(jobParameters, false)
        return true
    }

    override fun onStartJob(jobParameters: JobParameters): Boolean {
        return true
    }

}