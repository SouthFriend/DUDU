package com.car.music.fix

import android.app.ActivityManager
import android.content.Context
import android.os.Process

// 音乐APP任务栈锁定工具（核心修复逻辑）
object TaskLockUtils {
    private lateinit var targetPackageNames: Array<String>
    private lateinit var activityManager: ActivityManager

    // 初始化锁定列表
    fun startLock(context: Context, packages: Array<String>) {
        targetPackageNames = packages
        activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    }

    // 锁定目标音乐APP任务栈，防止被系统销毁/返回
    fun lockTargetTask(context: Context) {
        val tasks = activityManager.appTasks
        for (task in tasks) {
            val baseIntent = task.taskInfo.baseIntent
            val pkg = baseIntent.component?.packageName
            // 匹配网易云/QQ音乐，强制锁定任务栈
            if (pkg in targetPackageNames) {
                task.moveToFront() // 保持前台焦点
                task.setExcludeFromRecents(false) // 不被系统回收
            }
        }
    }
}
