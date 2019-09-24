package com.projet.azzed.androidvolley.utils

import android.util.Log

object LogsUtils {


    fun longLog(str: String) {
        if (str.length > 4000) {
            Log.d("tryhard", str.substring(0, 4000))
            longLog(str.substring(4000))
        } else
            Log.d("tryhard", str)
    }

}