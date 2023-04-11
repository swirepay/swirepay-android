package com.swirepay.swirepay_sdk_playground

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.ui.nativepayment.NativePaymentActivity
import com.swirepay.swirepay_sdk_playground.databinding.ActivityMainBinding
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etInitSdk.setText("")

        //USD
//        binding.etInitSdk.setText("sk_test_52Y1EaICF09by7oT8HCOpkyBdFFQGp6g")

        //INR
//        binding.etInitSdk.setText("")//sk_test_dGEOrl2wzQ9nVLpQxq1WmdZJNDPJB6zF
          //binding.etInitSdk.setText("sk_test_dGEOrl2wzQ9nVLpQxq1WmdZJNDPJB6zF")//india account
         //binding.etInitSdk.setText("sk_test_GMP6MilKUyOMxMervlPKgn43arU9lxGg")//usaccount
//         binding.etInitSdk.setText("sk_test_7GsTyKNtgZxj0UD6eT2ICqvHQZUvWKw5")//usaccount
//         binding.etInitSdk.setText("sk_test_5X5U7tb1ww47cukHGucCMXA9hBXi4yrr")//usaccount
        //binding.etInitSdk.setText("GMP6MilKUyOMxMervlPKgn43arU9lxGg")
//        binding.etInitSdk.setText("sk_test_7lrEzsAai0aYDod9Op7w4dLYSIqLHpuT")
//        binding.etInitSdk.setText("sk_test_vts2SlOTnOIEF0IA4TCKEfDlK3Zao8DF")
//        binding.etInitSdk.setText("sk_test_xikzjS5KEH37YRGeXBLzuUaQ2nvMhB5w")
        //binding.etInitSdk.setText("pk_test_NZahOuYDntC2yRroIpwKNo4eCArlQFu2") //public Usaccount rajeshmec

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

        binding.btnPayment.setOnClickListener {

            startActivity(Intent(this, PaymentLinkActivity::class.java))
        }

        binding.btnInvoice.setOnClickListener {

            startActivity(Intent(this, InvoicePaymentActivity::class.java))
        }

        val btnSubscriptionButton: Button = findViewById(R.id.btnSubscriptionButton)
        btnSubscriptionButton.setOnClickListener {

            startActivity(Intent(this, SubscriptionButtonActivity::class.java))
        }

        val btnPaymentMethod: Button = findViewById(R.id.btnPaymentMethod)
        btnPaymentMethod.setOnClickListener {
            try {
                SwirepaySdk.createPaymentMethod(this, REQUEST_CODE_PAYMENT_METHOD)
            } catch (e: Exception) {
                Toast.makeText(this, "Key not initialized!", Toast.LENGTH_SHORT).show()
            }
        }

        val btnCreateAccount: Button = findViewById(R.id.btnAccount)
        btnCreateAccount.setOnClickListener {
            try {
                SwirepaySdk.createAccount(this, REQUEST_CODE_CONNECT_ACCOUNT)
            } catch (e: Exception) {
                Toast.makeText(this, "Key not initialized!", Toast.LENGTH_SHORT).show()
            }
        }

        val btnCreateToken: Button = findViewById(R.id.btnToken)
        btnCreateToken.setOnClickListener {
            startActivity(Intent(this, TokenActivity::class.java))
        }

        val btnPaymentButton: Button = findViewById(R.id.btnPaymentButton)
        btnPaymentButton.setOnClickListener {

            try {
                startActivity(Intent(this, PaymentButtonActivity::class.java))
            } catch (e: Exception) {
                Toast.makeText(this, "Key not initialized!", Toast.LENGTH_SHORT).show()
            }
        }

        val btnNativePayment: Button = findViewById(R.id.btnNativePayment)
        btnNativePayment.setOnClickListener {
            try {
                startActivity(Intent(this, NativePaymentActivity::class.java))
            } catch (e: Exception) {
                Toast.makeText(this, "Key not initialized!", Toast.LENGTH_SHORT).show()
            }
        }

        val btnCheckout: Button = findViewById(R.id.btnCheckout)
        btnCheckout.setOnClickListener {
            startActivity(Intent(this, ActivityCheckout::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val resultText = findViewById<TextView>(R.id.tvResult)
        val responseText = findViewById<TextView>(R.id.tvResponse)
        when (requestCode) {
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
            REQUEST_CODE_TOKEN -> {
                val result = SwirepaySdk.getToken(resultCode, data)
                Log.d("sdk_test", "onActivityResult: $result")
                resultText.text = result.toString()
                responseText.text = result.entity.toString()
            }
        }
    }

    companion object {
        const val REQUEST_CODE_PAYMENT_METHOD = 1003
        const val REQUEST_CODE_CONNECT_ACCOUNT = 1004
        const val REQUEST_CODE_TOKEN = 1005
    }

}