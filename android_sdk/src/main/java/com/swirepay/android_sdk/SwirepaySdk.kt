package com.swirepay.android_sdk

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.text.TextUtils
import com.google.gson.Gson
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.PrivateChannelEventListener
import com.pusher.client.channel.PusherEvent
import com.pusher.client.util.HttpAuthorizer
import com.swirepay.android_sdk.`interface`.IPusherCallback
import com.swirepay.android_sdk.model.*
import com.swirepay.android_sdk.model.pusher.AppConfig
import com.swirepay.android_sdk.model.pusher.CipherConversion
import com.swirepay.android_sdk.model.pusher.Request
import com.swirepay.android_sdk.retrofit.ApiClient
import com.swirepay.android_sdk.retrofit.ApiInterface
import com.swirepay.android_sdk.retrofit.PusherClient
import com.swirepay.android_sdk.ui.create_account.CreateAccountActivity
import com.swirepay.android_sdk.ui.invoice.InvoiceActivity
import com.swirepay.android_sdk.ui.payment_activity.PaymentActivity
import com.swirepay.android_sdk.ui.payment_activity.model.CustomerModel
import com.swirepay.android_sdk.ui.payment_button.PaymentButtonActivity
import com.swirepay.android_sdk.ui.payment_method.PaymentMethodActivity
import com.swirepay.android_sdk.ui.payment_method.SetupSession
import com.swirepay.android_sdk.ui.subscription_button.SubscriptionButtonActivity
import com.swirepay.android_sdk.ui.subscription_button.model.Account
import com.swirepay.android_sdk.ui.subscription_button.model.SubscriptionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

object SwirepaySdk {

    const val PLAN_START_DATE: String = "plan_start_date"
    var apiKey: String? = null
    const val PAYMENT_AMOUNT = "payment_amount"
    const val PAYMENT_CUSTOMER = "payment_customer"
    const val PAYMENT_CUSTOMER_GID = "payment_customer_gid"
    const val PAYMENT_CURRENCY = "payment_currency"
    const val PAYMENT_METHOD_TYPES = "payment_method_types"
    const val DUE_DATE = "due_date"
    const val PLAN_NAME = "plan_name"
    const val PLAN_AMOUNT = "plan_amount"
    const val PLAN_DESCRIPTION = "plan_description"
    const val PLAN_CURRENCY_CODE = "plan_currency_code"
    const val PLAN_COUPON_ID = "plan_coupon_id"
    const val PLAN_TOTAL_COUNT = "plan_total_count"
    const val PLAN_QUANTITY = "plan_quantity"
    const val PLAN_TAX_IDS = "plan_tax_ids"
    const val PLAN_BILLING_FREQ = "plan_frequency"
    const val PLAN_BILLING_PERIOD = "plan_period"
    const val PAYMENT_METHOD_URL = "payment_method_url"
    const val NOTIFICATION_TYPE = "notificationType"
    const val INVOICE_GID = "invoiceGid"
    const val RESULT = "result"
    const val STATUS = "status"
    const val PAYMENT_REQUEST = "payment_request"

    //https://staging-secure.swirepay.com/connect/create?key=key

    @Throws(KeyNotInitializedException::class)
    fun createPaymentLink(
        context: Activity,
        requestCode: Int,
        amount: Int,
        currencyCode: CurrencyType,
        list: ArrayList<PaymentMethodType>,
        customer: CustomerModel,
        customerGid: String?,
        notificationType: NotificationType,
        dueDate: String?,
    ) {
        if (apiKey == null || apiKey!!.isEmpty()) throw KeyNotInitializedException()

        context.startActivityForResult(Intent(context, PaymentActivity::class.java).apply {
            putExtra(PAYMENT_AMOUNT, amount)
            putExtra(PAYMENT_CURRENCY, currencyCode.toString())
            putExtra(PAYMENT_METHOD_TYPES, list)
            if (TextUtils.isEmpty(customerGid) || customerGid == null)
                putExtra(PAYMENT_CUSTOMER, customer)
            if (customerGid != null)
                putExtra(PAYMENT_CUSTOMER_GID, customerGid)
            putExtra(NOTIFICATION_TYPE, notificationType.toString())
            if (!TextUtils.isEmpty(dueDate) || dueDate != "")
                putExtra(DUE_DATE, dueDate)
        }, requestCode)
    }

    private fun <T : Parcelable?> getResult(resultCode: Int, data: Intent?): Result<T> {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val statusCode = data.getIntExtra(STATUS, 0)
                if (statusCode == 1) {
                    val paymentLink = data.extras?.getParcelable<T>(RESULT)
                    return Result(Status.SUCCEEDED, paymentLink as T)
                }
            }
        }
        val paymentResult = Result<T>(Status.FAILED)
        if (data != null) {
            val reason = data.getStringExtra(PaymentActivity.FAILURE_REASON)
            val paymentLink = data.getParcelableExtra<T?>(RESULT)
            val message = data.getStringExtra(PaymentActivity.PAYMENT_MESSAGE)
            if (paymentLink != null) {
                paymentResult.entity = paymentLink
            }
            if (reason != null)
                paymentResult.reason = reason
            if (message != null)
                paymentResult.message = message
        }
        return paymentResult
    }

    fun initSdk(swirepayApiKey: String) {
        apiKey = swirepayApiKey
    }

    @Throws(KeyNotInitializedException::class)
    fun createSubscriptionButton(
        context: Activity,
        requestCode: Int,
        name: String,
        planAmount: Int,
        description: String,
        currencyCode: CurrencyType,
        planBillingFrequency: String,
        planBillingPeriod: Int,
        planStartDate: Date,
        taxRates: List<String>?,
        couponId: String?,
        planQuantity: Int,
        planTotalPayments: Int,
        status: String,
    ) {
        if (apiKey == null || apiKey!!.isEmpty()) throw KeyNotInitializedException()
        //"2021-02-24T18:30:00"
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault())
        context.startActivityForResult(
            Intent(
                context,
                SubscriptionButtonActivity::class.java
            ).apply {
                putExtra(PLAN_NAME, name)
                putExtra(PLAN_AMOUNT, planAmount)
                putExtra(PLAN_DESCRIPTION, description)
                putExtra(PLAN_BILLING_FREQ, planBillingFrequency)
                putExtra(PLAN_BILLING_PERIOD, planBillingPeriod)
                putExtra(PLAN_CURRENCY_CODE, currencyCode.toString())
                if (couponId != null)
                    putExtra(PLAN_COUPON_ID, couponId)
                if (taxRates != null) {
                    putStringArrayListExtra(
                        PLAN_TAX_IDS,
                        taxRates as java.util.ArrayList<String>?
                    )
                }
                putExtra(PLAN_START_DATE, simpleDateFormat.format(planStartDate))
                putExtra(PLAN_QUANTITY, planQuantity)
                putExtra(PLAN_TOTAL_COUNT, planTotalPayments)
                putExtra(STATUS, status)
            }, requestCode
        )
    }

    @Throws(KeyNotInitializedException::class)
    fun createPaymentMethod(context: Activity, requestCode: Int) {
        if (apiKey == null || apiKey!!.isEmpty()) throw KeyNotInitializedException()
        context.startActivityForResult(Intent(context, PaymentMethodActivity::class.java).apply {
        }, requestCode)
    }

    @Throws(KeyNotInitializedException::class)
    fun createAccount(context: Activity, requestCode: Int) {
        if (apiKey == null || apiKey!!.isEmpty()) throw KeyNotInitializedException()
        context.startActivityForResult(Intent(context, CreateAccountActivity::class.java).apply {
        }, requestCode)
    }

    fun getPaymentLink(resultCode: Int, data: Intent?): Result<PaymentLink> {
        return getResult(resultCode, data)
    }

    fun getSubscriptionButton(resultCode: Int, data: Intent?): Result<SubscriptionButton> {
        return getResult(resultCode, data)
    }

    fun getPaymentMethod(resultCode: Int, data: Intent?): Result<SetupSession> {
        return getResult(resultCode, data)
    }

    fun getAccount(resultCode: Int, data: Intent?): Result<Account> {
        return getResult(resultCode, data)
    }

    fun getPaymentButton(resultCode: Int, data: Intent?): Result<PaymentButton> {
        return getResult(resultCode, data)
    }

    fun getInvoice(resultCode: Int, data: Intent?): Result<PaymentButton> {
        return getResult(resultCode, data)
    }

    @Throws(KeyNotInitializedException::class)
    fun createPaymentButton(
        context: Activity,
        requestCode: Int,
        amount: Int,
        description: String,
        currencyType: CurrencyType,
        notificationType: NotificationType,
        listOfPaymentMethods: List<PaymentMethodType>
    ) {
        if (apiKey == null || apiKey!!.isEmpty()) throw KeyNotInitializedException()
        if (listOfPaymentMethods.isEmpty()) throw PaymentTypeRequiredException()
        val list = listOfPaymentMethods.map {
            it.toString()
        }.toList()
        val paymentButtonRequest = PaymentButtonRequest(
            amount,
            currencyType.toString(),
            description,
            notificationType.toString(),
            list
        )
        context.startActivityForResult(Intent(context, PaymentButtonActivity::class.java).apply {
            putExtra(PaymentButtonActivity.PAYMENT_BUTTON_REQUEST, paymentButtonRequest)
        }, requestCode)
    }

    @Throws(KeyNotInitializedException::class)
    fun showInvoicePayment(context: Activity, requestCode: Int, invoiceGid: String) {

        if (apiKey == null || apiKey!!.isEmpty()) throw KeyNotInitializedException()

        context.startActivityForResult(Intent(context, InvoiceActivity::class.java).apply {
            putExtra(INVOICE_GID, invoiceGid)
        }, requestCode)
    }

    @Throws(KeyNotInitializedException::class)
    fun sendPaymentRequest(
        paymentReq: PaymentRequest, callback: IPusherCallback
    ) {

        if (apiKey == null || apiKey!!.isEmpty()) throw KeyNotInitializedException()

        val apiClient = PusherClient.retrofit.create(ApiInterface::class.java)

        apiClient.GetTerminalConfigData().enqueue(object : Callback<AppConfig> {
            override fun onResponse(call: Call<AppConfig>, response: Response<AppConfig>) {

                val appConfig = response.body()

                sendRequest(paymentReq, appConfig!!, callback)

            }

            override fun onFailure(call: Call<AppConfig>, t: Throwable) {
                callback.onFailure(call, t)
            }
        })
    }


    fun sendRequest(
        paymentReq: PaymentRequest,
        appConfig: AppConfig,
        callback: IPusherCallback
    ) {

        val apiClient = PusherClient.retrofit.create(ApiInterface::class.java)

        val requestStr = Gson().toJson(paymentReq)
        val encrypted = CipherConversion.encrypt(requestStr, appConfig.posKey)

        apiClient.sendPaymentRequest(Request(encrypted))
            .enqueue(object : Callback<SuccessResponse<String>> {
                override fun onResponse(
                    call: Call<SuccessResponse<String>>,
                    response: Response<SuccessResponse<String>>
                ) {
                    connectPusher(appConfig.appKey, response.body().toString(), callback)
                }

                override fun onFailure(call: Call<SuccessResponse<String>>, t: Throwable) {
                    callback.onFailure(call, t)
                }
            })
    }

    fun connectPusher(
        appKey: String,
        channelId: String,
        callback: IPusherCallback
    ) {

        val authorizer = HttpAuthorizer("${BuildConfig.AUTH_ENDPOINT}")

        val options = PusherOptions()
            .setAuthorizer(authorizer)
            .setHost("${BuildConfig.PUSHER_URL}")
            .setWssPort(443)

        var pusher = Pusher(appKey, options)
        pusher.connect()

        var channel = pusher.subscribePrivate(channelId)
        channel.bind("client-payment_res_event", object : PrivateChannelEventListener {
            override fun onEvent(event: PusherEvent?) {

                callback.onEvent(event)
            }

            override fun onSubscriptionSucceeded(channelName: String?) {
                println("subscription successful")
                callback.onSubscriptionSucceeded(channelName)
            }

            override fun onAuthenticationFailure(message: String?, e: Exception?) {
                callback.onAuthenticationFailure(message, e)
            }
        })
    }
}