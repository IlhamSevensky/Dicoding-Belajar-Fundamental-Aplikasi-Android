package com.ilham.made.consumerfavorite.service

import android.content.Intent
import android.widget.RemoteViewsService
import com.ilham.made.consumerfavorite.widget.StackRemoteViewsFactory

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}