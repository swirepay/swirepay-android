package com.swirepay.android_sdk

import android.content.Intent
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import com.swirepay.android_sdk.databinding.ActivityPaymentBinding

class PaymentActivity : AppCompatActivity() {
    lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.requestFocusFromTouch()
        binding.webView.settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        binding.webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        binding.webView.settings.setAppCacheEnabled(true)
        binding.webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        val webSettings: WebSettings = binding.webView.settings
        webSettings.domStorageEnabled = true
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSettings.useWideViewPort = true
        binding.webView.loadUrl("https://staging-secure.swirepay.com/payment-link/paymentlink-02fc322dbb6d4cb0996fb4a72c9cdc56")
        binding.webView.webViewClient  = object : WebViewClient(){
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    request?.let {
                        Log.d(TAG, "redirect: ${request.url}")
                        val redirectionUrl = request.url.toString()
                        // TODO Check the status here (api)
//                      val param = Uri.parse(url.toString()).getQueryParameter("zx")
                    }
                    //TODO return the result here
                    setResult(RESULT_OK , Intent().apply {
                        putExtra("payment_status" , 1)
                    })
                    finish()
                    return true
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    Log.d(TAG, "onReceivedError: ${error.toString()}")
                    super.onReceivedError(view, request, error)
                }

                override fun onReceivedSslError(
                    view: WebView?,
                    handler: SslErrorHandler?,
                    error: SslError?
                ) {
                    Log.d(TAG, "onReceivedErrorssl: ${error.toString()}")
                    super.onReceivedSslError(view, handler, error)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    Log.d(TAG, "onPageFinished: ")
                }
            }
    }

    companion object{
        const val TAG = "sdk_test"
    }

}