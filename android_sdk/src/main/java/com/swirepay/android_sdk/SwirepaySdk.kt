package com.swirepay.android_sdk

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import com.swirepay.android_sdk.model.CurrencyType
import com.swirepay.android_sdk.model.PaymentLink
import com.swirepay.android_sdk.model.Result
import com.swirepay.android_sdk.model.Status
import com.swirepay.android_sdk.ui.create_account.CreateAccountActivity
import com.swirepay.android_sdk.ui.payment_activity.PaymentActivity
import com.swirepay.android_sdk.ui.payment_method.PaymentMethodActivity
import com.swirepay.android_sdk.ui.payment_method.SetupSession
import com.swirepay.android_sdk.ui.subscription_button.SubscriptionButtonActivity
import com.swirepay.android_sdk.ui.subscription_button.model.SubscriptionButton
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object SwirepaySdk {
    const val PLAN_START_DATE: String = "plan_start_date"
    var apiKey : String? = null
    const val PAYMENT_AMOUNT = "payment_amount"
    const val PAYMENT_CURRENCY = "payment_currency"
    const val PAYMENT_METHOD_TYPES = "payment_method_types"
    const val PLAN_NAME = "plan_name"
    const val PLAN_AMOUNT = "plan_amount"
    const val PLAN_DESCRIPTION = "plan_description"
    const val PLAN_CURRENCY_CODE = "plan_currency_code"
    const val PLAN_BILLING_FREQ = "plan_frequency"
    const val PLAN_BILLING_PERIOD = "plan_period"
    const val PAYMENT_METHOD_URL = "payment_method_url"
    const val RESULT = "result"
    const val STATUS = "status"

    //https://staging-secure.swirepay.com/connect/create?key=key

    @Throws(KeyNotInitializedException::class)
    fun doPayment(context: Activity, amount : Int, currencyCode : CurrencyType, requestCode : Int , list : List<String>) {
        if(apiKey == null || apiKey!!.isEmpty()) throw KeyNotInitializedException()
        context.startActivityForResult(Intent(context , PaymentActivity::class.java).apply {
            putExtra(PAYMENT_AMOUNT , amount)
            putExtra(PAYMENT_CURRENCY , currencyCode.toString())
            putStringArrayListExtra(PAYMENT_METHOD_TYPES , list  as ArrayList<String>)
        } , 1001)
    }

    private fun <T : Parcelable?> getResult(resultCode : Int, data : Intent?) : Result<T> {
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
    fun createSubscriptionButton(context : Activity , name : String , amount : Int , description : String , currencyCode : CurrencyType , billingFrequency : String , billingPeriod : Int , requestCode: Int , planStartTime : Date) {
        if(apiKey == null || apiKey!!.isEmpty()) throw KeyNotInitializedException()
        //"2021-02-24T18:30:00"
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss" , Locale.getDefault())
        context.startActivityForResult(Intent(context , SubscriptionButtonActivity::class.java).apply {
            putExtra(PLAN_NAME , name)
            putExtra(PLAN_AMOUNT , amount)
            putExtra(PLAN_DESCRIPTION , description)
            putExtra(PLAN_BILLING_FREQ , billingFrequency)
            putExtra(PLAN_BILLING_PERIOD , billingPeriod)
            putExtra(PLAN_CURRENCY_CODE , currencyCode.toString())
            putExtra(PLAN_START_DATE , simpleDateFormat.format(planStartTime))
        } , requestCode)
    }

    @Throws(KeyNotInitializedException::class)
    fun createPaymentMethod(context : Activity , requestCode: Int){
        if(apiKey == null || apiKey!!.isEmpty()) throw KeyNotInitializedException()
        context.startActivityForResult(Intent(context , PaymentMethodActivity::class.java).apply {
        } , requestCode)
    }

    @Throws(KeyNotInitializedException::class)
    fun createAccount(context : Activity , requestCode: Int){
        if(apiKey == null || apiKey!!.isEmpty()) throw KeyNotInitializedException()
        context.startActivityForResult(Intent(context , CreateAccountActivity::class.java).apply {
        } , requestCode)
    }

    fun getPaymentLink(resultCode: Int, data: Intent?): Result<PaymentLink> {
        return getResult(resultCode , data)
    }
    fun getSubscriptionButton(resultCode: Int, data: Intent?): Result<SubscriptionButton> {
        return getResult(resultCode , data)
    }
    fun getSetupSession(resultCode: Int, data: Intent?): Result<SetupSession> {
        return getResult(resultCode , data)
    }

}