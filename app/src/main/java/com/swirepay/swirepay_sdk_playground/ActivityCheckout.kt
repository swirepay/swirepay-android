package com.swirepay.swirepay_sdk_playground

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.checkout.model.*
import com.swirepay.android_sdk.model.PaymentMethodType
import com.swirepay.android_sdk.model.PaymentSession
import com.swirepay.swirepay_sdk_playground.databinding.CheckoutActivityBinding

class ActivityCheckout : AppCompatActivity() {

    lateinit var binding: CheckoutActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CheckoutActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        Test
//        SwirepaySdk.initSdk("")

//        Live
        SwirepaySdk.initSdk("")

        val billingAddress = SPBillingAddress("Test street", "Chennai", "TN", "600030", "IN")
        val shippingAddress = SPShippingAddress("Test street", "Chennai", "TN", "600030", "IN")
        val customer = SPCustomer(
            "Muthu", "testaccountowner-stag+592@swirepay.com", "+919845789562",
            billingAddress,
            shippingAddress
        )

        val arrayList: ArrayList<PaymentMethodType> = ArrayList()
        arrayList.add(PaymentMethodType.CARD)
        arrayList.add(PaymentMethodType.UPI)
        arrayList.add(PaymentMethodType.NET_BANKING)

        val paymentSession = PaymentSession()
        paymentSession.amount = 100
        paymentSession.receiptEmail = "testaccountowner-stag+592@swirepay.com"
        paymentSession.receiptSms = "+919845789562"
        paymentSession.currencyCode = "INR"
        paymentSession.description = "Test"
        paymentSession.statementDescriptor = "IND Test"
        paymentSession.paymentMethodType = arrayList
        paymentSession.confirmMethod = "AUTOMATIC"
        paymentSession.captureMethod = "AUTOMATIC"

        val btnCheckout: Button = findViewById(R.id.btnCheckout)
        btnCheckout.setOnClickListener {

            SwirepaySdk.doPayment(
                this,
                "#FF0000",
                "#FFFFFF",
                "#FF0000",
                paymentSession,
                customer,
                REQUEST_CODE_CHECKOUT
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val resultText = findViewById<TextView>(R.id.tvResult)
        val responseText = findViewById<TextView>(R.id.tvResponse)

        when (requestCode) {
            REQUEST_CODE_CHECKOUT -> {
                val result = SwirepaySdk.getPaymentCheckout(resultCode, data)
                Log.d("sdk_test", "onActivityResult: $result")
                resultText.text = result.toString()
                responseText.text = result.entity.toString()
            }
        }
    }

    companion object {
        const val REQUEST_CODE_CHECKOUT = 243
    }
}