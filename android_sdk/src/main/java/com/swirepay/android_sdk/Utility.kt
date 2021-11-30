package com.swirepay.android_sdk

import android.util.Base64
import android.util.Base64.encodeToString

object Utility {
    const val baseUrl = "https://www.swirepay.com"
    fun getBase24String(data : String) : String {
        return encodeToString(data.toByteArray(), Base64.DEFAULT)
    }
}