package com.swirepay.android_sdk

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.swirepay.android_sdk.databinding.ActivityMainBinding

class PaymentActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.webView.let {
            it.loadUrl("https://secure.swirepay.com/payment-link/paymentlink-76eff35306c244e6a89632c77df9796e")
            it.settings.javaScriptEnabled = true
            it.webViewClient  = object : WebViewClient(){
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    request?.let {
                        val redirectionUrl = request.url.toString()
                        // TODO Check the status here (api)
//                      val param = Uri.parse(url.toString()).getQueryParameter("zx")
                    }
                    //TODO return the result here
                    return true
                }
            }
        }
    }
}