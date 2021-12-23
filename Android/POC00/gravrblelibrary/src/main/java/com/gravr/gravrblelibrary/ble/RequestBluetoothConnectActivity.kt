package com.gravr.gravrblelibrary.ble

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class RequestBluetoothConnectActivity : AppCompatActivity() {

    private lateinit var requestPermission: ActivityResultLauncher<String>

    companion object {
        const val TAG = "BLEHelper -> RequestBluetoothConnectActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                BLEHelper.bluetoothConnectPermissionGranted = true
                Log.d(TAG, "BLUETOOTH_CONNECT permission granted.")
                finish()

            } else {
                BLEHelper.bluetoothConnectPermissionGranted = false
                Log.d(TAG, "BLUETOOTH_CONNECT permission denied.")
                finish()
            }
        }

        requestPermission.launch(Manifest.permission.BLUETOOTH)
        Log.d(TAG, "BLUETOOTH_CONNECT permission requested.")
    }
}