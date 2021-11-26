package com.swirepay.android_sdk.retrofit

import com.swirepay.android_sdk.checkout.model.*
import com.swirepay.android_sdk.model.*
import com.swirepay.android_sdk.model.PaymentSession
import com.swirepay.android_sdk.model.pusher.AppConfig
import com.swirepay.android_sdk.model.pusher.Request
import com.swirepay.android_sdk.ui.payment_method.SetupSession
import com.swirepay.android_sdk.ui.subscription_button.model.Plan
import com.swirepay.android_sdk.ui.subscription_button.model.PlanRequest
import com.swirepay.android_sdk.ui.subscription_button.model.SubscriptionButton
import com.swirepay.android_sdk.ui.subscription_button.model.SubscriptionButtonRequest
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    @POST("v1/payment-link")
    fun fetchPaymentLink(
        @Body body: PaymentRequest,
        @Header("x-api-key") api_key: String
    ): Call<SuccessResponse<PaymentLink>>

    @GET("v1/payment-link/{paymentLinkGid}")
    fun checkStatus(@Path("paymentLinkGid") paymentLinkGid: String): Call<SuccessResponse<PaymentLink>>

    @POST("v1/plan")
    fun createPlan(
        @Body planRequest: PlanRequest,
        @Header("x-api-key") api_key: String
    ): Call<SuccessResponse<Plan>>

    @POST("v1/subscription-button")
    fun createSubscriptionButton(
        @Body subscriptionButtonRequest: SubscriptionButtonRequest,
        @Header("x-api-key") api_key: String
    ): Call<SuccessResponse<SubscriptionButton>>

    @GET("v1/subscription-button/{subscriptionButtonId}")
    fun getSubscriptionButton(
        @Path("subscriptionButtonId") subscriptionButtonId: String,
        @Header("x-api-key") api_key: String
    ): Call<SuccessResponse<SubscriptionButton>>

    @GET("v1/setup-session/{setupSessionId}")
    fun getSetupSession(
        @Path("setupSessionId") setupSessionId: String,
        @Header("x-api-key") api_key: String
    ): Call<SuccessResponse<SetupSession>>

    @POST("v1/payment-button")
    fun createPaymentButtonRequest(
        @Body paymentButtonRequest: PaymentButtonRequest,
        @Header("x-api-key") api_key: String
    ): Call<SuccessResponse<PaymentButton>>

    @GET("v1/payment-button/{paymentButtonId}")
    fun getPaymentButton(
        @Path("paymentButtonId") paymentButtonId: String,
        @Header("x-api-key") apiKey: String
    ): Call<SuccessResponse<PaymentButton>>

    @GET("v1/invoice-link/{invoiceLinkGid}")
    fun checkInvoiceStatus(@Path("invoiceLinkGid") invoiceLinkGid: String): Call<SuccessResponse<InvoiceResponse>>

    @GET("/v1/terminal/streams/config")
    fun GetTerminalConfigData(): Call<AppConfig>

    @POST("/v1/terminal/streams")
    fun sendPaymentRequest(@Body request: Request): Call<SuccessResponse<String>>

    @Headers("Content-Type:application/json")
    @POST("/v1/payment-method")
    fun createPaymentMethod(
        @Body paymentMethod: PaymentMethodCard?,
        @Header("x-api-key") api_key: String
    ): Call<SuccessResponse<PaymentMethodResponse>>

    @Headers("Content-Type:application/json")
    @POST("/v1/payment-method")
    fun createPaymentMethodUPI(
        @Body paymentMethod: PaymentMethodUpi?,
        @Header("x-api-key") api_key: String
    ): Call<SuccessResponse<PaymentMethodResponse>>

    @Headers("Content-Type:application/json")
    @POST("/v1/payment-method")
    fun createPaymentMethodNetBanking(
        @Body paymentMethod: PaymentMethodNetBank?,
        @Header("x-api-key") api_key: String
    ): Call<SuccessResponse<PaymentMethodResponse>>

    @POST("/v1/payment-session")
    fun createPaymentSession(
        @Body paymentSession: PaymentSession?,
        @Header("x-api-key") api_key: String
    ): Call<SuccessResponse<PaymentSessionResponse>>

    @POST("/v1/customer")
    fun createCustomer(
        @Body customer: SPCustomer?,
        @Header("x-api-key") api_key: String?
    ): Call<SuccessResponse<CustomerResponse>>

    @GET("v1/cashfree-bank")
    fun getAllBanks(
        @Query("isTest") isTest: Boolean
    ): Call<SuccessResponse<List<Banks>>>

    @GET("v1/customer")
    fun getCustomer(
        @Query(value = "name.EQ", encoded = true) name: String,
        @Query(value = "email.EQ", encoded = true) email: String,
        @Query(value = "phoneNumber.EQ", encoded = true) phoneNumber: String,
        @Header("x-api-key") api_key: String?
    ): Call<SuccessResponse<CustomerContent>>

    @GET("v1/payment-method")
    fun getPaymentMethod(
        @Query("customerGid.EQ", encoded = true) customerGid: String,
        @Header("x-api-key") api_key: String?
    ): Call<SuccessResponse<PaymentMethodContent>>
}