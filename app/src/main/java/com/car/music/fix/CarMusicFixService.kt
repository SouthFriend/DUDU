package com.car.music.fix

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.util.Log

class CarMusicFixService : Service() {
    private val TAG = "CarMusicFix"
    private val targetApps = arrayOf(
        "com.netease.cloudmusic",
        "com.tencent.qqmusic"
    )
    private lateinit var keyReceiver: BroadcastReceiver

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "修复服务启动")
        TaskLockUtils.startLock(this, targetApps)
        registerKeyReceiver()
    }

    private fun registerKeyReceiver() {
        keyReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
                    val reason = intent.getStringExtra("reason")
                    if (reason == "homekey" || reason == "recentapps") {
                        Log.d(TAG, "拦截到主页键/360全景返回，锁定音乐APP")
                        context?.let { TaskLockUtils.lockTargetTask(it) }
                    }
                }
            }
        }
        val filter = IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        registerReceiver(keyReceiver, filter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(keyReceiver)
        Log.d(TAG, "修复服务销毁")
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
