package com.swirepay.swirepay_sdk_playground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.swirepay.android_sdk.SwirepaySdk

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sdk = SwirepaySdk().secretKey
    }
}