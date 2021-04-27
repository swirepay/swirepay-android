package com.swirepay.swirepay_sdk_playground

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.model.CurrencyType
import com.swirepay.android_sdk.model.NotificationType
import com.swirepay.android_sdk.model.PaymentMethodType
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SwirepaySdk.initSdk("sk_secret_key")
        setContentView(R.layout.activity_main)
        val button: Button = findViewById(R.id.btnPayment)
        button.setOnClickListener {
            val listOfPaymentMethods = ArrayList<PaymentMethodType>().apply {
                add(PaymentMethodType.CARD)
            }
            SwirepaySdk.createPaymentLink(
                this, 10000, CurrencyType.INR, REQUEST_CODE,
                listOfPaymentMethods
            )
        }
        val btnSubscriptionButton: Button = findViewById(R.id.btnSubscriptionButton)
        btnSubscriptionButton.setOnClickListener {
            val couponId = null
//            val couponId = "coupon-4c4c5e149c884a0daff87f4c4407b95d"
            val listTaxRates = ArrayList<String>().apply {
                add("taxrates-05bd9db4868d4bd590505d89d1433a07")
            }
            val time = Calendar.getInstance()
            time.add(Calendar.DATE, 1)
            SwirepaySdk.createSubscriptionButton(
                this,
                "test1",
                100000,
                "test description",
                CurrencyType.INR,
                "MONTH",
                1,
                REQUEST_CODE_SUBSCRIPTION_BUTTON,
                time.time, couponId, listTaxRates, 1, 12
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
        }
    }

    companion object {
        const val REQUEST_CODE = 1001
        const val REQUEST_CODE_SUBSCRIPTION_BUTTON = 1002
        const val REQUEST_CODE_PAYMENT_METHOD = 1003
        const val REQUEST_CODE_CONNECT_ACCOUNT = 1004
        const val REQUEST_CODE_PAYMENT_BUTTON = 1005
    }

}