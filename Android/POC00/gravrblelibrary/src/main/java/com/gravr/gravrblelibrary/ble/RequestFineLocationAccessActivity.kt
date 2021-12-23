package com.gravr.gravrblelibrary.ble

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class RequestFineLocationAccessActivity : AppCompatActivity() {

    private lateinit var requestPermission: ActivityResultLauncher<String>

    companion object {
        const val TAG = "BLEHelper -> RequestFineLocationAccessActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                BLEHelper.fineLocationPermissionGranted = true
                Log.d(TAG, "ACCESS_FINE_LOCATION permission granted.")
                finish()

            } else {
                BLEHelper.fineLocationPermissionGranted = false
                Log.d(TAG, "ACCESS_FINE_LOCATION permission denied.")
                finish()
            }
        }

        requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        Log.d(TAG, "ACCESS_FINE_LOCATION permission requested.")
    }

}