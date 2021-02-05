package com.swirepay.android_sdk

import android.app.Activity
import android.content.Intent
import com.swirepay.android_sdk.ui.payment_activity.PaymentActivity

object SwirepaySdk {
    const val PAYMENT_AMOUNT = "payment_amount"
    const val PAYMENT_CURRENCY = "payment_currency"
    fun doPayment(context: Activity , amount : Int , currencyCode : CurrencyType){
        context.startActivityForResult(Intent(context , PaymentActivity::class.java).apply {
            putExtra(PAYMENT_AMOUNT , amount)
            putExtra(PAYMENT_CURRENCY , currencyCode.toString())
        } , 101)
    }

    enum class CurrencyType{
        INR , USD
    }
}