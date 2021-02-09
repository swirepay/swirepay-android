package com.swirepay.swirepay_sdk_playground

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.model.PaymentLink
import com.swirepay.android_sdk.ui.payment_activity.PaymentActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button : Button = findViewById(R.id.btnPayment)
        button.setOnClickListener {
            SwirepaySdk.doPayment(this , 10000 , SwirepaySdk.CurrencyType.INR , REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE){
            val paymentResult = SwirepaySdk.getResult(resultCode , data)
            Log.d("sdk_test", "onActivityResult: $paymentResult")
        }
    }

    companion object{
        const val REQUEST_CODE = 1001
    }

}