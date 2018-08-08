package ssun.pe.kr.notificationexample

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.os.Build
import android.util.Log

class App : Application() {
    companion object {
        private const val TAG = "App"
    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        Log.d(TAG, "[x1210x] createNotificationChannel()")

        // 채널 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "채널1"
            val desc = "설명1"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_1", name, importance)
            channel.description = desc
            (getSystemService(NotificationManager::class.java) as NotificationManager).createNotificationChannel(channel)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "채널2"
            val desc = "설명2"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_2", name, importance)
            channel.description = desc
            (getSystemService(NotificationManager::class.java) as NotificationManager).createNotificationChannel(channel)
        }

        // 채널 그룹 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (getSystemService(NotificationManager::class.java) as NotificationManager).createNotificationChannelGroup(
                    NotificationChannelGroup("GROUP_1", "그룹1")
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "채널3"
            val desc = "설명3"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_3", name, importance)
            channel.description = desc
            channel.group = "GROUP_1"
            (getSystemService(NotificationManager::class.java) as NotificationManager).createNotificationChannel(channel)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "채널4"
            val desc = "설명4"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_4", name, importance)
            channel.description = desc
            channel.group = "GROUP_1"
            (getSystemService(NotificationManager::class.java) as NotificationManager).createNotificationChannel(channel)
        }
    }
}