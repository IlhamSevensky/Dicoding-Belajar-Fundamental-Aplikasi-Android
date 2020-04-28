package com.ilham.made.fifthsubmission.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.ilham.made.fifthsubmission.R
import com.ilham.made.fifthsubmission.provider.FavoriteMovieContentProvider
import com.ilham.made.fifthsubmission.utils.Constants
import com.ilham.made.fifthsubmission.utils.Constants.Companion.COLUMN_POSTER
import java.util.concurrent.ExecutionException


internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<String>()


    override fun onCreate() {

    }

    override fun onDestroy() {

    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(p0: Int): Long = 0

    override fun onDataSetChanged() {
        mWidgetItems.clear()
        val thread: Thread = object : Thread() {
            override fun run() {
                val cursor = mContext.contentResolver.query(
                    FavoriteMovieContentProvider.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )
                cursor?.apply {
                    while (moveToNext()) {
                        mWidgetItems.add(getString(getColumnIndexOrThrow(COLUMN_POSTER)))
                    }
                }
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    override fun hasStableIds(): Boolean = false

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)

        var bmp: Bitmap? = null
        if (mWidgetItems.size > 0) {
            try {
                @Suppress("DEPRECATION")
                bmp = Glide.with(mContext)
                    .asBitmap()
                    .load(Constants.API_IMAGE_ENDPOINT + Constants.ENDPOINT_POSTER_SIZE_W185 + mWidgetItems[position])
                    .into(
                        com.bumptech.glide.request.target.Target.SIZE_ORIGINAL,
                        com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
                    )
                    .get()

            } catch (e: ExecutionException) {
                e.stackTrace
            }
        }
        if (bmp != null){
            rv.setImageViewBitmap(R.id.imageView, bmp)
        } else {
            rv.setImageViewResource(R.id.imageView, R.drawable.ic_no_image_available)
        }


        if (position > 0) {
            rv.setTextViewText(R.id.banner_text, mWidgetItems[position])
        }


        val bundle = Bundle()
        bundle.putInt(FavoriteMovieWidget.EXTRA_ITEM, position)
        val fillIntent = Intent()
        fillIntent.putExtras(bundle)
        rv.setOnClickFillInIntent(R.id.imageView, fillIntent)
        return rv
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewTypeCount(): Int = 1

}