package com.swirepay.android_sdk.ui.payment_activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.ui.base.BaseActivity
import com.swirepay.android_sdk.ui.payment_activity.model.CustomerModel

class PaymentActivity : BaseActivity() {

    val viewModel: ViewModelPayment by lazy {
        val amount = intent.getIntExtra(SwirepaySdk.PAYMENT_AMOUNT, 0)
        val currency = intent.getStringExtra(SwirepaySdk.PAYMENT_CURRENCY)
        val customer = intent.getParcelableExtra<CustomerModel>(SwirepaySdk.PAYMENT_CUSTOMER)
        val notificationType = intent.getStringExtra(SwirepaySdk.NOTIFICATION_TYPE)
        val customerGid = intent.getStringExtra(SwirepaySdk.PAYMENT_CUSTOMER_GID)
        val list = intent.getStringArrayListExtra(SwirepaySdk.PAYMENT_METHOD_TYPES)
        val dueDate = intent.getStringExtra(SwirepaySdk.DUE_DATE)
        ViewModelProvider(
            this, CustomCustomerDetailsViewModelProvider(
                amount,
                currency!!,
                list!!,
                customer,
                customerGid,
                notificationType = notificationType!!,
                dueDate,
            )
        ).get(ViewModelPayment::class.java)
    }
    override val param_id: String
        get() = "sp-payment-link"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        viewModel.livePaymentLink.observe(this, {
            loadUrl(it.link)
            Log.d(TAG, "onCreate: ${it.link}")
            binding.progress.visibility = View.GONE

        })
        viewModel.liveErrorMessages.observe(this, Observer { message ->
            setResult(RESULT_CANCELED, Intent().apply {
                viewModel.livePaymentLink.value?.let {
                    putExtra(SwirepaySdk.RESULT, it)
                }
                putExtra(FAILURE_REASON, PAYMENT_FAILURE_REASON_API_FAILURE)
                putExtra(PAYMENT_MESSAGE, message)
            })
            finish()
        })
        viewModel.livePaymentResults.observe(this, Observer {
            setResult(RESULT_OK, Intent().apply {
                putExtra(SwirepaySdk.STATUS, 1)
                putExtra(SwirepaySdk.RESULT, it)
            })
            finish()
        })
    }

    override fun onRedirect(url: String?) {
        Log.d(TAG, "onRedirect: $url")
        viewModel.checkPaymentStatus()
        binding.progress.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED, Intent().apply {
            putExtra(FAILURE_REASON, FAILURE_REASON_USER_CANCELLED)
        })
        finish()
    }

    companion object {
        const val TAG = "sdk_test"
        const val PAYMENT_MESSAGE = "payment_message"
        const val FAILURE_REASON = "payment_failure_reason"
        const val FAILURE_REASON_USER_CANCELLED = "user_cancelled"
        const val PAYMENT_FAILURE_REASON_API_FAILURE = "api_failure"
    }

}