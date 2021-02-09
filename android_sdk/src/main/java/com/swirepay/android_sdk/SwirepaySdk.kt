package com.swirepay.android_sdk

import android.app.Activity
import android.content.Intent
import com.swirepay.android_sdk.model.PaymentLink
import com.swirepay.android_sdk.model.PaymentResult
import com.swirepay.android_sdk.ui.payment_activity.PaymentActivity

object SwirepaySdk {
    const val PAYMENT_AMOUNT = "payment_amount"
    const val PAYMENT_CURRENCY = "payment_currency"
    fun doPayment(context: Activity , amount : Int , currencyCode : CurrencyType , requestCode : Int){
        context.startActivityForResult(Intent(context , PaymentActivity::class.java).apply {
            putExtra(PAYMENT_AMOUNT , amount)
            putExtra(PAYMENT_CURRENCY , currencyCode.toString())
        } , requestCode)
    }

    enum class CurrencyType{
        INR , USD
    }

    enum class PaymentStatus{
        SUCCEEDED , FAILED
    }



    fun getResult(resultCode : Int , data : Intent?) : PaymentResult {
        if(resultCode == Activity.RESULT_OK){
            if(data != null){
                val statusCode = data.getIntExtra(PaymentActivity.PAYMENT_STATUS , 0)
                if(statusCode == 1){
                    val paymentLink = data.extras?.getParcelable<PaymentLink>(PaymentActivity.PAYMENT_RESULT)
                    return PaymentResult(PaymentStatus.SUCCEEDED , paymentLink)
                }
            }
        }
        val paymentResult = PaymentResult(PaymentStatus.FAILED)
        if(data!= null){
            val reason = data.getStringExtra(PaymentActivity.PAYMENT_FAILURE_REASON)
            val paymentLink = data.getParcelableExtra<PaymentLink?>(PaymentActivity.PAYMENT_RESULT)
            if(paymentLink!= null){
                paymentResult.entity = paymentLink
            }
            if(reason != null)
            paymentResult.reason = reason
        }
        return paymentResult
    }

}