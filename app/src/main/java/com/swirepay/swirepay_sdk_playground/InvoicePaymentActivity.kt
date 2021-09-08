package com.swirepay.swirepay_sdk_playground

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.snackbar.Snackbar
import com.swirepay.android_sdk.KeyNotInitializedException
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.ui.payment_activity.PaymentActivity
import com.swirepay.swirepay_sdk_playground.databinding.ActivityInvoicePaymentBinding

class InvoicePaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInvoicePaymentBinding

    companion object {
        const val REQUEST_CODE_INVOICE = 1006
        const val TAG = "invoice_sdk_test"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoicePaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etInvoiceGid.setText("invoicelink-02f9c3ade85746a28007d6fe44efdc3a")

        binding.btnInvoice.setOnClickListener {

            val invoiceGid = binding.etInvoiceGid.text.toString()

            if (!TextUtils.isEmpty(invoiceGid)) {
                try {
                    SwirepaySdk.showInvoicePayment(
                        this, REQUEST_CODE_INVOICE, invoiceGid
                    )
                } catch (ke: KeyNotInitializedException) {
                    Toast.makeText(this, "Key not initialized!", Toast.LENGTH_SHORT).show()
                }

            } else
                Snackbar.make(
                    findViewById(R.id.root), R.string.invoicelink_error, Snackbar.LENGTH_SHORT
                ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = SwirepaySdk.getInvoice(resultCode, data)
        Log.d(TAG, "onActivityResult: $result")
        binding.tvResult.text = result.toString()
        binding.tvResponse.text = result.entity.toString()
    }
}