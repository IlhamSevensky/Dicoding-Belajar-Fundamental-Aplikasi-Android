package com.ilham.made.fifthsubmission.service

import android.content.Intent
import android.widget.RemoteViewsService
import com.ilham.made.fifthsubmission.widget.StackRemoteViewsFactory

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}