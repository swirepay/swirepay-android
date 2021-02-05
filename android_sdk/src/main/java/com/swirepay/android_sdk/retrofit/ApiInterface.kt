package com.swirepay.android_sdk.retrofit

import com.swirepay.android_sdk.ui.payment_activity.PaymentRequest
import com.swirepay.android_sdk.model.PaymentLink
import com.swirepay.android_sdk.model.SuccessResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @POST("v1/payment-link")
    fun fetchPaymentLink(@Body body: PaymentRequest, @Header("x-api-key") api_key: String): Call<SuccessResponse<PaymentLink>>

    @GET("v1/payment-link/{paymentLinkGid}")
    fun checkStatus(@Path("paymentLinkGid") paymentLinkGid: String)

}