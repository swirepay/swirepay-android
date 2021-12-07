package com.swirepay.android_sdk.checkout.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.checkout.model.*
import com.swirepay.android_sdk.model.OrderInfo
import com.swirepay.android_sdk.retrofit.ApiClient
import com.swirepay.android_sdk.retrofit.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelPaymentSession() : ViewModel() {

    val livePaymentMethod: MutableLiveData<PaymentMethodResponse> = MutableLiveData()
    val livePaymentSession: MutableLiveData<PaymentSessionResponse> = MutableLiveData()
    val getPaymentSessionResponse: MutableLiveData<PaymentSessionResponse> = MutableLiveData()
    val paymentMethodResults: MutableLiveData<PaymentMethodContent> = MutableLiveData()
    val liveErrorMessages: MutableLiveData<String> = MutableLiveData()

    fun createPaymentMethod(paymentMethodCard: PaymentMethodCard?) =
        viewModelScope.launch(Dispatchers.IO) {
            val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)

            val response =
                apiClient.createPaymentMethod(paymentMethodCard, SwirepaySdk.apiKey!!).execute()
            if (response.isSuccessful && response.body() != null) {
                val paymentResponse = response.body()!!.entity
                livePaymentMethod.postValue(paymentResponse)
            } else {
                liveErrorMessages.postValue("Error code : ${response.code()}")
                Log.d("sdk_test", "card-payment-method: ${response.code()}")
            }
        }

    fun createPaymentSession(orderInfo: OrderInfo?) =
        viewModelScope.launch(Dispatchers.IO) {
            val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)

            val response =
                apiClient.createPaymentSession(orderInfo, SwirepaySdk.apiKey!!).execute()
            if (response.isSuccessful && response.body() != null) {
                val sessionResponse = response.body()!!.entity
                livePaymentSession.postValue(sessionResponse)
            } else {
                liveErrorMessages.postValue("Error code : ${response.code()}")
                Log.d("sdk_test", "card-payment-session: ${response.code()}")
            }
        }

    fun getPaymentSession(paymentSession: String?, secret: String?) =
        viewModelScope.launch(Dispatchers.IO) {
            val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)

            val response =
                apiClient.getPaymentSession(
                    paymentSession,
                    paymentSession,
                    secret,
                    SwirepaySdk.apiKey!!
                ).execute()
            if (response.isSuccessful && response.body() != null) {
                val sessionResponse = response.body()!!.entity
                getPaymentSessionResponse.postValue(sessionResponse)
            } else {
                liveErrorMessages.postValue("Error code : ${response.code()}")
                Log.d("sdk_test", "card-payment-session: ${response.code()}")
            }
        }

    fun getPaymentMethod(customerGid: String) =
        viewModelScope.launch(Dispatchers.IO) {
            val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)

            val response =
                apiClient.getPaymentMethod(
                    customerGid,
                    true,
                    SwirepaySdk.apiKey!!
                ).execute()
            if (response.isSuccessful && response.body() != null) {
                val sessionResponse = response.body()!!.entity
                paymentMethodResults.postValue(sessionResponse)
            } else {
                liveErrorMessages.postValue("Error code : ${response.code()}")
                Log.d("sdk_test", "payment-method: ${response.code()}")
            }
        }
}