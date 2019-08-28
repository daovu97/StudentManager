package com.daovu65.studentmanager

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class GrantPermission(private val activity: Activity) {
    companion object {
        const val PERMISSION_REQUEST_CODE = 1

        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

     fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

     fun requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                activity,
                "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                activity,
                permissions,
                PERMISSION_REQUEST_CODE
            )
        }
    }
}