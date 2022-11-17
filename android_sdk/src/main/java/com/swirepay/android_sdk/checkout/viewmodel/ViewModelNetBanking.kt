package com.swirepay.android_sdk.checkout.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirepay.android_sdk.SwirepaySdk
import com.swirepay.android_sdk.Utility
import com.swirepay.android_sdk.checkout.model.*
import com.swirepay.android_sdk.model.Banks
import com.swirepay.android_sdk.model.OrderInfo
import com.swirepay.android_sdk.retrofit.ApiClient
import com.swirepay.android_sdk.retrofit.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelNetBanking() : ViewModel() {

    val livePaymentBanks: MutableLiveData<List<Banks>> = MutableLiveData()
    val liveNetBankingResponse: MutableLiveData<PaymentMethodResponse> = MutableLiveData()
    val liveNetBankingSessionResponse: MutableLiveData<PaymentSessionResponse> = MutableLiveData()
    val liveErrorMessages: MutableLiveData<String> = MutableLiveData()

    fun getAllBanks(isTest: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)

            val response =
                apiClient.getAllBanks(isTest).execute()
            if (response.isSuccessful && response.body() != null) {
                val banks = response.body()!!.entity
                livePaymentBanks.postValue(banks)
            } else {
                liveErrorMessages.postValue(Utility.getErrorMsg(response))
                Log.d("sdk_test", "netbanking-getAllBanks: ${response.errorBody()!!.string()}")
            }
        }

    fun createPaymentMethodNetBanking(paymentMethodNetBank: PaymentMethodNetBank) =
        viewModelScope.launch(Dispatchers.IO) {
            val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)

            val response =
                apiClient.createPaymentMethodNetBanking(paymentMethodNetBank, SwirepaySdk.apiKey!!)
                    .execute()
            if (response.isSuccessful && response.body() != null) {
                val paymentResponse = response.body()!!.entity
                liveNetBankingResponse.postValue(paymentResponse)
            } else {
                liveErrorMessages.postValue(Utility.getErrorMsg(response))
                Log.d("sdk_test", "netbanking-payment-method: ${response.code()}")
            }
        }

    fun createPaymentSession(paymentSession: OrderInfo?) =
        viewModelScope.launch(Dispatchers.IO) {
            val apiClient = ApiClient.retrofit.create(ApiInterface::class.java)

            val response =
                apiClient.createPaymentSession(paymentSession, SwirepaySdk.apiKey!!).execute()
            if (response.isSuccessful && response.body() != null) {
                val sessionResponse = response.body()!!.entity
                liveNetBankingSessionResponse.postValue(sessionResponse)
            } else {
                liveErrorMessages.postValue(Utility.getErrorMsg(response))
                Log.d("sdk_test", "netbanking-payment-session: ${response.code()}")
                Log.d("sdk_test", response.errorBody().toString())
            }
        }
}