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

class ViewModelPaymentUPI(
) : ViewModel() {
    val liveCustomerResponse: MutableLiveData<CustomerResponse> = MutableLiveData()
    val livePaymentMethodResponse: MutableLiveData<PaymentMethodResponse> = MutableLiveData()
    val livePaymentSessionResponse: MutableLiveData<PaymentSessionResponse> = MutableLiveData()
    val liveErrorMessages: MutableLiveData<String> = MutableLiveData()

    fun createCustomer(customer: SPCustomer?) =
        viewModelScope.launch(Dispatchers.IO) {
            val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)

            val response = apiClient.createCustomer(customer, SwirepaySdk.apiKey).execute()
            if (response.isSuccessful && response.body() != null) {
                val customerResponse = response.body()!!.entity
                liveCustomerResponse.postValue(customerResponse)
            } else {
                liveErrorMessages.postValue("Error code : ${response.code()}")
                Log.d("sdk_test", "upi-createCustomer: ${response.code()}")
            }
        }

    fun createPaymentMethodUPI(paymentMethodUpi: PaymentMethodUpi) =
        viewModelScope.launch(Dispatchers.IO) {
            val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)

            val response =
                apiClient.createPaymentMethodUPI(paymentMethodUpi, SwirepaySdk.apiKey!!).execute()
            if (response.isSuccessful && response.body() != null) {
                val paymentResponse = response.body()!!.entity
                livePaymentMethodResponse.postValue(paymentResponse)
            } else {
                liveErrorMessages.postValue("Error code : ${response.code()}")
                Log.d("sdk_test", "upi-payment-method: ${response.code()}")
            }
        }

    fun createPaymentSession(orderInfo: OrderInfo?) =
        viewModelScope.launch(Dispatchers.IO) {
            val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)

            val response =
                apiClient.createPaymentSession(orderInfo, SwirepaySdk.apiKey!!).execute()
            if (response.isSuccessful && response.body() != null) {
                val sessionResponse = response.body()!!.entity
                livePaymentSessionResponse.postValue(sessionResponse)
            } else {
                liveErrorMessages.postValue("Error code : ${response.code()}")
                Log.d("sdk_test", "upi-payment-session: ${response.message()}")
            }
        }
}