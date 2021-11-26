package com.swirepay.android_sdk.checkout.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.webkit.*
import androidx.appcompat.widget.Toolbar
import com.swirepay.android_sdk.R
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.checkout.utils.StatusbarUtil
import com.swirepay.android_sdk.databinding.ActivityPaymentActionBinding
import com.swirepay.android_sdk.ui.payment_activity.PaymentActivity

class PaymentActionActivity : AppCompatActivity() {

    lateinit var binding: ActivityPaymentActionBinding
    var amount: Int = 0
    lateinit var paymentStatus: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentActionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        StatusbarUtil.changeStatusbarColor(this.window)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        supportActionBar?.title = "Order"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor(SwirepaySdk.TOOLBAR_COLOR)))

        val actionUrl = intent.getStringExtra(SwirepaySdk.PAYMENT_METHOD_URL)
        amount = intent.getIntExtra(SwirepaySdk.PAYMENT_AMOUNT, 0)
        toolbar.setTitleTextColor(Color.parseColor(SwirepaySdk.TOOLBAR_ITEM))

        loadUrl(actionUrl.toString())
    }

    fun loadUrl(url: String) {
        binding.webView.clearCache(true)
        binding.webView.clearHistory()
        binding.progress.visibility = View.VISIBLE
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.requestFocusFromTouch()
        binding.webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        val webSettings: WebSettings = binding.webView.settings
        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true
        webSettings.useWideViewPort = true
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        Log.d("sdk_test", "loadUrl: $url")
        binding.webView.loadUrl(url)
        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val localUrl = request?.url.toString()
                Log.e("sdk_test", "shouldOverrideUrlLoading: $localUrl")
                val uri = request?.url

                val id = uri?.getQueryParameter("status")
                if (id != null) {
                    paymentStatus = id
                }
                if (isThisFinalUrl(localUrl)) {
                    loadUrl(localUrl)

                    Handler(Looper.getMainLooper()).postDelayed({
                        finish()
                        startActivity(
                            Intent(
                                applicationContext,
                                PaymentStatusActivity::class.java
                            ).apply {
                                putExtra(SwirepaySdk.PAYMENT_AMOUNT, amount)
                                putExtra(SwirepaySdk.PAYMENT_STATUS, paymentStatus)
                            })
                    }, 1500)
                    return true
                }

                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                Log.d(PaymentActivity.TAG, "onReceivedError: $error")
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
                binding.progress.visibility = View.GONE
                Log.d(PaymentActivity.TAG, "onPageFinished: ")
            }
        }
    }

    companion object {
        fun isThisFinalUrl(url: String?): Boolean {
            if (url != null && url.contains("/complete")) {
                return true
            }
            return false
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menu!!.findItem(android.R.id.home)?.setIcon(R.drawable.ic_close)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return false
//        }
//
//        return super.onKeyDown(keyCode, event)
//    }
}