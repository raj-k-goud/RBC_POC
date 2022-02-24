package com.test.rbcaccountpoc.utils

import android.util.Log
import com.test.rbcaccountpoc.BuildConfig


internal object Logger {

    fun debug( message: String) {
        if (BuildConfig.DEBUG)
        {
            Log.d("RBC Logs: ", message)
        }
    }
}