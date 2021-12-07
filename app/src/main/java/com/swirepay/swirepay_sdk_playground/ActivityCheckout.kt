package com.swirepay.swirepay_sdk_playground

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.swirepay.android_sdk.KeyNotInitializedException
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.SwirepaySdk.REQUEST_CODE_CHECKOUT
import com.swirepay.android_sdk.checkout.model.*
import com.swirepay.android_sdk.model.OrderInfo
import com.swirepay.swirepay_sdk_playground.databinding.CheckoutActivityBinding

class ActivityCheckout : AppCompatActivity() {

    lateinit var binding: CheckoutActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CheckoutActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnInitSdk.setOnClickListener {

            val key = binding.etInitSdk.text.toString()

            if (!TextUtils.isEmpty(key)) {
                SwirepaySdk.initSdk(key)
                Snackbar.make(findViewById(R.id.root), "Key Initialized!", Snackbar.LENGTH_LONG)
                    .show()
            } else
                Snackbar.make(
                    findViewById(R.id.root),
                    "Input a valid key to initialize sdk",
                    Snackbar.LENGTH_SHORT
                ).show()
        }



        val customer = SPCustomer(
            "Muthu", "testaccountowner-stag+789@swirepay.com", "+919845789562"
        )

        val orderInfo = OrderInfo()
        orderInfo.amount = 100
        orderInfo.receiptEmail = "testaccountowner-stag+592@swirepay.com"
        orderInfo.receiptSms = "+919845789562"
        orderInfo.currencyCode = "INR"
        orderInfo.description = "Test"
        orderInfo.statementDescriptor = "IND Test"

        val btnCheckout: Button = findViewById(R.id.btnCheckout)
        btnCheckout.setOnClickListener {

            try {
                SwirepaySdk.doPayment(
                    this,
                    orderInfo,
                    customer,
                    REQUEST_CODE_CHECKOUT,
                    false
                )
            } catch (e: KeyNotInitializedException) {
                Toast.makeText(this, "Key not initialized!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val resultText = findViewById<TextView>(R.id.tvResult)
        val responseText = findViewById<TextView>(R.id.tvResponse)

        when (requestCode) {
            REQUEST_CODE_CHECKOUT -> {
                val result = SwirepaySdk.getPaymentCheckout(resultCode, data)
                Log.d("sdk_test", "onActivityResult: " + Gson().toJson(result))
                resultText.text = Gson().toJson(result)
                responseText.text = result.entity.toString()
            }
        }
    }
}