package com.swirepay.android_sdk.ui.payment_activity

import android.content.Intent
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.swirepay.android_sdk.BuildConfig
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.databinding.ActivityPaymentBinding
import com.swirepay.android_sdk.model.PaymentLink

class PaymentActivity : AppCompatActivity() {
    lateinit var binding: ActivityPaymentBinding
    val viewModel : ViewModelPayment by lazy {
        val amount = intent.getIntExtra("" , 0)
        ViewModelProvider(this , CustomCustomerDetailsViewModelProvider(100 ,
            SwirepaySdk.CurrencyType.INR
        )).get(ViewModelPayment::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.livePaymentLink.observe(this , Observer {
            setWebView(it)
            binding.progress.visibility = View.GONE
        })
        viewModel.liveErrorMessages.observe(this , Observer { message ->
            setResult(RESULT_CANCELED , Intent().apply {
                viewModel.livePaymentLink.value?.let {
                    putExtra(PAYMENT_RESULT , it)
                }
                putExtra(PAYMENT_FAILURE_REASON , PAYMENT_FAILURE_REASON_API_FAILURE)
            })
            finish()
        })
        viewModel.livePaymentResults.observe(this , Observer {
            setResult(RESULT_OK , Intent().apply {
                putExtra(PAYMENT_STATUS , 1)
                putExtra(PAYMENT_RESULT , it)
            })
            finish()
        })
    }

    private fun setWebView(paymentLink: PaymentLink){
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.requestFocusFromTouch()
//        binding.webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        binding.webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        val webSettings: WebSettings = binding.webView.settings
        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true
        webSettings.useWideViewPort = true
        val url : String = "${BuildConfig.PAYMENT_URL}/payment-link/${paymentLink.gid}"
        Log.d(TAG, "setWebView: url => $url")
        binding.webView.loadUrl(url)
        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.webViewClient  = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                request?.let {
                    Log.d(TAG, "redirect: ${request.url}")
                    val redirectionUrl = request.url.toString()
                    // TODO Check the status here (api)
                    // val param = Uri.parse(url.toString()).getQueryParameter("zx")
                }
                viewModel.checkPaymentStatus(paymentLink.gid)
                binding.progress.visibility = View.VISIBLE
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

    override fun onBackPressed() {
        setResult(RESULT_CANCELED , Intent().apply {
            putExtra(PAYMENT_FAILURE_REASON , PAYMENT_FAILURE_REASON_USER_CANCELLED)
        })
        finish()
    }

    companion object{
        const val TAG = "sdk_test"
        const val PAYMENT_RESULT = "payment_result"
        const val PAYMENT_STATUS = "payment_status"
        const val PAYMENT_FAILURE_REASON = "payment_failure_reason"
        const val PAYMENT_FAILURE_REASON_USER_CANCELLED = "user_cancelled"
        const val PAYMENT_FAILURE_REASON_API_FAILURE = "api_failure"
    }

}