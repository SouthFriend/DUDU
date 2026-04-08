package com.car.music.fix

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

// 车机开机自动启动修复服务
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "com.dudu.car.BOOT_COMPLETED"
        ) {
            val serviceIntent = Intent(context, CarMusicFixService::class.java)
            context.startService(serviceIntent)
        }
    }
}
