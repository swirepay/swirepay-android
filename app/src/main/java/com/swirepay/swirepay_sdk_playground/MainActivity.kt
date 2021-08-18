package com.swirepay.swirepay_sdk_playground

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.model.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SwirepaySdk.initSdk("sk_live_dk52KE0juj272uX28s8mPvC3e9U6MnxK")
        setContentView(R.layout.activity_main)
        val button: Button = findViewById(R.id.btnPayment)
        button.setOnClickListener {
            val listOfPaymentMethods = ArrayList<PaymentMethodType>().apply {
                add(PaymentMethodType.CARD)
            }
            SwirepaySdk.createPaymentLink(
                this,
                10000,
                CurrencyType.INR,
                REQUEST_CODE,
                listOfPaymentMethods,
                email = "testaccountowner-stag+457@swirepay.com",
                phoneNo = "+919159620464",
                notificationType = NotificationType.SMS,
                name = "test name"
            )
        }
        val btnSubscriptionButton: Button = findViewById(R.id.btnSubscriptionButton)
        btnSubscriptionButton.setOnClickListener {
            val couponId = null
//            val couponId = "coupon-4c4c5e149c884a0daff87f4c4407b95d"
            val listTaxRates = ArrayList<String>().apply {
//                add("taxrates-05bd9db4868d4bd590505d89d1433a07")
            }
            val time = Calendar.getInstance()
            time.add(Calendar.DATE, 1)
            SwirepaySdk.createSubscriptionButton(
                this,
                "test3",
                2000,
                "test desc 1",
                CurrencyType.USD,
                "MONTH",
                1,
                REQUEST_CODE_SUBSCRIPTION_BUTTON,
                time.time, couponId, listTaxRates, 2, 12
            )
        }
        val btnPaymentMethod: Button = findViewById(R.id.btnPaymentMethod)
        btnPaymentMethod.setOnClickListener {
            SwirepaySdk.createPaymentMethod(this, REQUEST_CODE_PAYMENT_METHOD)
        }
        val btnCreateAccount: Button = findViewById(R.id.btnAccount)
        btnCreateAccount.setOnClickListener {
            SwirepaySdk.createAccount(this, REQUEST_CODE_CONNECT_ACCOUNT)
        }
        val btnPaymentButton: Button = findViewById(R.id.btnPaymentButton)
        btnPaymentButton.setOnClickListener {
            val listOfPaymentMethods = ArrayList<PaymentMethodType>().apply {
                add(PaymentMethodType.CARD)
            }
            SwirepaySdk.createPaymentButton(
                this,
                REQUEST_CODE_PAYMENT_BUTTON,
                10000,
                "TEST",
                notificationType = NotificationType.ALL,
                listOfPaymentMethods = listOfPaymentMethods,
                currencyType = CurrencyType.USD
            )
        }

        val btnInvoice: Button = findViewById(R.id.btnInvoice)
        btnInvoice.setOnClickListener {

            val taxRatesList = ArrayList<String>().apply {
            }

            val invoiceLineItems = InvoiceLineItems(
                "100000",
                "Product 2",
                "Product 2",
                "1",
                "",
                "1000.00",
                CurrencyType.USD
            )

            val invoiceLineItemDtos = InvoiceLineItemDtos(
                "100000",
                "Product 2",
                "Product 2",
                "1",
                "",
                "1000.00",
                CurrencyType.USD
            )

            val invoiceLineItemsList = ArrayList<InvoiceLineItems>().apply {
                add(invoiceLineItems)
            }

            val invoiceLineItemDtosList = ArrayList<InvoiceLineItemDtos>().apply {
                add(invoiceLineItemDtos)
            }

            SwirepaySdk.createInvoice(
                this, REQUEST_CODE_INVOICE,
                CurrencyType.USD,
                "customer-eaa0b7bdfa28469e8ce07115899a9f0f",
                "coupon-95eaecf56e514b3d98e59397f81d7645",
                "3",
                "2021-08-19T07:00:00",
                "2021-08-18T07:00:00",
                100000,
                "ACTIVE",
                "Invoice 1708",
                taxRatesList,
                "",
                null,
                null,
                null,
                null,
                invoiceLineItemsList,
                invoiceLineItemDtosList
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val resultText = findViewById<TextView>(R.id.tvResult)
        val responseText = findViewById<TextView>(R.id.tvResponse)
        when (requestCode) {
            REQUEST_CODE -> {
                val paymentResult = SwirepaySdk.getPaymentLink(resultCode, data)
                Log.d("sdk_test", "onActivityResult: $paymentResult")
                resultText.text = paymentResult.entity.toString()
                responseText.text = paymentResult.toString()
            }
            REQUEST_CODE_SUBSCRIPTION_BUTTON -> {
                val result = SwirepaySdk.getSubscriptionButton(resultCode, data)
                Log.d("sdk_test", "onActivityResult: $result")
                resultText.text = result.toString()
                responseText.text = result.entity.toString()
            }
            REQUEST_CODE_PAYMENT_METHOD -> {
                val result = SwirepaySdk.getPaymentMethod(resultCode, data)
                Log.d("sdk_test", "onActivityResult: $result")
                resultText.text = result.toString()
                responseText.text = result.entity.toString()
            }
            REQUEST_CODE_CONNECT_ACCOUNT -> {
                val result = SwirepaySdk.getAccount(resultCode, data)
                Log.d("sdk_test", "onActivityResult: $result")
                resultText.text = result.toString()
                responseText.text = result.entity.toString()
            }
            REQUEST_CODE_PAYMENT_BUTTON -> {
                val result = SwirepaySdk.getPaymentButton(resultCode, data)
                Log.d("sdk_test", "onActivityResult: $result")
                resultText.text = result.toString()
                responseText.text = result.entity.toString()
            }
            REQUEST_CODE_INVOICE -> {
                val result = SwirepaySdk.getInvoice(resultCode, data)
                Log.d("sdk_test", "onActivityResult: $result")
                resultText.text = result.toString()
                responseText.text = result.entity.toString()
            }
        }
    }

    companion object {
        const val REQUEST_CODE = 1001
        const val REQUEST_CODE_SUBSCRIPTION_BUTTON = 1002
        const val REQUEST_CODE_PAYMENT_METHOD = 1003
        const val REQUEST_CODE_CONNECT_ACCOUNT = 1004
        const val REQUEST_CODE_PAYMENT_BUTTON = 1005
        const val REQUEST_CODE_INVOICE = 1006
    }

}