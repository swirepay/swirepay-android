package com.swirepay.android_sdk

import android.app.Activity
import android.content.Context
import android.content.Intent

object SwirepaySdk {
    fun doPayment(context: Activity , amount : Int , currencyCode : CurrencyType){
        context.startActivityForResult(Intent(context , PaymentActivity::class.java) , 101)
    }

    enum class CurrencyType{
        INR , USD
    }
}