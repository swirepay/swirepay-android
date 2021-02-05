package com.swirepay.swirepay_sdk_playground

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.swirepay.android_sdk.PaymentActivity
import com.swirepay.android_sdk.SwirepaySdk

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button : Button = findViewById(R.id.btnPayment)
        button.setOnClickListener {
            SwirepaySdk.doPayment(this , 10000 , SwirepaySdk.CurrencyType.INR)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("sdk_test", "onActivityResult: ${data?.getIntExtra("payment_status)" , 0)}")
    }

}