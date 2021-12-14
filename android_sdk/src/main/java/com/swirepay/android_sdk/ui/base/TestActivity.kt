package com.swirepay.android_sdk.ui.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.swirepay.android_sdk.R

class TestActivity : BaseActivity() {
    override val param_id: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadUrl("https://staging-secure.swirepay.com/subscription-button/subscriptionbutton-296fe6c3b73f41b79edfb783ea5a91f0")
    }

    override fun onRedirect(url: String?) {
    }
}