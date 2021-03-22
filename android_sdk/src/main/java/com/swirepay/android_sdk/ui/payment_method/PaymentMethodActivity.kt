package com.swirepay.android_sdk.ui.payment_method

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.swirepay.android_sdk.BuildConfig
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.SwirepaySdk.RESULT
import com.swirepay.android_sdk.Utility
import com.swirepay.android_sdk.ui.base.BaseActivity
import com.swirepay.android_sdk.ui.payment_activity.PaymentActivity

class PaymentMethodActivity : BaseActivity() {
    val viewModel : ViewModelPaymentMethod by lazy {
        ViewModelProvider(this).get(ViewModelPaymentMethod::class.java)
    }
    override val param_id: String
        get() = "sp-session-id"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadUrl("${BuildConfig.PAYMENT_URL}/setup-session?key=${Utility.getBase24String(SwirepaySdk.apiKey!!)}")

        viewModel.liveErrorMessages.observe(this , Observer {
            setResult(RESULT_CANCELED , Intent().apply {
                putExtra(
                    PaymentActivity.FAILURE_REASON,
                    PaymentActivity.PAYMENT_FAILURE_REASON_API_FAILURE
                )
                putExtra(PaymentActivity.PAYMENT_MESSAGE, it)
            })
            finish()
        })

        viewModel.liveResult.observe(this, Observer {
            val intent = Intent().apply {
                putExtra(RESULT, it)
                putExtra(SwirepaySdk.STATUS, 1)
            }
            setResult(RESULT_OK , intent)
            finish()
        })
    }

    //sp-session-id
    override fun onRedirect(url: String?) {
        Log.d("sdk_test", "onRedirect: $url")
        if(result_id.isNotEmpty())
            viewModel.fetchSetupSession(result_id)
        else {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

}