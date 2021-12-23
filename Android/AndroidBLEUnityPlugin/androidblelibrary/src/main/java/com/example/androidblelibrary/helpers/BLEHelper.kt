package com.example.androidblelibrary.helpers

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import com.example.androidblelibrary.UnityPlayerActivity

class BLEHelper private constructor() : Fragment() {

    // It's a singleton class providing BLE connectivity services for an Android Unity plugin.

    var bleValue: Int = 0

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter

    companion object {
        // Singleton instance
        private lateinit var instance: BLEHelper

        @JvmStatic
        fun getHelper() : BLEHelper {
            if (!this::instance.isInitialized) {
                instance = BLEHelper()
            }

            return instance
        }
    }

    fun initHelper(activity: Activity) {
        Log.d("BLEHelper", "Initialising helper...")
        bluetoothManager = activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        if (!bluetoothAdapter.isEnabled) {
            Log.d("BLEHelper", "Requesting Bluetooth to be enabled.")

            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == UnityPlayerActivity.RESULT_OK) {
                    Log.d("GraVRActivity", "YES! Bluetooth has been enabled.")
                }
                else {
                    Log.d("GraVRActivity", "Bluetooth has been enabled. Better luck next time bro.")
                }
            }

            startForResult.launch(enableBtIntent)

            Log.d("BLEHelper", "Request to enable Bluetooth sent.")
        }

        Log.d("BLEHelper", "Finished initialising helper.")
    }

    fun readBLE(): Int {
        bleValue += 10
        if (bleValue >= 360) {
            bleValue = 0
        }

        return bleValue
    }
}