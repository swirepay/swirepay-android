package com.swirepay.android_sdk.ui.invoice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.model.InvoiceRequest
import com.swirepay.android_sdk.ui.base.BaseActivity
import com.swirepay.android_sdk.ui.payment_activity.PaymentActivity

class InvoiceActivity : BaseActivity() {

    val viewModel: ViewModelInvoice by lazy {
        val invoiceRequest = intent.getParcelableExtra<InvoiceRequest>(INVOICE_REQUEST)

        ViewModelProvider(
            this, InvoiceViewModelProvider(
                invoiceRequest!!
            )
        ).get(ViewModelInvoice::class.java)
    }

    companion object {
        const val INVOICE_REQUEST = "invoiceRequest"
        const val TAG = "sdk_test"
    }

    override val param_id: String
        get() = "sp-invoice"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.liveInvoiceLink.observe(this, {
            loadUrl(it.invoiceLinks[0].link)
            Log.d(TAG, "onCreate: ${it.invoiceLinks[0].link}")
            binding.progress.visibility = View.GONE
        })

        viewModel.liveErrorMessages.observe(this, {
            setResult(RESULT_CANCELED, Intent().apply {
                putExtra(PaymentActivity.FAILURE_REASON, PaymentActivity.PAYMENT_FAILURE_REASON_API_FAILURE)
                putExtra(PaymentActivity.PAYMENT_MESSAGE, it)
            })
            finish()
        })

        viewModel.liveInvoiceResults.observe(this, {
            val intent = Intent().apply {
                putExtra(SwirepaySdk.RESULT, it)
                putExtra(SwirepaySdk.STATUS, 1)
            }
            setResult(RESULT_OK, intent)
            finish()
        })
    }

    override fun onRedirect(url: String?) {

        Log.d(TAG, "onRedirect: $url")
        viewModel.checkInvoiceStatus()
        binding.progress.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED , Intent().apply {
            putExtra(PaymentActivity.FAILURE_REASON, PaymentActivity.FAILURE_REASON_USER_CANCELLED)
        })
        finish()
    }
}