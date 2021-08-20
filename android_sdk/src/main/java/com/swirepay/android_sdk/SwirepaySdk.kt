package com.swirepay.android_sdk

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import com.swirepay.android_sdk.model.*
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object SwirepaySdk {

    const val PLAN_START_DATE: String = "plan_start_date"
    var apiKey: String? = null
    const val PAYMENT_AMOUNT = "payment_amount"
    const val PAYMENT_CUSTOMER = "payment_customer"
    const val PAYMENT_CUSTOMER_GID = "payment_customer_gid"
    const val PAYMENT_CURRENCY = "payment_currency"
    const val PAYMENT_METHOD_TYPES = "payment_method_types"
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

    //https://staging-secure.swirepay.com/connect/create?key=key

    @Throws(KeyNotInitializedException::class)
    fun createPaymentLink(
        context: Activity,
        requestCode: Int,
        amount: Int,
        currencyCode: CurrencyType,
        list: ArrayList<PaymentMethodType>,
        customer: CustomerModel,
        customerGid: String,
        notificationType: NotificationType,
    ) {
        if (apiKey == null || apiKey!!.isEmpty()) throw KeyNotInitializedException()
        context.startActivityForResult(Intent(context, PaymentActivity::class.java).apply {
            putExtra(PAYMENT_AMOUNT, amount)
            putExtra(PAYMENT_CURRENCY, currencyCode.toString())
            putExtra(PAYMENT_METHOD_TYPES, list)
            putExtra(PAYMENT_CUSTOMER, customer)
            if (customerGid != null)
                putExtra(PAYMENT_CUSTOMER_GID, customerGid)
            putExtra(NOTIFICATION_TYPE, notificationType.toString())
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
        name: String,
        amount: Int,
        description: String,
        currencyCode: CurrencyType,
        billingFrequency: String,
        billingPeriod: Int,
        requestCode: Int,
        planStartTime: Date,
        couponId: String? = null,
        listOfTaxIds: ArrayList<String>? = null, planQuantiity: Int, totalCount: Int
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
                putExtra(PLAN_AMOUNT, amount)
                putExtra(PLAN_DESCRIPTION, description)
                putExtra(PLAN_BILLING_FREQ, billingFrequency)
                putExtra(PLAN_BILLING_PERIOD, billingPeriod)
                putExtra(PLAN_CURRENCY_CODE, currencyCode.toString())
                if (couponId != null)
                    putExtra(PLAN_COUPON_ID, couponId)
                if (listOfTaxIds != null) {
                    putStringArrayListExtra(
                        PLAN_TAX_IDS,
                        listOfTaxIds
                    )
                }
                putExtra(PLAN_START_DATE, simpleDateFormat.format(planStartTime))
                putExtra(PLAN_QUANTITY, planQuantiity)
                putExtra(PLAN_TOTAL_COUNT, totalCount)
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
}