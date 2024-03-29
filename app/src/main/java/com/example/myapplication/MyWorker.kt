package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        showNotification()
        return Result.success()
    }

    private fun showNotification() {
        createNotificationChannel()

        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("WorkManager Notification")
            .setContentText("This is a notification generated by WorkManager.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(applicationContext)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    @SuppressLint("WrongConstant")
    private fun createNotificationChannel() {
        // Create a notification channel (required for Android 8.0 and above)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val name = "WorkManagerChannel"
            val descriptionText = "Channel description"
            val importance = NotificationManagerCompat.IMPORTANCE_DEFAULT
            val channel = NotificationManagerCompat.from(applicationContext).getNotificationChannel(CHANNEL_ID)
                ?: NotificationManagerCompat.from(applicationContext).createNotificationChannel(
                    NotificationChannel(CHANNEL_ID, name,importance).apply {
                        description = descriptionText
                    }
                )
        }
    }

    companion object {
        const val CHANNEL_ID = "work_manager_channel"
        const val NOTIFICATION_ID = 1
    }
}