package com.swirepay.android_sdk.ui.invoice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.swirepay.android_sdk.BuildConfig
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.Utility
import com.swirepay.android_sdk.ui.base.BaseActivity
import com.swirepay.android_sdk.ui.invoice.model.Invoice
import com.swirepay.android_sdk.ui.payment_activity.PaymentActivity

class InvoiceActivity : BaseActivity() {

    private var invoiceGid: String = ""

    companion object {
        const val TAG = "sdk_test"
    }

    override val param_id: String
        get() = "sp-invoice-link"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        invoiceGid = intent.getStringExtra(SwirepaySdk.INVOICE_GID).toString()

        loadUrl(
            "${BuildConfig.PAYMENT_URL}/invoice-link/$invoiceGid"
        )
    }

    override fun onRedirect(url: String?) {

        Log.d(TAG, "onRedirect: $url")

        if (result_id != null) {
            val intent = Intent().apply {
                putExtra(SwirepaySdk.RESULT, Invoice(gid = result_id))
                putExtra(SwirepaySdk.STATUS, 1)
            }
            setResult(RESULT_OK, intent)
            finish()
        } else {
            setResult(RESULT_CANCELED)
            finish()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED, Intent().apply {
            putExtra(PaymentActivity.FAILURE_REASON, PaymentActivity.FAILURE_REASON_USER_CANCELLED)
        })
        finish()
    }
}