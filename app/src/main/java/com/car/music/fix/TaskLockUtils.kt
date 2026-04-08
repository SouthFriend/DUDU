package com.car.music.fix

import android.app.ActivityManager
import android.content.Context
import android.util.Log

object TaskLockUtils {
    private val TAG = "TaskLockUtils"
    private lateinit var targetPackageNames: Array<String>
    private lateinit var activityManager: ActivityManager

    fun startLock(context: Context, packages: Array<String>) {
        targetPackageNames = packages
        activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    }

    fun lockTargetTask(context: Context) {
        val appTasks = activityManager.appTasks
        for (task in appTasks) {
            val pkg = task.taskInfo.baseIntent?.component?.packageName
            if (pkg in targetPackageNames) {
                task.moveToFront()
                Log.d(TAG, "已锁定音乐APP: $pkg")
            }
        }
    }
}
