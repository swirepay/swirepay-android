package com.swirepay.android_sdk.ui.base

import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.Toast
import com.swirepay.android_sdk.Utility
import com.swirepay.android_sdk.databinding.ActivityPaymentBinding
import com.swirepay.android_sdk.ui.payment_activity.PaymentActivity

abstract class BaseActivity : AppCompatActivity() {
    val binding: ActivityPaymentBinding by lazy { ActivityPaymentBinding.inflate(layoutInflater) }
    abstract val param_id : String
    var result_id = ""

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
        Log.d("sdk_test", "loadUrl: $url")
        binding.webView.loadUrl(url)
        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val localUrl = request?.url.toString()
                Log.d("sdk_test", "shouldOverrideUrlLoading: $localUrl")
                val uri = request?.url
                val id = uri?.getQueryParameter(param_id)
                if(id != null){
                    result_id = id
                }
                if(isThisFinalUrl(localUrl)){
                    onRedirect(localUrl)
                    return true
                }

                return super.shouldOverrideUrlLoading(view, request)
            }

//            override fun onReceivedError(
//                view: WebView?,
//                request: WebResourceRequest?,
//                error: WebResourceError?
//            ) {
//                Log.d(PaymentActivity.TAG, "onReceivedError: ${error.toString()}")
//                super.onReceivedError(view, request, error)
//            }

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
    companion object{
        fun isThisFinalUrl(url: String?) : Boolean{
            if(url != null && url.contains(Utility.baseUrl)){
                return true
            }
            return false
        }

    }
    var backPressedTime : Long = 0
    override fun onBackPressed() {
        super.onBackPressed()
        if(System.currentTimeMillis() - backPressedTime < 2000){
            setResult(RESULT_CANCELED , Intent().apply {
                putExtra(
                    PaymentActivity.FAILURE_REASON,
                    PaymentActivity.FAILURE_REASON_USER_CANCELLED
                )
            })
            finish()
        }else{
            backPressedTime = System.currentTimeMillis()
            Toast.makeText(this , "Click back again to exit!" , Toast.LENGTH_LONG).show()
        }

    }

}