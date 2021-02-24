package com.swirepay.android_sdk.ui.base

import android.content.Intent
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import com.swirepay.android_sdk.databinding.ActivityPaymentBinding
import com.swirepay.android_sdk.ui.payment_activity.PaymentActivity

abstract class BaseActivity : AppCompatActivity() {
    val binding: ActivityPaymentBinding by lazy { ActivityPaymentBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    abstract fun onRedirect(url : String?)

    fun loadUrl(url : String) {
        binding.progress.visibility = View.GONE
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.requestFocusFromTouch()
//        binding.webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        binding.webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        val webSettings: WebSettings = binding.webView.settings
        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true
        webSettings.useWideViewPort = true
        binding.webView.loadUrl(url)
        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                onRedirect(request?.url.toString())
                return true
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                Log.d(PaymentActivity.TAG, "onReceivedError: ${error.toString()}")
                super.onReceivedError(view, request, error)
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                Log.d(PaymentActivity.TAG, "onReceivedErrorssl: ${error.toString()}")
                super.onReceivedSslError(view, handler, error)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d(PaymentActivity.TAG, "onPageFinished: ")
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED , Intent().apply {
            putExtra(
                PaymentActivity.FAILURE_REASON,
                PaymentActivity.FAILURE_REASON_USER_CANCELLED
            )
        })
        finish()
    }

}