package com.gravr.gravrblelibrary.ble

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class BLEHelper private constructor(private val activity: Activity) {
    // It's a singleton class providing BLE connectivity services for an Android Unity plugin.

    private val bluetoothManager: BluetoothManager = activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter
    private var bluetoothScanner: BluetoothLeScanner? = null    // Cannot be initialised in init() because if Bluetooth is not enabled, it will return null.

    private var bleValue: Int = 0

    private var scanning = false
    private val handler = Handler(Looper.getMainLooper())
    private val scanPeriod: Long = 10000   // Stops scanning after 10 seconds.

    private val bleScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)

            Log.d(TAG, "OnScanResult.")
            if (result != null) {
                Log.d(TAG, "New device found with name ${result.device.name}.")
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)

            Log.d(TAG, "OnScanFailed.")
        }
    }

    companion object {
        const val TAG = "BLEHelper"

        @Volatile var fineLocationPermissionGranted = false
        @Volatile var bluetoothConnectPermissionGranted = false

        // Singleton instance
        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: BLEHelper

        @JvmStatic
        fun getHelper(activity: Activity) : BLEHelper {
            if (!this::instance.isInitialized) {
                instance = BLEHelper(activity)
                Log.d(TAG, "Helper instantiated.")
            }

            return instance
        }
    }

    fun initHelper() {
        Log.d(TAG, "Initialising helper...")

        GlobalScope.launch {
            val initPeriod: Long = 3 * 60000   // Coroutine will end after 3 minutes.

            withTimeout(initPeriod) {

                requestLocationPermission()
                while (!fineLocationPermissionGranted) {    // Waiting for ACCESS_FINE_LOCATION permission to be granted.
                    delay(1000)                    // Check every second.
                    Log.d(TAG, "Waiting on user to grant ACCESS_FINE_LOCATION permission.")
                }

                requestBluetoothPermission()
                while (!bluetoothConnectPermissionGranted) {    // Waiting for BLUETOOTH_CONNECT permission to be granted.
                    delay(1000)                        // Check every second.
                    Log.d(TAG, "Waiting on user to grant BLUETOOTH_CONNECT permission.")
                }

                checkBluetoothEnabled()
                while (!bluetoothAdapter.isEnabled) {   // Waiting for Bluetooth to be enabled.
                    delay(1000)      // Check every second.
                    Log.d(TAG, "Waiting on Bluetooth to be enabled.")
                }
                Log.d(TAG, "Bluetooth enabled.")

                bluetoothScanner = bluetoothAdapter.bluetoothLeScanner

                startScanning()
                Log.d(TAG, "Started scanning for Bluetooth devices.")

                Log.d(TAG, "Finished initialising helper.")
            }
        }
    }

    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                fineLocationPermissionGranted = false

                // Creating Intent for RequestFineLocationAccessActivity
                val requestFineLocationIntent = Intent(activity, RequestFineLocationAccessActivity::class.java)

                activity.startActivity(requestFineLocationIntent)

            } else {
                // Permission has been given already.
                fineLocationPermissionGranted = true
            }

        } else {
            // Earlier versions of Android do not require expressly asking this permission.
            fineLocationPermissionGranted = true
        }
    }

    private fun requestBluetoothPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            bluetoothConnectPermissionGranted = false

            // Creating Intent for RequestFineLocationAccessActivity
            val requestBluetoothConnectIntent = Intent(activity, RequestBluetoothConnectActivity::class.java)

            activity.startActivity(requestBluetoothConnectIntent)

        } else {
            // Permission has been given already.
            bluetoothConnectPermissionGranted = true
        }
    }

    private fun checkBluetoothEnabled() {
        if (!bluetoothAdapter.isEnabled) {
            // NOTE: This is not really needed for the Quest 2 as it doesn't let you disable Bluetooth.
            Log.d(TAG, "Requesting Bluetooth to be enabled.")

            // Creating Intent for EnableBtActivity
            val requestBtEnableIntent = Intent(activity, EnableBtActivity::class.java)

            activity.startActivity(requestBtEnableIntent)

        }
    }

    private fun startScanning() {
        if (!scanning) {
            handler.postDelayed({
                stopScanning()
            }, scanPeriod)

            scanning = true
            bluetoothScanner?.startScan(bleScanCallback)
            Log.d(TAG, "Starting Bluetooth scan.")
        }
    }

    private fun stopScanning() {
        scanning = false
        bluetoothScanner?.stopScan(bleScanCallback)
        Log.d(TAG, "Stopping Bluetooth scan.")
    }

    fun connect(address: String) : Boolean {
        return try {
            val device = bluetoothAdapter.getRemoteDevice(address)
            true

        } catch (e: IllegalArgumentException) {
            Log.w(TAG, "Device not found with provided address.")
            false
        }
    }

    fun readBLE(): Int {
        bleValue += 10
        if (bleValue >= 360) {
            bleValue = 0
        }

        return bleValue
    }
}