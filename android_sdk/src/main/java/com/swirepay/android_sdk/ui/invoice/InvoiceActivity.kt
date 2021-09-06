package com.swirepay.android_sdk.ui.invoice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.swirepay.android_sdk.BuildConfig
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.Utility
import com.swirepay.android_sdk.ui.base.BaseActivity
import com.swirepay.android_sdk.ui.invoice.model.Invoice
import com.swirepay.android_sdk.ui.payment_activity.CustomCustomerDetailsViewModelProvider
import com.swirepay.android_sdk.ui.payment_activity.PaymentActivity
import com.swirepay.android_sdk.ui.payment_activity.ViewModelPayment
import com.swirepay.android_sdk.ui.payment_activity.model.CustomerModel

class InvoiceActivity : BaseActivity() {

    private var invoiceGid: String = ""

    val viewModel: ViewModelInvoice by lazy {
        ViewModelProvider(
            this
        ).get(ViewModelInvoice::class.java)
    }

    override val param_id: String
        get() = "sp-invoice-link"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        invoiceGid = intent.getStringExtra(SwirepaySdk.INVOICE_GID).toString()

        loadUrl(
            "${BuildConfig.PAYMENT_URL}/invoice-link/$invoiceGid"
        )

        viewModel.liveInvoiceResults.observe(this, Observer {
            setResult(RESULT_OK, Intent().apply {
                putExtra(SwirepaySdk.STATUS, 1)
                putExtra(SwirepaySdk.RESULT, it)
            })
            finish()
        })
    }

    override fun onRedirect(url: String?) {

        Log.d(TAG, "onRedirect: $url")

        if (result_id.isNotEmpty())
            viewModel.checkInvoiceStatus(result_id)
        binding.progress.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED, Intent().apply {
            putExtra(FAILURE_REASON, FAILURE_REASON_USER_CANCELLED)
        })
        finish()
    }

    companion object {
        const val TAG = "invoice_sdk_test"
        const val FAILURE_REASON = "payment_failure_reason"
        const val FAILURE_REASON_USER_CANCELLED = "user_cancelled"
    }
}

