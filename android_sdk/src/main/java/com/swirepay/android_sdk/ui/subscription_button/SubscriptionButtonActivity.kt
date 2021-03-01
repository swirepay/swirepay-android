package com.swirepay.android_sdk.ui.subscription_button

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.ui.base.BaseActivity
import com.swirepay.android_sdk.ui.payment_activity.PaymentActivity

class SubscriptionButtonActivity : BaseActivity() {
    private val viewModel : ViewModelSubscriptionButton by lazy {
        val name = intent.getStringExtra(SwirepaySdk.PLAN_NAME)!!
        val description = intent.getStringExtra(SwirepaySdk.PLAN_DESCRIPTION)!!
        val amount = intent.getIntExtra(SwirepaySdk.PLAN_AMOUNT , 0)
        val billPeriod = intent.getIntExtra(SwirepaySdk.PLAN_BILLING_PERIOD , 0)
        val billFreq = intent.getStringExtra(SwirepaySdk.PLAN_BILLING_FREQ)!!
        val currencyCode = intent.getStringExtra(SwirepaySdk.PLAN_CURRENCY_CODE)!!
        val planStartTime = intent.getStringExtra(SwirepaySdk.PLAN_START_DATE)!!
        ViewModelProvider(this , CustomSubscriptionButtonViewModelProvider(name , amount, description, currencyCode , billFreq , billPeriod , planStartTime)).get(ViewModelSubscriptionButton::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.liveSubscriptionButton.observe(this , Observer {
            loadUrl(it.link)
        })

        viewModel.liveResult.observe(this , Observer {
            val intent = Intent().apply {
                putExtra(RESULT , it)
                putExtra(SwirepaySdk.STATUS, 1)
            }
            setResult(RESULT_OK , intent)
            finish()
        })

        viewModel.liveErrorMessages.observe(this , {
            setResult(RESULT_CANCELED , Intent().apply {
                putExtra(
                    PaymentActivity.FAILURE_REASON,
                    PaymentActivity.PAYMENT_FAILURE_REASON_API_FAILURE
                )
                putExtra(PaymentActivity.PAYMENT_MESSAGE, it)
            })
            finish()
        })
    }

    override fun onRedirect(url: String?) {
        Log.d("sdk_test", "onRedirect: $url")
        val uri = Uri.parse(url)
        val id = uri.getQueryParameter("SpSubscriptionnbutton")
        if(id != null)
        viewModel.fetchSubscriptionButton(id)
        else {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    companion object{
        const val RESULT = "result"
    }

}