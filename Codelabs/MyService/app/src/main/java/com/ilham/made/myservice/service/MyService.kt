package com.ilham.made.myservice.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyService : Service() {

    companion object {
        internal val TAG = MyService::class.java.simpleName
    }

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service Dijalankan...")

        GlobalScope.launch {
            delay(3000) // Delay 3 detik setelah service dijalankan
            stopSelf() // Method untuk menghentikan Service
            Log.d(TAG, "Service Dihentikan...")
        }
        return START_STICKY
        /*
        START_STICKY menandakan bahwa bila service tersebut dimatikan oleh sistem Android karena kekurangan memori,
        ia akan diciptakan kembali jika sudah ada memori yang bisa digunakan.
        Metode onStartCommand() juga akan kembali dijalankan.
         */
    }

    override fun onDestroy() { // method onDestroy akan otomatis aktif ketika service dihentikan
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }
}
