package com.car.music.fix

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    private val TAG = "BootReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            Log.d(TAG, "车机开机，启动音乐修复服务")
            val serviceIntent = Intent(context, CarMusicFixService::class.java)
            context.startService(serviceIntent)
        }
    }
}
