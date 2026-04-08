package com.car.music.fix

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.KeyEvent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter

// 通用安卓13版 - 嘟嘟梁山S3音乐返回BUG修复服务
class CarMusicFixService : Service() {
    // 需要修复的音乐APP包名（网易云+QQ音乐）
    private val targetApps = arrayOf(
        "com.netease.cloudmusic",  // 网易云音乐
        "com.tencent.qqmusic"      // QQ音乐
    )
    
    // 车机按键监听广播
    private lateinit var keyReceiver: BroadcastReceiver

    override fun onCreate() {
        super.onCreate()
        // 启动任务栈锁定
        TaskLockUtils.startLock(this, targetApps)
        // 注册安卓通用按键监听（替换嘟嘟专属SDK）
        registerKeyReceiver()
    }

    // 注册安卓通用的按键监听（适配360全景/返回键）
    private fun registerKeyReceiver() {
        keyReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val keyCode = intent?.getIntExtra("keyCode", 0) ?: 0
                val action = intent?.getIntExtra("action", 0) ?: 0
                
                // 拦截返回键/主页键（360全景返回后触发的异常按键）
                if (action == KeyEvent.ACTION_UP && 
                    (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME)) {
                    TaskLockUtils.lockTargetTask(context!!)
                }
            }
        }
        
        val filter = IntentFilter()
        filter.addAction("android.intent.action.MEDIA_BUTTON")
        filter.addAction("android.intent.action.ACTION_CLOSE_SYSTEM_DIALOGS")
        registerReceiver(keyReceiver, filter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY // 服务被杀死后自动重启（适配车机内存回收）
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(keyReceiver) // 注销广播，避免内存泄漏
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
