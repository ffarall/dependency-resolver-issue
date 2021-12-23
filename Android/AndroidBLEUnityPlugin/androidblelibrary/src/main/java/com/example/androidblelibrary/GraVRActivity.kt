package com.example.androidblelibrary

import android.os.Bundle
import android.util.Log
import com.example.androidblelibrary.helpers.BLEHelper

open class GraVRActivity : UnityPlayerActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        3
        Log.d("GraVRActivity", "On Create calling readBLE() returned ${BLEHelper.getHelper().readBLE()}")
    }
}