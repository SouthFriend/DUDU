package com.car.music.fix

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.KeyEvent
import com.dudu.car.CarKeyEvent

// 嘟嘟梁山S3 音乐返回BUG核心修复服务
class CarMusicFixService : Service() {
    // 需要修复的音乐APP包名（网易云+QQ音乐）
    private val targetApps = arrayOf(
        "com.netease.cloudmusic",  // 网易云音乐
        "com.tencent.qqmusic"      // QQ音乐
    )

    override fun onCreate() {
        super.onCreate()
        // 启动任务栈锁定
        TaskLockUtils.startLock(this, targetApps)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 监听车机360全景切换、按键事件
        registerCarKeyListener()
        return START_STICKY // 服务被杀死后自动重启
    }

    // 监听嘟嘟梁山车机专属按键/360全景事件
    private fun registerCarKeyListener() {
        CarKeyEvent.setOnKeyListener { keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP) {
                when (keyCode) {
                    // 拦截转向/360全景返回后的异常返回键
                    KeyEvent.KEYCODE_BACK,
                    KeyEvent.KEYCODE_HOME -> {
                        // 仅锁定音乐APP，不拦截正常操作
                        TaskLockUtils.lockTargetTask(this)
                        true
                    }
                }
            }
            false
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
