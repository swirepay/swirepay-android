package com.swirepay.android_sdk

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import com.swirepay.android_sdk.model.CurrencyType
import com.swirepay.android_sdk.model.PaymentLink
import com.swirepay.android_sdk.model.Result
import com.swirepay.android_sdk.model.Status
import com.swirepay.android_sdk.ui.payment_activity.PaymentActivity
import com.swirepay.android_sdk.ui.subscription_button.SubscriptionButtonActivity
import com.swirepay.android_sdk.ui.subscription_button.model.SubscriptionButton

object SwirepaySdk {
    var apiKey : String? = null
    const val PAYMENT_AMOUNT = "payment_amount"
    const val PAYMENT_CURRENCY = "payment_currency"
    const val PLAN_NAME = "plan_name"
    const val PLAN_AMOUNT = "plan_amount"
    const val PLAN_DESCRIPTION = "plan_description"
    const val PLAN_CURRENCY_CODE = "plan_currency_code"
    const val PLAN_BILLING_FREQ = "plan_frequency"
    const val PLAN_BILLING_PERIOD = "plan_period"
    const val RESULT = "result"
    const val STATUS = "status"

    @Throws(KeyNotInitializedException::class)
    fun doPayment(context: Activity, amount : Int, currencyCode : CurrencyType, requestCode : Int) {
        if(apiKey == null || apiKey!!.isEmpty()) throw KeyNotInitializedException()
        context.startActivityForResult(Intent(context , PaymentActivity::class.java).apply {
            putExtra(PAYMENT_AMOUNT , amount)
            putExtra(PAYMENT_CURRENCY , currencyCode.toString())
        } , requestCode)
    }

    fun <T : Parcelable?> getResult(resultCode : Int, data : Intent?) : Result<T> {
        if(resultCode == Activity.RESULT_OK){
            if(data != null){
                val statusCode = data.getIntExtra(STATUS , 0)
                if(statusCode == 1){
                    val paymentLink = data.extras?.getParcelable<T>(RESULT)
                    return Result(Status.SUCCEEDED , paymentLink as T)
                }
            }
        }
        val paymentResult = Result<T>(Status.FAILED)
        if(data!= null){
            val reason = data.getStringExtra(PaymentActivity.FAILURE_REASON)
            val paymentLink = data.getParcelableExtra<T?>(RESULT)
            val message = data.getStringExtra(PaymentActivity.PAYMENT_MESSAGE)
            if(paymentLink!= null){
                paymentResult.entity = paymentLink
            }
            if(reason != null)
            paymentResult.reason = reason
            if(message != null)
                paymentResult.message = message
        }
        return paymentResult
    }

    fun initSdk(swirepayApiKey : String){
        apiKey = swirepayApiKey
    }

    @Throws(KeyNotInitializedException::class)
    fun createSubscriptionButton(context : Activity , name : String , amount : Int , description : String , currencyCode : CurrencyType , billingFrequency : String , billingPeriod : Int , requestCode: Int) {
        if(apiKey == null || apiKey!!.isEmpty()) throw KeyNotInitializedException()
        context.startActivityForResult(Intent(context , SubscriptionButtonActivity::class.java).apply {
            putExtra(PLAN_NAME , name)
            putExtra(PLAN_AMOUNT , amount)
            putExtra(PLAN_DESCRIPTION , description)
            putExtra(PLAN_BILLING_FREQ , billingFrequency)
            putExtra(PLAN_BILLING_PERIOD , billingPeriod)
            putExtra(PLAN_CURRENCY_CODE , currencyCode.toString())
        } , requestCode)
    }

    fun getPaymentLink(resultCode: Int, data: Intent?): Result<PaymentLink> {
        return getResult<PaymentLink>(resultCode , data)
    }
    fun getSubscriptionButton(resultCode: Int, data: Intent?): Result<SubscriptionButton> {
        return getResult<SubscriptionButton>(resultCode , data)
    }

}