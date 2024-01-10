package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MyViewModel(private var application: Application) : AndroidViewModel(application) {

    fun initializeWorkManager() {
        val periodicWorkRequest = PeriodicWorkRequestBuilder<MyWorker>(
            repeatInterval = 45,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(application).enqueueUniquePeriodicWork(
            "MyWorker",
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )
    }

    fun showFirstNotification() {
        viewModelScope.launch {
            val myWorker = OneTimeWorkRequestBuilder<MyWorker>()
                .setInitialDelay(0, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(application).enqueue(myWorker)
        }
    }
}