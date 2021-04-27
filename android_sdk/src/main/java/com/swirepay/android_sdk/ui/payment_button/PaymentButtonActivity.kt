package com.swirepay.android_sdk.ui.payment_button

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.swirepay.android_sdk.R
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.model.PaymentButtonRequest
import com.swirepay.android_sdk.ui.base.BaseActivity
import com.swirepay.android_sdk.ui.payment_activity.PaymentActivity
import com.swirepay.android_sdk.ui.subscription_button.CustomPaymentButtonViewModelProvider
import com.swirepay.android_sdk.ui.subscription_button.CustomSubscriptionButtonViewModelProvider
import com.swirepay.android_sdk.ui.subscription_button.SubscriptionButtonActivity
import com.swirepay.android_sdk.ui.subscription_button.ViewModelSubscriptionButton

class PaymentButtonActivity : BaseActivity() {
    companion object{
        const val PAYMENT_BUTTON_REQUEST = "paymentButtonRequest"
    }
    val viewModel :  ViewModelPaymentButton by lazy {
        val request = intent.getParcelableExtra<PaymentButtonRequest>(PAYMENT_BUTTON_REQUEST)
        ViewModelProvider(this , CustomPaymentButtonViewModelProvider(request!!)).get(
            ViewModelPaymentButton::class.java)
    }
    override val param_id: String
        get() = "sp-payment-button"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.liveData.observe(this , {
            loadUrl(it.link)
        })
        viewModel.liveErrorMessages.observe(this ,{
            setResult(RESULT_CANCELED , Intent().apply {
                putExtra(
                    PaymentActivity.FAILURE_REASON,
                    PaymentActivity.PAYMENT_FAILURE_REASON_API_FAILURE
                )
                putExtra(PaymentActivity.PAYMENT_MESSAGE, it)
            })
            finish()
        })
        viewModel.liveResult.observe(this , {
            val intent = Intent().apply {
                putExtra(SubscriptionButtonActivity.RESULT, it)
                putExtra(SwirepaySdk.STATUS, 1)
            }
            setResult(RESULT_OK , intent)
            finish()
        })
    }

    override fun onRedirect(url: String?) {
        if(result_id.isNotEmpty()){
            Log.d("result_test", "onRedirect:$result_id ")
            viewModel.getPaymentButton(result_id)
        }
        else {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}