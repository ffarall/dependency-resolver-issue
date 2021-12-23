package com.gravr.gravrblelibrary.ble

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class EnableBtActivity : AppCompatActivity() {
    // Activity created for the sole purpose of asking Bluetooth to be enabled.

    private lateinit var requestBtEnable: ActivityResultLauncher<Intent>

    companion object {
        const val TAG = "BLEHelper -> EnableBtActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onStart() {
        super.onStart()

        requestBtEnable = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                Log.d(TAG, "YES! Bluetooth has been enabled.")

                Log.d(TAG, "Continuing with BLEHelper initialisation.")
                finish()

            } else {
                Log.d(TAG, "Bluetooth has not been enabled. Better luck next time bro.")
                finish()
            }
        }

        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        requestBtEnable.launch(enableBtIntent)
        Log.d(TAG, "Request to enable Bluetooth sent.")
    }
}