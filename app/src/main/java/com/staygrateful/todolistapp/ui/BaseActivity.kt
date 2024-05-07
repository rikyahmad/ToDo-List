package com.staygrateful.todolistapp.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.staygrateful.todolistapp.external.helper.AlarmSchedulerHelper

abstract class BaseActivity : AppCompatActivity() {

    private val requestAlarmPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                setAlarm()
            } else {
                Toast.makeText(this, "Izin SCHEDULE_EXACT_ALARM dibutuhkan.", Toast.LENGTH_LONG)
                    .show()
            }
        }

    private val requestPostPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Toast.makeText(this, "Izin POST_NOTIFICATIONS diperlukan.", Toast.LENGTH_LONG)
                    .show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPostPermission()
        //requestAlarmPermission()
    }

    private fun setAlarm() {
        AlarmSchedulerHelper.scheduleAlarm(this, 1, System.currentTimeMillis() + 15_000L, 3000L)
    }

    private fun requestPostPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPostPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun requestAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestAlarmPermissionLauncher.launch(Manifest.permission.SCHEDULE_EXACT_ALARM)
        } else {
            setAlarm()
        }
    }
}